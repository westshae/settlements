package cc.altoya.settlements.Blueprint;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Build.BuildUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandDupe {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2, "/blueprint dupe")) {
            return true;
        }

        duplicateBlueprintBlocksOnly(sender, args[1]);
        return true;
    }

    public static void duplicateBlueprintBlocksOnly(Player player, String blueprintName) {
        Chunk chunk = player.getLocation().getChunk();

        if (BuildUtil.isChunkStructure(chunk)) {
            ChatUtil.sendErrorMessage(player, "There is already a structure in this chunk.");
            return;
        }

        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }

        FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();

        List<String> normalBlocks = blueprintConfig.getStringList("blueprints." + blueprintName + ".blocks");

        int playerHeight = player.getLocation().getBlockY();

        // Retrieve the first block's position as the origin for the structure
        String firstBlockKey = blueprintConfig.getString("blueprints." + blueprintName + ".first");
        Block firstBlock = GeneralUtil.getBlockFromKey(firstBlockKey);

        if (firstBlock == null) {
            ChatUtil.sendErrorMessage(player, "Error retrieving the first block from key: " + firstBlockKey);
            return;
        }

        // Get the original coordinates of the first block
        int originX = firstBlock.getX();
        int originY = firstBlock.getY();
        int originZ = firstBlock.getZ();

        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                int blocksProcessed = 0;

                while (index < normalBlocks.size() && blocksProcessed < 1) {
                    String blockString = normalBlocks.get(index);
                    Block block = BlueprintUtil.turnStringIntoBlock(blockString);

                    if (block != null) {
                        int relativeY = block.getY() - originY + playerHeight;
                        int x = block.getX();
                        int z = block.getZ();

                        int adjustedX = (chunk.getX() * 16 + x) - originX;
                        int adjustedZ = (chunk.getZ() * 16 + z) - originZ;

                        Location blockLocation = new Location(chunk.getWorld(), adjustedX, relativeY, adjustedZ);

                        BlueprintUtil.placeBlockWithoutBlueprintData(blockLocation, block.getType(),
                                block.getBlockData());

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

        ChatUtil.sendSuccessMessage(player, "Successfully duplicated build from blueprint: " + blueprintName);
    }

}
