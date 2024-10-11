package cc.altoya.settlements.Blueprint;

import org.bukkit.configuration.file.FileConfiguration;
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

    private static void deleteBlueprint(Player player, String name) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();
        if (!BlueprintUtil.doesBlueprintExist(name)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }
        config.set("blueprints." + name, null);

        ChatUtil.sendSuccessMessage(player, "Blueprint deleted.");
        BlueprintUtil.saveBlueprintConfig(config);
    }

}
