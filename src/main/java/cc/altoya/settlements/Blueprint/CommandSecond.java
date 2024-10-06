package cc.altoya.settlements.Blueprint;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandSecond {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2, "/blueprint second")) {
            return true;
        }

        setSecond(sender, args[1]);
        return true;
    }

    private static void setSecond(Player player, String name) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();
        if (!BlueprintUtil.doesBlueprintExist(name)) {
            ChatUtil.sendErrorMessage(player, "This structure doesn't exist, use create first.");
            return;
        }
        Block targettedBlock = player.getTargetBlock(null, 10);
        if (!(targettedBlock.getX() % 16 == -1 && targettedBlock.getZ() % 16 == -1)) {// -1 is 15 from this setup
            ChatUtil.sendErrorMessage(player, "The second point of a blueprint must be placed at a chunk's [15, ~, 15]");
            return;
        }
        Block firstPointBlock = GeneralUtil
                .getBlockFromKey(config.getString("blueprints." + name + ".first"));

        if (targettedBlock.getX() != firstPointBlock.getX() + 15
                && targettedBlock.getZ() != firstPointBlock.getZ() + 15) {
            ChatUtil.sendErrorMessage(player, "Second point must be in the same chunk as first point.");
            return;
        }
        String blockKey = GeneralUtil.getKeyFromBlock(targettedBlock);
        config.set("blueprints." + name + ".second", blockKey);
        ChatUtil.sendSuccessMessage(player, "Successfully added second point to blueprint.");
        BlueprintUtil.saveBlueprintConfig(config);

    }

}
