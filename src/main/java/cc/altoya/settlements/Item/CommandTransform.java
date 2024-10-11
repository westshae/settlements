package cc.altoya.settlements.Item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandTransform {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
      return true;
    }

    transformHand(sender);
    return true;
  }

  private static void transformHand(Player player) {
    List<Material> materials = ItemUtil.getAllResourceMaterials();
    ItemStack hand = player.getInventory().getItemInMainHand();
    if(!materials.contains(hand.getType())){
      ChatUtil.sendErrorMessage(player, "There are no custom items for " + hand.getType());
      return;
    }
    ItemUtil.transformItems(player);
    ChatUtil.sendSuccessMessage(player, "Your item has been changed into custom resources.");
  }
}