package cc.altoya.settlements.Blueprint;

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
        if (!BlueprintUtil.doesBlueprintExist(name)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist, use create first.");
            return;
        }
        BlueprintUtil.setFirstBlock(player, name);
        
        ChatUtil.sendSuccessMessage(player, "Successfully added first block to blueprint.");
    }
}
