package cc.altoya.settlements.Blueprint;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandCreate {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 4, "/blueprint create")) {
            return true;
        }

        createBoilerplate(sender, args[1], args[2], args[3]);
        return true;
    }

    private static void createBoilerplate(Player player, String name, String structureType, String resourceType) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();
        if (BlueprintUtil.doesBlueprintExist(name)) {
            ChatUtil.sendErrorMessage(player, "A blueprint named this already exists.");
            return;
        }
        config.set("blueprints." + name + ".structureType", structureType);
        config.set("blueprints." + name + ".resourceType", resourceType);

        ChatUtil.sendSuccessMessage(player, "Blueprint boilerplate created.");
        BlueprintUtil.saveBlueprintConfig(config);
    }

}
