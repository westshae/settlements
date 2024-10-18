package cc.altoya.settlements.Map;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandCity {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
      return true;
    }

    giveMap(sender);
    return true;
  }

  private static void giveMap(Player player) {
    MapUtil.givePlayerCityMap(player);
    ChatUtil.sendSuccessMessage(player, "Successfully given city map");
  }
}
