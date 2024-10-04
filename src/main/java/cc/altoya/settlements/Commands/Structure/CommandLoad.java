package cc.altoya.settlements.Commands.Structure;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.StructureUtil;

public class CommandLoad {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2, "/structure load {command}")) {
            return true;
        }
        //load create {name} creates the boilerplate of a new structure
        //load setType {type}
        //load firstpoint sets one of the diagonal points showing the boundary based on where you are looking
        //load secondpoint sets one of the diagonal points showing the boundary based on where you are looking
        //load setInteractive sets the block you are looking at as an interactive block.
        switch (args[1]) {
            case "create":
                StructureUtil.generateMineBuilding(sender, sender.getLocation().getChunk());
                break;
        }
        return true;
    }
}
