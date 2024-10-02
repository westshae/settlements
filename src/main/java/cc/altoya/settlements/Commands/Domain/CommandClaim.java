package cc.altoya.settlements.Commands.Domain;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.DomainUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandClaim {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/domain claim")) {
            return true;
        }
        DomainUtil.addPlayerChunk(sender, sender.getLocation().getChunk());
        return true;
    }
}
