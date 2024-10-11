package cc.altoya.settlements.Domain;

import java.util.Map;

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

    private static void help(Player player){
        Map<String, String> commands = Map.of(
            "/domain claim", "Claims the chunk you're currently in.",
            "/domain unclaim", "Unclaims the chunk you're currently in.",
            "/domain list", "Lists all your claimed chunks.",
            "/domain help", "The command you're looking at right now."
        );

        ChatUtil.sendCommandHelpMessage(player, "/domain", commands);
    }
}
