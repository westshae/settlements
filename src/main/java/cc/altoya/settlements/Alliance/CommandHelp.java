package cc.altoya.settlements.Alliance;

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
        commands.put("/alliance create {allianceName}", "The first command to create an alliance.");
        commands.put("/alliance delete", "The command to delete your alliance.");
        commands.put("/alliance info", "Prints out information about the alliance. Eg players, name, wealth.");
        commands.put("/alliance invite {username}", "Extends an invitation to the player specified.");
        commands.put("/alliance join {allianceName}", "Allows you to join an alliance if you've been invited.");
        commands.put("/alliance leave", "Allows you to leave your alliance.");
        commands.put("/alliance help", "The command you're looking at right now.");

        ChatUtil.sendCommandHelpMessage(player, "/alliance", commands);
    }
}
