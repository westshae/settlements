package cc.altoya.settlements.Build;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandCost {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
            return true;
        }

        sendBlueprintCost(sender, args[1]);
        return true;
    }

    private static void sendBlueprintCost(Player player, String blueprintName) {
      if(!BlueprintUtil.doesBlueprintExist(blueprintName)){
        ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
        return;
      }
      BuildUtil.sendBuildCost(player, blueprintName);
    }

}
