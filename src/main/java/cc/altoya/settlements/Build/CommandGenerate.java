package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandGenerate {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2,
                "/build generate {structureType}")) {
            return true;
        }
        generateBuildingFromBlueprint(sender, args[1]);
        return true;
    }

    private static void generateBuildingFromBlueprint(Player player, String blueprintName) {
        Chunk chunk = player.getLocation().getChunk();

        if (BuildUtil.isChunkStructure(chunk)) {
            ChatUtil.sendErrorMessage(player, "There is already a structure in this chunk.");
            return;
        }

        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }

        FileConfiguration config = BlueprintUtil.getBlueprintConfig();

        BuildUtil.createNewStructure(player, chunk, blueprintName);

        BuildUtil.placeBlocksFromBlueprint(chunk, player, blueprintName);
        ChatUtil.sendSuccessMessage(player, "Successfully generated structure from blueprint: " + blueprintName);
    }
}
