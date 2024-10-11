package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandHire {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/build hire")) {
            return true;
        }

        hireWorker(sender);
        return true;
    }

    private static void hireWorker(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        if(!BuildUtil.isChunkStructure(chunk)){
            ChatUtil.sendErrorMessage(player, "There is no structure in this chunk.");
            return;
        }   
        FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();
        FileConfiguration buildConfig = BuildUtil.getBuildConfig();
        String structureBlueprintName = BuildUtil.getStructureBlueprintName(chunk);

        Integer housing = blueprintConfig.getInt("blueprints." + structureBlueprintName + ".housing");
        Integer workerCount = buildConfig.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".workers");

        if(housing == workerCount){
            ChatUtil.sendErrorMessage(player, "You've maxed out the housing of this structure.");
            return;
        }

        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".workers", workerCount + 1);

        ChatUtil.sendSuccessMessage(player, "Structure now has " + (workerCount + 1) + " workers");
        BuildUtil.saveBuildConfig(buildConfig);
    }

}
