package cc.altoya.settlements.Build;

import java.util.Map;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandHelp {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/build help")) {
            return true;
        }

        help(sender);
        return true;
    }

    private static void help(Player player){
        Map<String, String> commands = Map.of(
            "/build delete", "Deletes any structure within the chunk you are in.",
            "/build generate {blueprintName}", "Generates a structure in the chunk you are in.",
            "/build refresh", "Deletes then builds the structure of the chunk you are in.",
            "/build help", "The command you're looking at right now"
        );

        ChatUtil.sendCommandHelpMessage(player, "/build", commands);
    }
}
