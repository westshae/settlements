package cc.altoya.settlements.Build;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class BuildUtil {
    public static FileConfiguration getStructureConfig() {
        return GeneralUtil.getPluginConfig("settlements", "builds.yml");
    }

    public static void saveStructureConfig(FileConfiguration config) {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(),
                "builds.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewStructure(Player player, Chunk chunk, String type) {
        FileConfiguration config = getStructureConfig();

        config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".owner", GeneralUtil.getKeyFromPlayer(player));
        config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".type", type);
        saveStructureConfig(config);
    }

    public static String getStructureType(Chunk chunk) {
        FileConfiguration config = getStructureConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }
        return config.getString("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".type");
    }

    public static void saveBlockAsStructureBlock(Block block, boolean isInteractive) {
        FileConfiguration config = getStructureConfig();
        config.set("builds.all_blocks." + GeneralUtil.getKeyFromBlock(block) + ".interactive", isInteractive);

        String blockPath = (isInteractive) ? ".interactiveBlocks" : ".blocks";

        List<String> structureBlocks = config.getStringList("builds." + GeneralUtil.getKeyFromChunk(block.getChunk()) + blockPath);
        structureBlocks.add(GeneralUtil.getKeyFromBlock(block));

        config.set("builds." + GeneralUtil.getKeyFromChunk(block.getChunk()) + blockPath,
                structureBlocks);
        saveStructureConfig(config);
    }

    public static boolean isBlockStructureBlock(Block block) {
        FileConfiguration config = getStructureConfig();
        return config.contains("builds.all_blocks." + GeneralUtil.getKeyFromBlock(block));
    }

    public static boolean isBlockInteractiveBlock(Block block) {
        FileConfiguration config = getStructureConfig();
        return config.getBoolean("builds.all_blocks." + GeneralUtil.getKeyFromBlock(block) + ".interactive");
    }

    public static void placeStructureBlock(Player player, Location location, Material material, BlockData blockData) {
        Block currentBlockAtLocation = location.getBlock();
        currentBlockAtLocation.setType(material);
        currentBlockAtLocation.setBlockData(blockData);
        saveBlockAsStructureBlock(currentBlockAtLocation, false);
    }

    public static void placeInteractiveBlock(Player player, Location location, Material material, BlockData blockData) {
        Block currentBlockAtLocation = location.getBlock();
        currentBlockAtLocation.setType(material);
        currentBlockAtLocation.setBlockData(blockData);
        saveBlockAsStructureBlock(currentBlockAtLocation, true);
    }

    public static boolean isChunkStructure(Chunk chunk) {
        FileConfiguration config = getStructureConfig();
        return config.contains("builds." + GeneralUtil.getKeyFromChunk(chunk));
    }

    public static Integer getResourcesFromStructure(Chunk chunk) {
        FileConfiguration config = getStructureConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }
        return config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".resources");
    }

    public static void editResources(Player player, Chunk chunk, int amount) {
        FileConfiguration config = getStructureConfig();
        if (!isChunkStructure(chunk)) {
            return;
        }
        config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".resources",
                getResourcesFromStructure(chunk) + amount);
        saveStructureConfig(config);
        ChatUtil.sendSuccessMessage(player, "Resources now at " + getResourcesFromStructure(chunk));
        onResourceAmountGivePlayerItem(player, chunk);
    }

    public static void onResourceAmountGivePlayerItem(Player player, Chunk chunk) {
        if (!isChunkStructure(chunk)) {
            return;
        }
        Integer resources = getResourcesFromStructure(chunk);
        if (resources == null) {
            return;
        }
        if (resources < 50) {
            return;
        }
        player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
        ChatUtil.sendSuccessMessage(player, "You reached 50 resources, here's a diamond.");
        editResources(player, chunk, -50);
    }


}
