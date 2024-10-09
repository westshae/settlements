package cc.altoya.settlements.Build;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.ItemUtil;

public class CommandGiveAll {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2, "/build giveall")) {
      return true;
    }

    giveOneOfEach(sender, Integer.parseInt(args[1]));
    return true;
  }

  private static void giveOneOfEach(Player player, int amount) {
    List<Material> materials = ItemUtil.getAllResourceMaterials();
    for (Material material : materials) {
      ItemUtil.givePlayerCustomItem(player, material, amount);
    }
    ChatUtil.sendSuccessMessage(player, "You've been given " + amount + " of each type of resource.");
  }
}
