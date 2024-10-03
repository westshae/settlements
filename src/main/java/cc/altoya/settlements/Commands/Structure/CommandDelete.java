package cc.altoya.settlements.Commands.Structure;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.StructureUtil;

public class CommandDelete {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/structure delete")) {
            return true;
        }

        StructureUtil.deleteStructure(sender, sender.getLocation().getChunk());
        return true;
    }
}
