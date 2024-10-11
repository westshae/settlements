package cc.altoya.settlements.Blueprint;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandSave {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
            return true;
        }

        saveCurrentStructure(sender, args[1]);
        return true;
    }

    private static void saveCurrentStructure(Player player, String name) {
        if (!BlueprintUtil.doesBlueprintExist(name)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist, use create first.");
            return;
        }
        BlueprintUtil.save(name);

        ChatUtil.sendSuccessMessage(player, "Successfully saved structure.");
    }

}
