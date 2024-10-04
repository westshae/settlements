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
import org.bukkit.block.data.BlockData;
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

    public static void createNewStructure(Player player, Chunk chunk, String type) {
        FileConfiguration config = getStructureConfig();

        config.set("structures." + GeneralUtil.getKeyFromChunk(chunk) + ".owner", GeneralUtil.getKeyFromPlayer(player));
        config.set("structures." + GeneralUtil.getKeyFromChunk(chunk) + ".type", type);
        saveStructureConfig(config);
    }

    public static String getStructureType(Chunk chunk) {
        FileConfiguration config = getStructureConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }
        return config.getString("structures." + GeneralUtil.getKeyFromChunk(chunk) + ".type");
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

    public static boolean doesStructureNameExist(String name) {
        FileConfiguration config = getStructureConfig();
        return (config.contains("structures.blueprints." + name));
    }

    public static String turnBlockIntoString(Block block) {
        if (block == null) {
            return null; // Return null if block is null
        }

        Material material = block.getType();
        BlockData blockData = block.getBlockData();
        Location location = block.getLocation();

        // Format: material|blockData|worldName;x;y;z
        return String.format("%s|%s|%s;%d;%d;%d",
                material.toString(),
                blockData.getAsString(),
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ());
    }

    // Converts a String representation back into a Block
    public static Block turnStringIntoBlock(String data) {
        if (data == null || !data.contains("|")) {
            return null; // Return null if data is invalid
        }

        String[] parts = data.split("\\|");
        if (parts.length != 3) {
            return null; // Ensure the correct format is maintained
        }

        String materialString = parts[0];
        String blockDataString = parts[1];
        String[] locationParts = parts[2].split(";");

        // Validate location parts
        if (locationParts.length != 4) {
            return null; // Return null if location is invalid
        }

        // Retrieve the world and coordinates
        String worldName = locationParts[0];
        int x = Integer.parseInt(locationParts[1]);
        int y = Integer.parseInt(locationParts[2]);
        int z = Integer.parseInt(locationParts[3]);

        // Get the world from the server
        org.bukkit.World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null; // Return null if world is not found
        }

        // Create the Block at the specified location
        Block block = world.getBlockAt(x, y, z);
        block.setType(Material.valueOf(materialString)); // Set the block type

        // Create BlockData from the string
        BlockData blockData = Bukkit.createBlockData(blockDataString);
        block.setBlockData(blockData);

        return block;
    }

}
