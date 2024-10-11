package cc.altoya.settlements.Blueprint;

import org.bukkit.entity.Player;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandList {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }

        sendBlueprints(sender);
        return true;
    }

    private static void sendBlueprints(Player player) {
        BlueprintUtil.sendPlayerBlueprintList(player);
    }

}
