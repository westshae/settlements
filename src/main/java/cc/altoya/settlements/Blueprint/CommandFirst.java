package cc.altoya.settlements.Blueprint;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandFirst {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
            return true;
        }

        setFirst(sender, args[1]);
        return true;
    }

        private static void setFirst(Player player, String name) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();
        if (!BlueprintUtil.doesBlueprintExist(name)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist, use create first.");
            return;
        }
        Block targettedBlock = player.getTargetBlock(null, 10);
        if(!(targettedBlock.getX() % 16 == 0 && targettedBlock.getZ() % 16 == 0)){
            ChatUtil.sendErrorMessage(player, "The first block of a blueprint must be placed at a chunk's [0, ~, 0]");
            return;
        }
        config.set("blueprints." + name + ".first", BlueprintUtil.turnBlockIntoString(targettedBlock, targettedBlock.getLocation()));
        ChatUtil.sendSuccessMessage(player, "Successfully added first block to blueprint.");
        BlueprintUtil.saveBlueprintConfig(config);
    }
}
