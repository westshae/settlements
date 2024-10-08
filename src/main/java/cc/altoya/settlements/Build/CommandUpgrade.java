package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUpgrade {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1,
                "/build upgrade")) {
            return true;
        }
        setupStructureUpgrade(sender);
        return true;
    }

    public static void setupStructureUpgrade(Player player) {
        Chunk chunk = player.getLocation().getChunk();

        String currentBlueprintName = BuildUtil.getStructureBlueprintName(chunk);
        Integer currentVersion = BlueprintUtil.getVersion(currentBlueprintName);

        String nextBlueprintName = currentBlueprintName + "v" + Integer.toString(currentVersion + 1);

        BuildUtil.setBlueprintName(chunk, player, nextBlueprintName);
        CommandDelete.deleteStructure(player);
        CommandNew.generateBuildingFromBlueprint(player, nextBlueprintName);
    }

}
