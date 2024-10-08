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

public class CommandDelete {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/build delete")) {
            return true;
        }

        deleteStructure(sender);
        return true;
    }

    public static void deleteStructure(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        String blueprintName = BuildUtil.getStructureBlueprintName(chunk);
        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }

        FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();
        FileConfiguration buildConfig = BuildUtil.getBuildConfig();

        String buildFirstKey = buildConfig.getString("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".first");
        Block buildFirstBlock = BlueprintUtil.turnStringIntoBlock(buildFirstKey);

        List<String> blocks = blueprintConfig.getStringList("blueprints." + blueprintName + ".blocks");

        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                int blocksProcessed = 0;

                while (index < blocks.size() && blocksProcessed < 1) {
                    String blockString = blocks.get(index);
                    Block block = BlueprintUtil.turnStringIntoBlock(blockString);

                    if (block != null) {
                        Location relativeLocation = block.getLocation(); 
                        Location nonRelativeLocation = BlueprintUtil.getNonRelativeLocation(buildFirstBlock, relativeLocation);

                        nonRelativeLocation.getBlock().setType(Material.AIR);

                        blocksProcessed++;
                    } else {
                        ChatUtil.sendErrorMessage(player, "Error converting block from string: " + blockString);
                    }
                    index++;
                }

                if (index < blocks.size()) {
                    this.runTaskLater(GeneralUtil.getPlugin(), 1);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(GeneralUtil.getPlugin(), 0, 1);

        ChatUtil.sendSuccessMessage(player, "Successfully generated structure from blueprint: " + blueprintName);
    }

}
