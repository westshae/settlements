package cc.altoya.settlements.Domain;

import java.util.HashMap;

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
        HashMap<String, String> commands = new HashMap<>();
        commands.put("/domain claim", "Claims the chunk you're currently in.");
        commands.put("/domain unclaim", "Unclaims the chunk you're currently in.");
        commands.put("/domain list", "Lists all your claimed chunks.");
        commands.put("/domain help", "The command you're looking at right now.");

        ChatUtil.sendCommandHelpMessage(player, "/domain", commands);
    }
}
