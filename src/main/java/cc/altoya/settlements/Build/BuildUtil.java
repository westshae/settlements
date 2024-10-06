package cc.altoya.settlements.Build;

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
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class BuildUtil {
    public static FileConfiguration getBuildConfig() {
        return GeneralUtil.getPluginConfig("settlements", "builds.yml");
    }

    public static void saveBuildConfig(FileConfiguration config) {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(),
                "builds.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewStructure(Player player, Chunk chunk, String type, String blueprintName) {
        FileConfiguration config = getBuildConfig();

        config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".owner", GeneralUtil.getKeyFromPlayer(player));
        config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".type", type);
        config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".blueprintName", blueprintName);
        saveBuildConfig(config);
    }

    public static String getStructureType(Chunk chunk) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }
        return config.getString("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".type");
    }

    public static String getStructureBlueprintName(Chunk chunk) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }
        return config.getString("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".blueprintName");
    }

    public static void saveBlockAsStructureBlock(Block block, boolean isInteractive) {
        FileConfiguration config = getBuildConfig();
        config.set("builds.all_blocks." + GeneralUtil.getKeyFromBlock(block) + ".interactive", isInteractive);

        String blockPath = (isInteractive) ? ".interactiveBlocks" : ".blocks";

        List<String> structureBlocks = config
                .getStringList("builds." + GeneralUtil.getKeyFromChunk(block.getChunk()) + blockPath);
        structureBlocks.add(GeneralUtil.getKeyFromBlock(block));

        config.set("builds." + GeneralUtil.getKeyFromChunk(block.getChunk()) + blockPath,
                structureBlocks);
        saveBuildConfig(config);
    }

    public static boolean isBlockStructureBlock(Block block) {
        FileConfiguration config = getBuildConfig();
        return config.contains("builds.all_blocks." + GeneralUtil.getKeyFromBlock(block));
    }

    public static boolean isBlockInteractiveBlock(Block block) {
        FileConfiguration config = getBuildConfig();
        return config.getBoolean("builds.all_blocks." + GeneralUtil.getKeyFromBlock(block) + ".interactive");
    }

    public static void placeStructureBlock(Player player, Location location, Material material, BlockData blockData) {
        Block currentBlockAtLocation = location.getBlock();
        currentBlockAtLocation.setType(material, false);
        currentBlockAtLocation.setBlockData(blockData, false);
        saveBlockAsStructureBlock(currentBlockAtLocation, false);
    }

    public static void placeInteractiveBlock(Player player, Location location, Material material, BlockData blockData) {
        Block currentBlockAtLocation = location.getBlock();
        currentBlockAtLocation.setType(material, false);
        currentBlockAtLocation.setBlockData(blockData, false);
        saveBlockAsStructureBlock(currentBlockAtLocation, true);
    }

    public static boolean isChunkStructure(Chunk chunk) {
        FileConfiguration config = getBuildConfig();
        return config.contains("builds." + GeneralUtil.getKeyFromChunk(chunk));
    }

    public static Integer getResourcesFromStructure(Chunk chunk) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }
        return config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".resources");
    }

    public static void editResources(Player player, Chunk chunk, int amount) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return;
        }
        config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".resources",
                getResourcesFromStructure(chunk) + amount);
        saveBuildConfig(config);
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

    public static void setStructurePlayerheight(Chunk chunk, Player player, int playerHeight) {
        FileConfiguration buildConfig = getBuildConfig();

        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".playerHeight", playerHeight);

        saveBuildConfig(buildConfig);
    }

    public static Integer getStructurePlayerheight(Chunk chunk, Player player) {
        FileConfiguration buildConfig = getBuildConfig();
        if (!buildConfig.contains("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".playerHeight")) {
            return null;
        } else {
            return buildConfig.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".playerHeight");
        }
    }

    public static Integer getSuppliesFromStructure(Chunk chunk) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }
        return config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".supplies");
    }


    public static void editSupplies(Player player, Chunk chunk, int amount) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return;
        }
        config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".supplies",
        getSuppliesFromStructure(chunk) + amount);
        saveBuildConfig(config);
        ChatUtil.sendSuccessMessage(player, "Supplies are now at " + getSuppliesFromStructure(chunk));
    }


    public static void deleteBlocksFromBlueprint(Chunk chunk, Player player) {
        FileConfiguration config = getBuildConfig();

        List<String> blocks = new ArrayList<>();

        blocks.addAll(config.getStringList("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".interactiveBlocks"));
        blocks.addAll(config.getStringList("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".blocks"));

        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                int blocksProcessed = 0;
                int blocksToProcess = 1;
                while (index < blocks.size() && blocksProcessed < blocksToProcess) {
                    String blockKey = blocks.get(index);
                    String allBlockPath = "builds.all_blocks." + blockKey;

                    if (config.contains(allBlockPath)) {
                        Block block = GeneralUtil.getBlockFromKey(blockKey);
                        if (block != null) {
                            block.setType(Material.AIR); // Remove the block
                            config.set(allBlockPath, null); // Remove from config
                            blocksProcessed++;
                        }
                    }
                    index++;
                }

                // If there are more blocks to process, schedule the next run after a brief
                // pause
                if (index < blocks.size()) {
                    // Pause for 1 tick (50 milliseconds) to allow server to process other tasks
                    this.runTaskLater(GeneralUtil.getPlugin(), 1); // Replace MyPlugin with your plugin instance class
                } else {
                    // Clean up the structure entry from config
                    config.set("structures." + GeneralUtil.getKeyFromChunk(chunk), null);
                    saveBuildConfig(config); // Save the updated config
                    this.cancel(); // Stop the runnable if all blocks have been processed
                }
            }
        }.runTaskTimer(GeneralUtil.getPlugin(), 0, 1); // Start immediately and run every tick

    }

    public static void placeBlocksFromBlueprint(Chunk chunk, Player player, String blueprintName) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        List<String> normalBlocks = config.getStringList("blueprints." + blueprintName + ".blocks");

        // Retrieve the list of interactive blocks from the configuration
        List<String> interactiveBlocks = config.getStringList("blueprints." + blueprintName + ".interactive");

        int originalY = config.getInt("blueprints." + blueprintName + ".originalY");
        int potentialPlayerHeight = player.getLocation().getBlockY();

        // Retrieve the first block's position as the origin for the structure
        String firstBlockKey = config.getString("blueprints." + blueprintName + ".first");
        Block firstBlock = GeneralUtil.getBlockFromKey(firstBlockKey);

        if (firstBlock == null) {
            ChatUtil.sendErrorMessage(player, "Error retrieving the first block from key: " + firstBlockKey);
            return;
        }

        // Get the original coordinates of the first block
        int originX = firstBlock.getX();
        int originZ = firstBlock.getZ();

        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                int blocksProcessed = 0;

                while (index < normalBlocks.size() && blocksProcessed < 1) {
                    String blockString = normalBlocks.get(index);
                    Block block = BlueprintUtil.turnStringIntoBlock(blockString);

                    Integer playerHeight = getStructurePlayerheight(chunk, player);
                    if(playerHeight == null){
                        playerHeight = potentialPlayerHeight;
                        BuildUtil.setStructurePlayerheight(chunk, player, playerHeight);
                    }
            
                    if (block != null) {
                        int relativeY = block.getY() - originalY + playerHeight;
                        int x = block.getX();
                        int z = block.getZ();

                        int adjustedX = (chunk.getX() * 16 + x) - originX;
                        int adjustedZ = (chunk.getZ() * 16 + z) - originZ;

                        Location blockLocation = new Location(chunk.getWorld(), adjustedX, relativeY, adjustedZ);

                        if (interactiveBlocks.contains(blockString)) {
                            BuildUtil.placeInteractiveBlock(player, blockLocation, block.getType(),
                                    block.getBlockData());
                        } else {
                            BuildUtil.placeStructureBlock(player, blockLocation, block.getType(), block.getBlockData());
                        }

                        blocksProcessed++;
                    } else {
                        ChatUtil.sendErrorMessage(player, "Error converting block from string: " + blockString);
                    }
                    index++;
                }

                // If there are more blocks to process, schedule the next run after a brief
                // pause
                if (index < normalBlocks.size()) {
                    // Pause for 1 tick (50 milliseconds) to allow server to process other tasks
                    this.runTaskLater(GeneralUtil.getPlugin(), 1); // Replace MyPlugin with your plugin instance class
                } else {
                    this.cancel(); // Stop the runnable if all blocks have been processed
                }
            }
        }.runTaskTimer(GeneralUtil.getPlugin(), 0, 1); // Start immediately and run every tick

    }
}
