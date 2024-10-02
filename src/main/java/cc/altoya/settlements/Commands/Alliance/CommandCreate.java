package cc.altoya.settlements.Commands.Alliance;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.AllianceUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandCreate {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2, "/alliance create {name}")) {
            return true;
        }

        AllianceUtil.createAlliance(sender, args[1]);
        return true;
    }
}
