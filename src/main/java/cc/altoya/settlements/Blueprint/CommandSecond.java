package cc.altoya.settlements.Blueprint;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandSecond {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
            return true;
        }

        setSecond(sender, args[1]);
        return true;
    }

    private static void setSecond(Player player, String name) {
        if (!BlueprintUtil.doesBlueprintExist(name)) {
            ChatUtil.sendErrorMessage(player, "This structure doesn't exist, use create first.");
            return;
        }
        BlueprintUtil.setSecondBlock(player, name);
        ChatUtil.sendSuccessMessage(player, "Successfully added second point to blueprint.");
    }

}
