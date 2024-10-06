package cc.altoya.settlements.Build;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
    FileConfiguration config = BuildUtil.getStructureConfig();
    
    if (!DomainUtil.doesPlayerOwnChunk(player, chunk)) {
        ChatUtil.sendErrorMessage(player, "You don't own this structure");
        return;
    }

    List<String> allBlocks = new ArrayList<>();

    allBlocks.addAll(config.getStringList("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".interactiveBlocks"));
    allBlocks.addAll(config.getStringList("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".blocks"));

    new BukkitRunnable() {
        int index = 0;

        @Override
        public void run() {
            int blocksProcessed = 0;
            int blocksToProcess = 1;
            while (index < allBlocks.size() && blocksProcessed < blocksToProcess) {
                String blockKey = allBlocks.get(index);
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

            // If there are more blocks to process, schedule the next run after a brief pause
            if (index < allBlocks.size()) {
                // Pause for 1 tick (50 milliseconds) to allow server to process other tasks
                this.runTaskLater(GeneralUtil.getPlugin(), 1); // Replace MyPlugin with your plugin instance class
            } else {
                // Clean up the structure entry from config
                config.set("structures." + GeneralUtil.getKeyFromChunk(chunk), null);
                BuildUtil.saveStructureConfig(config); // Save the updated config
                ChatUtil.sendSuccessMessage(player, "Structure successfully deleted.");
                this.cancel(); // Stop the runnable if all blocks have been processed
            }
        }
    }.runTaskTimer(GeneralUtil.getPlugin(), 0, 1); // Start immediately and run every tick
}

}
