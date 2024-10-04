package cc.altoya.settlements.Commands.Structure;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.StructureUtil;

public class CommandLoad {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 3,
                "/structure load {command}")) {
            return true;
        }
        // load create {name} creates the boilerplate of a new structure
        // load type {type}
        // load firstpoint sets one of the diagonal points showing the boundary based on where you are looking
        // load secondpoint sets one of the diagonal points showing the boundary based on where you are looking
        // load setInteractive sets the block you are looking at as an interactive block.
        switch (args[1]) {
            case "create":
                createBoilerplate(args[2]);
                break;
            case "type":
                setType(args[2], args[3]);
                break;
            case "firstpoint":
                setFirstPoint(args[2]);
                break;
            case "secondpoint":
                setSecondPoint(args[2]);
                break;
            case "interactive":
                setInteractiveBlock(args[2]);
                break;
            case "save":
                saveCurrentStructure(args[2]);
                break;

        }
        return true;
    }

    private static void createBoilerplate(String name) {
        FileConfiguration config = StructureUtil.
    }

    private static void setType(String name, String type) {

    }

    private static void setFirstPoint(String name) {

    }

    private static void setSecondPoint(String name) {

    }

    private static void setInteractiveBlock(String name) {

    }

    private static void saveCurrentStructure(String name){

    }
}
