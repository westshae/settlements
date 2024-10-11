package cc.altoya.settlements.Blueprint;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandDelete {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
            return true;
        }

        deleteBlueprint(sender, args[1]);
        return true;
    }

    private static void deleteBlueprint(Player player, String blueprintName) {
        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }
        BlueprintUtil.delete(blueprintName);

        ChatUtil.sendSuccessMessage(player, "Blueprint \"" + blueprintName + "\"deleted.");
    }

}
