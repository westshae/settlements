package cc.altoya.settlements.Blueprint;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;

import cc.altoya.settlements.Util.GeneralUtil;

public class BlueprintUtil {
    public static FileConfiguration getBlueprintConfig() {
        return GeneralUtil.getPluginConfig("settlements", "blueprints.yml");
    }

    public static void saveBlueprintConfig(FileConfiguration config) {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(),
                "blueprints.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getRelativeLocation(Block origin, Block block) {
        int relativeX = block.getX() - origin.getX();
        int relativeY = block.getY() - origin.getY();
        int relativeZ = block.getZ() - origin.getZ();
    
        return new Location(origin.getWorld(), relativeX, relativeY, relativeZ);
    }

    public static Location getNonRelativeLocation(Block origin, Location relativeLocation){
        return origin.getRelative(relativeLocation.getBlockX(), relativeLocation.getBlockY(), relativeLocation.getBlockZ()).getLocation();
    }

    public static boolean doesBlueprintExist(String name) {
        FileConfiguration config = getBlueprintConfig();
        return (config.contains("blueprints." + name));
    }

    public static String turnBlockIntoString(Block block, Location location) {
        if (block == null || location == null) {
            return null; // Return null if block is null
        }

        Material material = block.getType();
        BlockData blockData = block.getBlockData();

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

    public static String getBuildingType(String blueprintName) {
        FileConfiguration config = getBlueprintConfig();
        if (!doesBlueprintExist(blueprintName)) {
            return null;
        }
        return config.getString("blueprints." + blueprintName + ".structureType");
    }

    public static String getResourceType(String blueprintName) {
        FileConfiguration config = getBlueprintConfig();
        if (!doesBlueprintExist(blueprintName)) {
            return null;
        }
        return config.getString("blueprints." + blueprintName + ".resourceType");
    }

    public static Integer getVersion(String blueprintName) {
        FileConfiguration config = getBlueprintConfig();
        if (!doesBlueprintExist(blueprintName)) {
            return null;
        }
        return config.getInt("blueprints." + blueprintName + ".version");
    }


    public static void placeBlockWithoutBlueprintData(Location location, Material material, BlockData blockData) {
        Block block = location.getBlock();
        block.setType(material, false);
        block.setBlockData(blockData, false);
    }
}
