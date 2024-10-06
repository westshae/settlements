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

    private static void help(Player player){
        Map<String, String> commands = Map.of(
            "/blueprint create {blueprintName} {blueprintType}", "The first command to create a blueprint.",
            "/blueprint first {blueprintName}", "Sets the blueprints chunk [0, ~, 0] point based on the block you're looking at.",
            "/blueprint second {blueprintName}", "Sets the blueprints chunk [15, ~, 15] point based on the block you're looking at.",
            "/blueprint interactive {blueprintName}", "Specifies if a block is an interactive block (A mine's ore, a factories lever, etc) based on the block you're looking at.",
            "/blueprint save {blueprintName}", "Gets all blocks between the first/second point, then converts each block to a string form for future generation.",
            "/blueprint help", "The command you're looking at right now"
        );

        ChatUtil.sendCommandHelpMessage(player, "/blueprint", commands);
    }
}
