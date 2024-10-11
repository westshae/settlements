package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUpgrade {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }
        Chunk chunk = sender.getLocation().getChunk();
        String currentBlueprintName = BuildUtil.getStructureBlueprintName(chunk);
        String nextBlueprintName = BlueprintUtil.getUpgradedBlueprintName(currentBlueprintName);

        if (!CommandNew.takePlayerCosts(sender, nextBlueprintName)) {
            return true;
        }
        setupStructureUpgrade(sender, nextBlueprintName);
        return true;
    }

    public static void setupStructureUpgrade(Player player, String nextBlueprintName) {
        Chunk chunk = player.getLocation().getChunk();
        BuildUtil.setBlueprintName(chunk, player, nextBlueprintName);
        CommandDelete.deleteStructure(player);
        CommandNew.generateBuildingFromBlueprint(player, nextBlueprintName);
    }
}
