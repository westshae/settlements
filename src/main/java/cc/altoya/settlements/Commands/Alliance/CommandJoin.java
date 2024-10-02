package cc.altoya.settlements.Commands.Alliance;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.AllianceUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandJoin {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2, "/alliance join {alliance}")) {
            return true;
        }
        AllianceUtil.joinAlliance(sender, args[1]);
        return true;
    }
}
