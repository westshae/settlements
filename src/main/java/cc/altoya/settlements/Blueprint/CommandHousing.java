package cc.altoya.settlements.Blueprint;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandHousing {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 3, "/blueprint housing")) {
            return true;
        }

        setHousing(sender, args[1], args[2]);
        return true;
    }

    private static void setHousing(Player player, String blueprintName, String amount) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();
        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }
        Integer amountInt = Integer.parseInt(amount);
        config.set("blueprints." + blueprintName + ".housing", amountInt);
        config.set("blueprints." + blueprintName + ".version", 1);

        ChatUtil.sendSuccessMessage(player, "Blueprint housing set to " + amount);
        BlueprintUtil.saveBlueprintConfig(config);
    }

}
