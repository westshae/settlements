package cc.altoya.settlements.Blueprint;

import org.bukkit.configuration.file.FileConfiguration;
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

    private static void createBoilerplate(Player player, String name) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();
        if (BlueprintUtil.doesBlueprintExist(name)) {
            ChatUtil.sendErrorMessage(player, "A blueprint named this already exists.");
            return;
        }
        config.set("blueprints." + name + ".version", 1);
        config.set("blueprints." + name + ".housing", 0);

        ChatUtil.sendSuccessMessage(player, "Blueprint boilerplate created.");
        BlueprintUtil.saveBlueprintConfig(config);
    }

}
