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
            "/build new {blueprintName}", "Generates a structure in the chunk you are in.",
            "/build refresh", "Deletes then builds the structure of the chunk you are in.",
            "/build collect", "Collects resources from the structure of the chunk you are in.",
            "/build plot", "Shows you where the bottom level of the structure will begin to generate to allow you to terraform.",
            "/build upgrade", "Upgrades your structure to the next level.",
            "/build workers {amount}", "Set workers of structure in your chunk.",
            "/build giveall {amount}", "Gives the executer x amount of all custom resources.",
            "/build transform", "Transforms the item in your hand to its custom version, if it exists.",
            "/build help", "The command you're looking at right now"
        );

        commands.put("/build collectall", "/Build collect, for every single structure you own.");
        commands.put("/build supply", "Take the item in your hand, and supplies it to the structure of the chunk you're in.");

        ChatUtil.sendCommandHelpMessage(player, "/build", commands);
    }
}
