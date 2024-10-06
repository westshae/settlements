package cc.altoya.settlements.Blueprint;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandInteractive {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2, "/blueprint interactive")) {
            return true;
        }

        setInteractiveBlock(sender, args[1]);
        return true;
    }

    private static void setInteractiveBlock(Player player, String name) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();
        if (!BlueprintUtil.doesBlueprintExist(name)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist, use create first.");
            return;
        }
        Block targettedBlock = player.getTargetBlock(null, 10);
        String blockKey = GeneralUtil.getKeyFromBlock(targettedBlock);
        List<String> interactiveBlocks = GeneralUtil
                .createListFromString((String) config.get("blueprints." + name + ".interactive"));
        interactiveBlocks.add(blockKey);
        config.set("blueprints." + name + ".interactive",
                GeneralUtil.createStringFromList(interactiveBlocks));
        ChatUtil.sendSuccessMessage(player, "Added block to blueprints interactive block list.");
        BlueprintUtil.saveBlueprintConfig(config);
    }

}
