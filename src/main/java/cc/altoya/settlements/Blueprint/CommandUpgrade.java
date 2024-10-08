package cc.altoya.settlements.Blueprint;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Build.CommandNew;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUpgrade {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 3, "/blueprint upgrade")) {
            return true;
        }

        setUpgrade(sender, args[1], args[2]);
        return true;
    }

    private static void setUpgrade(Player player, String originalBlueprintName, String version) {
        int versionInt = Integer.parseInt(version);
        String currentBlueprintName = originalBlueprintName + "v" + version;

        if (!BlueprintUtil.doesBlueprintExist(originalBlueprintName)) {
            ChatUtil.sendErrorMessage(player,
                    "There is no blueprint name that matched that provided. Upgrades must be based on other blueprints.");
            return;
        }
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        config.set("blueprints." + currentBlueprintName + ".version", versionInt);

        BlueprintUtil.saveBlueprintConfig(config);

        CommandNew.generateBuildingFromBlueprint(player, currentBlueprintName);
        ChatUtil.sendSuccessMessage(player, "New blueprint upgrade based off " + originalBlueprintName + " has been made. The new name is " + currentBlueprintName);
        ChatUtil.sendSuccessMessage(player, "Please note, you need to set interactives, firstBlock, secondBlock, again.");
    }
}
