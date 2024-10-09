package cc.altoya.settlements.Blueprint;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Build.BuildUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.ItemUtil;

public class CommandSave {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2, "/blueprint save")) {
            return true;
        }

        saveCurrentStructure(sender, args[1]);
        return true;
    }

    private static void saveCurrentStructure(Player player, String name) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        if (!BlueprintUtil.doesBlueprintExist(name)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist, use create first.");
            return;
        }

        String firstPointKey = config.getString("blueprints." + name + ".first");
        String secondPointKey = config.getString("blueprints." + name + ".second");

        Block firstBlock = BlueprintUtil.turnStringIntoBlock(firstPointKey);
        Block secondBlock = BlueprintUtil.turnStringIntoBlock(secondPointKey);

        int x1 = firstBlock.getX();
        int y1 = firstBlock.getY();
        int z1 = firstBlock.getZ();

        int x2 = secondBlock.getX();
        int y2 = secondBlock.getY();
        int z2 = secondBlock.getZ();

        List<Material> resourceBlockList = ItemUtil.getAllResourceBlocks(firstBlock.getChunk());

        List<String> resourceBlocksInChunk = new ArrayList<String>();

        List<String> blockList = new ArrayList<>();

        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) {
                    Block block = firstBlock.getWorld().getBlockAt(x, y, z);
                    if(block.getType() == Material.AIR){
                        continue;
                    }
                    Location relativeLocation = BlueprintUtil.getRelativeLocation(firstBlock, block);
                    String blockString = BlueprintUtil.turnBlockIntoString(block, relativeLocation);
                    if(resourceBlockList.contains(block.getType())){
                        resourceBlocksInChunk.add(blockString);
                    }
                    blockList.add(blockString);
                }
            }
        }
        config.set("blueprints." + name + ".blocks", blockList);
        config.set("blueprints." + name + ".resourceBlocks", resourceBlocksInChunk);
        BlueprintUtil.saveBlueprintConfig(config);

        ChatUtil.sendSuccessMessage(player, "Successfully saved structure with " + blockList.size() + " blocks.");
    }

}
