package cc.altoya.settlements.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StructureUtil {
    public static FileConfiguration getStructureConfig() {
        return GeneralUtil.getPluginConfig("settlements", "structures.yml");
    }

    public static void saveStructureConfig(FileConfiguration config) {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(),
                "structures.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewStructure(Player player, String type) {
        FileConfiguration config = getStructureConfig();
        Chunk chunk = player.getLocation().getChunk();

        config.set("structures." + GeneralUtil.getKeyFromChunk(chunk) + ".owner", GeneralUtil.getKeyFromPlayer(player));
        config.set("structures." + GeneralUtil.getKeyFromChunk(chunk) + ".type", type);
        saveStructureConfig(config);
    }

    public static void saveBlockAsStructureBlock(Block block, boolean isInteractive) {
        FileConfiguration config = getStructureConfig();
        config.set("structures.all_blocks." + GeneralUtil.getKeyFromBlock(block) + ".interactive", isInteractive);

        String blockPath = (isInteractive) ? ".interactiveBlocks" : ".blocks";

        List<String> structureBlocks = GeneralUtil.createListFromString(
                (String) config.get("structures." + GeneralUtil.getKeyFromChunk(block.getChunk()) + blockPath));
        structureBlocks.add(GeneralUtil.getKeyFromBlock(block));

        config.set("structures." + GeneralUtil.getKeyFromChunk(block.getChunk()) + blockPath,
                GeneralUtil.createStringFromList(structureBlocks));
        saveStructureConfig(config);
    }

    public static boolean isBlockStructureBlock(Block block) {
        FileConfiguration config = getStructureConfig();
        return config.contains("structures.all_blocks." + GeneralUtil.getKeyFromBlock(block));
    }

    public static boolean isBlockInteractiveBlock(Block block) {
        FileConfiguration config = getStructureConfig();
        return config.getBoolean("structures.all_blocks." + GeneralUtil.getKeyFromBlock(block) + ".interactive");
    }

    public static void placeStructureBlock(Player player, Location location, Material material) {
        Block currentBlockAtLocation = location.getBlock();
        currentBlockAtLocation.setType(material);
        saveBlockAsStructureBlock(currentBlockAtLocation, false);
    }

    public static void placeInteractiveBlock(Player player, Location location, Material material) {
        Block currentBlockAtLocation = location.getBlock();
        currentBlockAtLocation.setType(material);
        saveBlockAsStructureBlock(currentBlockAtLocation, true);
    }

    public static boolean isChunkStructure(Chunk chunk) {
        FileConfiguration config = getStructureConfig();
        return config.contains("structures." + GeneralUtil.getKeyFromChunk(chunk));
    }

    public static Integer getResourcesFromStructure(Chunk chunk) {
        FileConfiguration config = getStructureConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }
        return config.getInt("structures." + GeneralUtil.getKeyFromChunk(chunk) + ".resources");
    }

    public static void editResources(Player player, Chunk chunk, int amount) {
        FileConfiguration config = getStructureConfig();
        if (!isChunkStructure(chunk)) {
            return;
        }
        config.set("structures." + GeneralUtil.getKeyFromChunk(chunk) + ".resources",
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

    public static boolean doesStructureNameExist(String name){
        FileConfiguration config = getStructureConfig();
        return (config.contains("structures.blueprints." + name));
    }
}
