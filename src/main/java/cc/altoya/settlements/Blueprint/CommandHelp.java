package cc.altoya.settlements.Blueprint;

import java.util.Map;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandHelp {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/blueprint help")) {
            return true;
        }

        help(sender);
        return true;
    }

    private static void help(Player player) {
        Map<String, String> commands = Map.of(
                "/blueprint create {blueprintName}", "The first command to create a blueprint.",
                "/blueprint first {blueprintName}",
                "Sets the blueprints chunk [0, ~, 0] point based on the block you're looking at.",
                "/blueprint second {blueprintName}",
                "Sets the blueprints chunk [15, ~, 15] point based on the block you're looking at.",
                "/blueprint save {blueprintName}",
                "Gets all blocks between the first/second point, then converts each block to a string form for future generation.",
                "/blueprint dupe {blueprintName}",
                "Places down all the blocks from the blueprint specified, without any data, to be used for a new blueprint.",
                "/blueprint upgrade {baseBlueprintName}",
                "Creates a new blueprint named {original}v{version#}, with a version of {version#}, and creates a dupe of the previous blueprint.",
                "/blueprint delete {blueprintName}",
                "Deletes the blueprint provided.",
                "/blueprint housing {blueprintName} {amount}",
                "Sets the blueprints housing count.",
                "/blueprint cost {blueprintName} {amount}",
                "Sets the blueprints resource cost for the item you are holding.",
                "/blueprint help", "The command you're looking at right now");

        ChatUtil.sendCommandHelpMessage(player, "/blueprint", commands);
    }
}
