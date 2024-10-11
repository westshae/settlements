package cc.altoya.settlements.Alliance;

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
            "/alliance create {allianceName}", "The first command to create an alliance.",
            "/alliance delete", "The command to delete your alliance.",
            "/alliance info", "Prints out information about the alliance. Eg players, name, wealth.",
            "/alliance invite {username}", "Extends an invitation to the player specified.",
            "/alliance join {allianceName}", "Allows you to join an alliance if you've been invited.",
            "/alliance leave", "Allows you to leave your alliance.",
            "/alliance help", "The command you're looking at right now."
        );

        ChatUtil.sendCommandHelpMessage(player, "/alliance", commands);
    }
}
