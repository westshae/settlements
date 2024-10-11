package cc.altoya.settlements.Build;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.ItemUtil;

public class CommandCollect {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }

        collectResources(sender, sender.getLocation().getChunk());
        return true;
    }

    private static void collectResources(Player player, Chunk chunk) {
        if (!CityUtil.doesPlayerOwnChunk(player, chunk)) {
            ChatUtil.sendErrorMessage(player, "You don't own this structure");
            return;
        }

        List<Material> materials = ItemUtil.getAllResourceMaterials();
        for (Material material : materials) {
            Integer resourceAmount = BuildUtil.getResourcesFromStructure(chunk, material);
            if (resourceAmount == 0) {
                continue;
            }
            BuildUtil.editResources(player, chunk, material, -resourceAmount);

            ItemUtil.givePlayerCustomItem(player, material, resourceAmount);

            ChatUtil.sendSuccessMessage(player, "You have collected " + resourceAmount + " " + ItemUtil.formatItemId(material.toString()));
        }
    }
}
