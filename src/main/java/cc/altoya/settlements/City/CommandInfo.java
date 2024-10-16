package cc.altoya.settlements.City;

import org.bukkit.entity.Player;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandInfo {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
      return true;
    }
    sendPlayerCityInfo(sender);
    return true;
  }

  private static void sendPlayerCityInfo(Player player) {
    CityUtil.sendCityInfo(player);
  }
}
