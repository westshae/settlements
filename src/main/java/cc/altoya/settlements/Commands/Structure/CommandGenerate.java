package cc.altoya.settlements.Commands.Structure;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.StructureUtil;

public class CommandGenerate {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/structure generate")) {
            return true;
        }
        StructureUtil.placeBlock(sender);
        return true;
    }
}
