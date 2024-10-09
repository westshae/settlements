package cc.altoya.settlements.Build;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.ItemUtil;

public class CommandTransform {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/build transform")) {
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
    ItemUtil.givePlayerCustomItem(player, hand.getType(), hand.getAmount());
    hand.setAmount(0);
    ChatUtil.sendSuccessMessage(player, "Your item has been changed into custom resources.");
  }
}
