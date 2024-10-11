package cc.altoya.settlements.Blueprint;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandSecond {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
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
        Block firstPointBlock = BlueprintUtil.turnStringIntoBlock(config.getString("blueprints." + name + ".first"));

        Block chunksBlock = firstPointBlock.getChunk().getBlock(15, targettedBlock.getY(), 15);

        Location relativeSecondLocation = BlueprintUtil.getRelativeLocation(firstPointBlock, targettedBlock);
        BlueprintUtil.turnBlockIntoString(targettedBlock, relativeSecondLocation);

        if(!targettedBlock.equals(chunksBlock)){
            ChatUtil.sendErrorMessage(player, "The second point of a blueprint must be placed at a chunk's [15, ~, 15]");
            return;
        }
        config.set("blueprints." + name + ".second", BlueprintUtil.turnBlockIntoString(targettedBlock, targettedBlock.getLocation()));
        BlueprintUtil.saveBlueprintConfig(config);
        ChatUtil.sendSuccessMessage(player, "Successfully added second point to blueprint.");
    }

}
