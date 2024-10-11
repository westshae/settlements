package cc.altoya.settlements.Blueprint;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandHelp {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }

        help(sender);
        return true;
    }

    private static void help(Player player) {
        ChatUtil.sendCommandHelpMessage(player, "/blueprint", BlueprintUtil.getBlueprintCommands());
    }
}
