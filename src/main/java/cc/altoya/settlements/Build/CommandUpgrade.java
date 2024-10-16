package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUpgrade {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }
        setupStructureUpgrade(sender);
        return true;
    }

    public static void setupStructureUpgrade(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        String currentBlueprintName = BuildUtil.getStructureBlueprintName(chunk);
        String nextBlueprintName = BlueprintUtil.getUpgradedBlueprintName(currentBlueprintName);

        if (!BlueprintUtil.canAffordBlueprint(player, nextBlueprintName)) {
            ChatUtil.sendErrorMessage(player, "You can't afford this blueprint. Check you have enough.");
            return;
        }
        CityUtil.deductResourcesForBlueprint(player, nextBlueprintName);

        BuildUtil.upgradeStructure(player, nextBlueprintName);

        ChatUtil.sendSuccessMessage(player, "Structure upgraded successfully.");
    }
}
