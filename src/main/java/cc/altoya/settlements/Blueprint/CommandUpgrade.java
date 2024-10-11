package cc.altoya.settlements.Blueprint;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUpgrade {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 3)) {
            return true;
        }

        setUpgrade(sender, args[1], args[2]);
        return true;
    }

    private static void setUpgrade(Player player, String blueprintName, String version) {
        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player,
                    "There is no blueprint name that matched that provided. Upgrades must be based on other blueprints.");
            return;
        }


        BlueprintUtil.createUpgradeBlueprint(player, blueprintName, Integer.parseInt(version));
        ChatUtil.sendSuccessMessage(player, "New blueprint upgrade made.");
        ChatUtil.sendSuccessMessage(player, "Please note, you need to set interactives, firstBlock, secondBlock, again.");
    }
}
