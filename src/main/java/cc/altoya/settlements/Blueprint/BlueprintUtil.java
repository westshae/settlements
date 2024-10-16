package cc.altoya.settlements.Blueprint;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Build.BuildUtil;
import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Util.ChatUtil;
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

    public static void sendPlayerBlueprintList(Player player) {
        FileConfiguration config = getBlueprintConfig();

        ChatUtil.sendSuccessMessage(player, "Available Blueprints: ");
        List<String> blueprintNames = List.copyOf(config.getKeys(false));
        for (String blueprintName : blueprintNames) {
            ChatUtil.sendSuccessMessage(player, blueprintName);
        }
    }

    public static Location getRelativeLocation(Block origin, Block block) {
        int relativeX = block.getX() - origin.getX();
        int relativeY = block.getY() - origin.getY();
        int relativeZ = block.getZ() - origin.getZ();

        return new Location(origin.getWorld(), relativeX, relativeY, relativeZ);
    }

    public static Location getNonRelativeLocation(Block origin, Location relativeLocation) {
        return origin
                .getRelative(relativeLocation.getBlockX(), relativeLocation.getBlockY(), relativeLocation.getBlockZ())
                .getLocation();
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

    public static Integer getVersion(String blueprintName) {
        FileConfiguration config = getBlueprintConfig();
        if (!doesBlueprintExist(blueprintName)) {
            return null;
        }
        return config.getInt("blueprints." + blueprintName + ".version");
    }

    public static Integer getHousing(String blueprintName) {
        FileConfiguration config = getBlueprintConfig();
        if (!doesBlueprintExist(blueprintName)) {
            return null;
        }
        return config.getInt("blueprints." + blueprintName + ".housing");
    }

    public static void placeBlockWithoutBlueprintData(Location location, Material material, BlockData blockData) {
        Block block = location.getBlock();
        block.setType(material, false);
        block.setBlockData(blockData, false);
    }

    public static Block getRelativeSecondBlock(Block firstBlock, String blueprintName) {
        FileConfiguration config = getBlueprintConfig();
        if (!doesBlueprintExist(blueprintName)) {
            return null;
        }
        String firstBlockKey = config.getString("blueprints." + blueprintName + ".first");
        String secondBlockKey = config.getString("blueprints." + blueprintName + ".second");
        Block blueprintFirstBlock = BlueprintUtil.turnStringIntoBlock(firstBlockKey);
        Block blueprintSecondBlock = BlueprintUtil.turnStringIntoBlock(secondBlockKey);

        int offsetX = blueprintSecondBlock.getX() - blueprintFirstBlock.getX();
        int offsetY = blueprintSecondBlock.getY() - blueprintFirstBlock.getY();
        int offsetZ = blueprintSecondBlock.getZ() - blueprintFirstBlock.getZ();

        // Create a new location for the second block based on the new base location
        Location newSecondBlockLocation = new Location(
                firstBlock.getWorld(),
                firstBlock.getX() + offsetX,
                firstBlock.getY() + offsetY,
                firstBlock.getZ() + offsetZ);
        return newSecondBlockLocation.getBlock();
    }

    public static String getUpgradedBlueprintName(String originalBlueStringName) {
        Integer currentVersion = BlueprintUtil.getVersion(originalBlueStringName);

        int lastIndexOfV = originalBlueStringName.lastIndexOf('v');

        // If 'V' is found, get the substring up to and including the last 'V'
        if (lastIndexOfV != -1) {

            originalBlueStringName = originalBlueStringName.substring(0, lastIndexOfV + 1);
        }

        return originalBlueStringName + "v" + Integer.toString(currentVersion + 1);
    }

    public static void setBlueprintCost(String blueprintName, Material itemMaterial, int amount) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        config.set("blueprints." + blueprintName + ".cost." + itemMaterial, amount);
        BlueprintUtil.saveBlueprintConfig(config);
    }

    public static boolean canAffordBlueprint(Player player, String blueprintName) {
        ConfigurationSection blueprintCosts = BlueprintUtil.getBlueprintCosts(blueprintName);

        if (blueprintCosts == null) {
            return true;
        }

        for (String itemMaterialString : blueprintCosts.getKeys(false)) {
            Material material = Material.matchMaterial(itemMaterialString);
            if (material == null) {
                continue;
            }

            int requiredAmount = blueprintCosts.getInt(itemMaterialString);

            if (!CityUtil.hasResourcesAvailable(player, material, requiredAmount)) {
                return false;
            }
        }

        // If all resources are available, the player can afford the blueprint
        return true;
    }

    public static void create(String blueprintName, Material material, int housingCount) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        config.set("blueprints." + blueprintName + ".version", 1);
        config.set("blueprints." + blueprintName + ".housing", housingCount);
        config.set("blueprints." + blueprintName + ".material", material.toString());

        BlueprintUtil.saveBlueprintConfig(config);
    }

    public static void delete(String blueprintName) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        config.set("blueprints." + blueprintName, null);

        BlueprintUtil.saveBlueprintConfig(config);
    }

    public static void setFirstBlock(Player player, String blueprintName) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();
        Block targettedBlock = player.getTargetBlock(null, 10);

        Block chunkZero = targettedBlock.getChunk().getBlock(0, targettedBlock.getY(), 0);
        if (!chunkZero.equals(targettedBlock)) {
            ChatUtil.sendErrorMessage(player, "The first block of a blueprint must be placed at a chunk's [0, ~, 0]");
            return;
        }
        config.set("blueprints." + blueprintName + ".first",
                BlueprintUtil.turnBlockIntoString(targettedBlock, targettedBlock.getLocation()));
        BlueprintUtil.saveBlueprintConfig(config);
    }

    public static void setSecondBlock(Player player, String blueprintName) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        Block targettedBlock = player.getTargetBlock(null, 10);
        Block firstPointBlock = BlueprintUtil
                .turnStringIntoBlock(config.getString("blueprints." + blueprintName + ".first"));

        Block chunksBlock = firstPointBlock.getChunk().getBlock(15, targettedBlock.getY(), 15);

        Location relativeSecondLocation = BlueprintUtil.getRelativeLocation(firstPointBlock, targettedBlock);
        BlueprintUtil.turnBlockIntoString(targettedBlock, relativeSecondLocation);

        if (!targettedBlock.equals(chunksBlock)) {
            ChatUtil.sendErrorMessage(player,
                    "The second point of a blueprint must be placed at a chunk's [15, ~, 15]");
            return;
        }
        config.set("blueprints." + blueprintName + ".second",
                BlueprintUtil.turnBlockIntoString(targettedBlock, targettedBlock.getLocation()));
        BlueprintUtil.saveBlueprintConfig(config);
    }

    public static Block getFirstBlock(String blueprintName) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        String firstPointKey = config.getString("blueprints." + blueprintName + ".first");
        return BlueprintUtil.turnStringIntoBlock(firstPointKey);
    }

    public static Block getSecondBlock(String blueprintName) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        String secondPointKey = config.getString("blueprints." + blueprintName + ".second");
        return BlueprintUtil.turnStringIntoBlock(secondPointKey);
    }

    public static void save(String blueprintName) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        Block firstBlock = getFirstBlock(blueprintName);
        Block secondBlock = getSecondBlock(blueprintName);

        int x1 = firstBlock.getX();
        int y1 = firstBlock.getY();
        int z1 = firstBlock.getZ();

        int x2 = secondBlock.getX();
        int y2 = secondBlock.getY();
        int z2 = secondBlock.getZ();

        // List<Material> resourceBlockList =
        // ItemUtil.getAllResourceBlocks(firstBlock.getChunk());

        // TODO IMPLEMENT A SYSTEM WHERE RESOURCE BLOCKS ARE BASED ON THE STRUCTURE
        // TYPE.

        List<String> resourceBlocksInChunk = new ArrayList<String>();

        List<String> blockList = new ArrayList<>();

        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) {
                    Block block = firstBlock.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == Material.AIR) {
                        continue;
                    }
                    Location relativeLocation = BlueprintUtil.getRelativeLocation(firstBlock, block);
                    String blockString = BlueprintUtil.turnBlockIntoString(block, relativeLocation);
                    // if (resourceBlockList.contains(block.getType())) {
                    // resourceBlocksInChunk.add(blockString);
                    // }
                    blockList.add(blockString);
                }
            }
        }
        config.set("blueprints." + blueprintName + ".blocks", blockList);
        config.set("blueprints." + blueprintName + ".resourceBlocks", resourceBlocksInChunk);
        BlueprintUtil.saveBlueprintConfig(config);
    }

    public static void teleportPlayerToBlueprint(Player player, String blueprintName) {
        Block firstBlock = getFirstBlock(blueprintName);

        Location teleportLocation = firstBlock.getLocation().clone().add(0, 2, 0);
        player.teleport(teleportLocation);
    }

    public static void createUpgradeBlueprint(Player player, String blueprintName, int version) {
        String currentBlueprintName = blueprintName + "v" + version;

        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        config.set("blueprints." + currentBlueprintName + ".version", version);

        BlueprintUtil.saveBlueprintConfig(config);

        BuildUtil.generateBuildingFromBlueprint(player, currentBlueprintName);
    }

    public static ConfigurationSection getBlueprintCosts(String blueprintName) {
        FileConfiguration blueprintConfig = getBlueprintConfig();
        return blueprintConfig.getConfigurationSection("blueprints." + blueprintName + ".cost");
    }

    public static String getBlueprintMaterial(String blueprintName) {
        FileConfiguration blueprintConfig = getBlueprintConfig();
        return blueprintConfig.getString("blueprints." + blueprintName + ".material");
    }

    public static HashMap<String, String> getBlueprintCommands() {
        HashMap<String, String> commands = new HashMap<>();
        commands.put("/blueprint create {blueprintName} {housingCount}", "The first command to create a blueprint.");
        commands.put("/blueprint first {blueprintName}",
                "Sets the blueprint's chunk [0, ~, 0] point based on the block you're looking at.");
        commands.put("/blueprint second {blueprintName}",
                "Sets the blueprint's chunk [15, ~, 15] point based on the block you're looking at.");
        commands.put("/blueprint save {blueprintName}",
                "Gets all blocks between the first/second point, then converts each block to a string form for future generation.");
        commands.put("/blueprint upgrade {baseBlueprintName} {version}",
                "Creates a new blueprint named {original}v{version}, and creates a dupe of the previous blueprint.");
        commands.put("/blueprint delete {blueprintName}", "Deletes the blueprint provided.");
        commands.put("/blueprint list", "Sends you a list of all available blueprints.");
        commands.put("/blueprint cost {blueprintName} {amount}",
                "Sets the blueprint's resource cost for the item you are holding.");
        commands.put("/blueprint help", "The command you're looking at right now.");
        commands.put("/blueprint teleport {blueprintName}", "Teleports you to the blueprint's firstblock.");
        return commands;
    }
}
