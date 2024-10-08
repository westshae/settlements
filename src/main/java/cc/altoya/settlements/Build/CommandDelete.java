package cc.altoya.settlements.Build;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Domain.DomainUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandDelete {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/build delete")) {
            return true;
        }

        deleteStructure(sender, sender.getLocation().getChunk());
        return true;
    }

    private static void deleteStructure(Player player, Chunk chunk) {
        if (!DomainUtil.doesPlayerOwnChunk(player, chunk)) {
            ChatUtil.sendErrorMessage(player, "You don't own this structure");
            return;
        }

        FileConfiguration config = BuildUtil.getBuildConfig();

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
                        Block block = BlueprintUtil.turnStringIntoBlock(blockKey);

                        if(block.getType() == Material.AIR){
                            blocksProcessed++;
                            index++;
                            continue;
                        }

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
                    BuildUtil.saveBuildConfig(config); // Save the updated config
                    this.cancel(); // Stop the runnable if all blocks have been processed
                }
            }
        }.runTaskTimer(GeneralUtil.getPlugin(), 0, 1); // Start immediately and run every tick

        ChatUtil.sendSuccessMessage(player, "Structure successfully deleted.");
    }

}
