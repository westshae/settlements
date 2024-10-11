package cc.altoya.settlements.City;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Build.BuildUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.ItemUtil;

public class CommandCollectAll {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
      return true;
    }

    collectAllResources(sender);
    return true;
  }

  private static void collectAllResources(Player player) {
    List<String> ownedStructures = BuildUtil.getPlayerBuilds(player);

    for(String chunkKey : ownedStructures){
      Chunk chunk = GeneralUtil.getChunkFromKey(chunkKey);

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
}
