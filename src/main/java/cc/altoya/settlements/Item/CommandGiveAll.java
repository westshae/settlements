package cc.altoya.settlements.Item;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandGiveAll {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
      return true;
    }

    giveOneOfEach(sender, Integer.parseInt(args[1]));
    return true;
  }

  private static void giveOneOfEach(Player player, int amount) {
    ItemUtil.givePlayerEachResource(player, amount);
    ChatUtil.sendSuccessMessage(player, "You've been given " + amount + " of each type of resource.");
  }
}
