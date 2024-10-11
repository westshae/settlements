package cc.altoya.settlements.Blueprint;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Build.BuildUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandDupe {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2, "/blueprint dupe")) {
            return true;
        }

        duplicateBlueprintBlocksOnly(sender, args[1]);
        return true;
    }

    public static void duplicateBlueprintBlocksOnly(Player player, String blueprintName) {
        Chunk chunk = player.getLocation().getChunk();

        FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();    
        Block firstBlock = chunk.getBlock(0, player.getLocation().getBlockY(), 0);
    
        List<String> blocks = blueprintConfig.getStringList("blueprints." + blueprintName + ".blocks");
    
        BuildUtil.placeBlocksFromStringList(blocks, firstBlock);
    
        ChatUtil.sendSuccessMessage(player, "Successfully duplicated structure from blueprint: " + blueprintName);
    
    }
}
