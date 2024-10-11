package cc.altoya.settlements.Blueprint;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandHousing {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 3)) {
            return true;
        }

        setHousing(sender, args[1], args[2]);
        return true;
    }

    private static void setHousing(Player player, String blueprintName, String amount) {
        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }
        BlueprintUtil.setBlueprintHousing(blueprintName, Integer.parseInt(amount));

        ChatUtil.sendSuccessMessage(player, "Blueprint housing set to " + amount);
    }

}
