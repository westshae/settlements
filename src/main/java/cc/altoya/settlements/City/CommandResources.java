package cc.altoya.settlements.City;

import org.bukkit.entity.Player;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandResources {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
      return true;
    }
    sendPlayerResourceInfo(sender);
    return true;
  }

  private static void sendPlayerResourceInfo(Player player) {
    CityUtil.sendCityResources(player);
  }
}
