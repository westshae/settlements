package cc.altoya.settlements.Commands.Alliance;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.AllianceUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandChat {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/alliance chat")) {
            return true;
        }

        AllianceUtil.toggleAllianceChat(sender);
        return true;
    }
}
