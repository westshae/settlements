package cc.altoya.settlements.Build;

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
        commands.put("/build delete", "Deletes any structure within the chunk you are in.");
        commands.put("/build new {blueprintName}", "Generates a structure in the chunk you are in.");
        commands.put("/build refresh", "Deletes then builds the structure of the chunk you are in.");
        commands.put("/build collect", "Collects resources from the structure of the chunk you are in.");
        commands.put("/build plot",
                "Shows you where the bottom level of the structure will begin to generate to allow you to terraform.");
        commands.put("/build upgrade", "Upgrades your structure to the next level.");
        commands.put("/build hire", "Hires a new worker.");
        commands.put("/build giveall {amount}", "Gives the executer x amount of all custom resources.");
        commands.put("/build transform", "Transforms the item in your hand to its custom version, if it exists.");
        commands.put("/build help", "The command you're looking at right now.");
        commands.put("/build collectall", "/Build collect, for every single structure you own.");
        commands.put("/build supply",
                "Take the item in your hand, and supplies it to the structure of the chunk you're in.");

        ChatUtil.sendCommandHelpMessage(player, "/build", commands);
    }
}
