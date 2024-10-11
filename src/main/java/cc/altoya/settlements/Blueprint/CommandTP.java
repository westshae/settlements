package cc.altoya.settlements.Blueprint;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandTP {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
            return true;
        }

        teleportPlayer(sender, args[1]);
        return true;
    }

    private static void teleportPlayer(Player player, String blueprintName) {
        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }
        BlueprintUtil.teleportPlayerToBlueprint(player, blueprintName);

        ChatUtil.sendSuccessMessage(player, "Teleported Successfully.");
    }

}
