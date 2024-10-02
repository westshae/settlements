package cc.altoya.settlements.Commands.Domain;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.DomainUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUnclaim {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/domain unclaim")) {
            return true;
        }
        DomainUtil.removePlayerChunk(sender, sender.getLocation().getChunk());
        return true;
    }
}
