package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Item.ItemUtil;
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

        ConfigurationSection costs = BlueprintUtil.getBlueprintCosts(nextBlueprintName);

        if(!ItemUtil.hasCosts(player, costs)){
            ChatUtil.sendErrorMessage(player, "You don't have the resources required to upgrade.");
            return;
        }


        ItemUtil.removeItems(player, costs);
        BuildUtil.upgradeStructure(player, nextBlueprintName);

        ChatUtil.sendSuccessMessage(player, "Structure upgraded successfully.");
    }
}
