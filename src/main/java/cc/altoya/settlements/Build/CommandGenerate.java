package cc.altoya.settlements.Build;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandGenerate {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2,
                "/build generate {structureType}")) {
            return true;
        }
        generateBuildingFromBlueprint(sender, args[1]);
        return true;
    }

    public static void generateBuildingFromBlueprint(Player player, String blueprintName) {
        Chunk chunk = player.getLocation().getChunk();
        Integer version = BlueprintUtil.getVersion(blueprintName);

        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }

        FileConfiguration buildConfig = BuildUtil.getBuildConfig();

        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".owner", GeneralUtil.getKeyFromPlayer(player));
        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".blueprintName", blueprintName);
        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".version", version);

        BuildUtil.saveBuildConfig(buildConfig);

        FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();

        List<String> normalBlocks = blueprintConfig.getStringList("blueprints." + blueprintName + ".blocks");

        // Retrieve the list of interactive blocks from the configuration
        List<String> interactiveBlocks = blueprintConfig.getStringList("blueprints." + blueprintName + ".interactive");

        int originalY = blueprintConfig.getInt("blueprints." + blueprintName + ".originalY");
        int potentialPlayerHeight = player.getLocation().getBlockY();

        // Retrieve the first block's position as the origin for the structure
        String firstBlockKey = blueprintConfig.getString("blueprints." + blueprintName + ".first");
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

                    Integer playerHeight = BuildUtil.getStructurePlayerheight(chunk, player);
                    if (playerHeight == null) {
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
                        boolean isInteractive = interactiveBlocks.contains(blockString);

                        boolean existingBlockIsAir = chunk.getWorld().getBlockAt(adjustedX, relativeY, adjustedZ).getType() == Material.AIR;
                        boolean newBlockIsAir = block.getType() == Material.AIR;

                        if(existingBlockIsAir && newBlockIsAir){
                            blocksProcessed++;
                            index++;
                            continue;
                        }

                        BuildUtil.placeBlockForStructure(player, blockLocation, block.getType(), block.getBlockData(), isInteractive);

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

        ChatUtil.sendSuccessMessage(player, "Successfully generated structure from blueprint: " + blueprintName);
    }
}
