package cc.altoya.settlements.Blueprint;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandCreate {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
            return true;
        }

        createBoilerplate(sender, args[1]);
        return true;
    }

    private static void createBoilerplate(Player player, String blueprintName) {
        if (BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "A blueprint named this already exists.");
            return;
        }

        BlueprintUtil.create(blueprintName);

        ChatUtil.sendSuccessMessage(player, "Blueprint \"" + blueprintName + "\" created.");
    }

}
