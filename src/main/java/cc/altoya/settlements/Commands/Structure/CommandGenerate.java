package cc.altoya.settlements.Commands.Structure;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.StructureUtil;

public class CommandGenerate {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2, "/structure generate")) {
            return true;
        }
        switch (args[1]) {
            case "mine":
                StructureUtil.generateMineBuilding(sender, sender.getLocation().getChunk());
                break;
        }
        return true;
    }
}
