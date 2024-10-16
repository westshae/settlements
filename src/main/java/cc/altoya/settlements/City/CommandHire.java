package cc.altoya.settlements.City;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandHire {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
      return true;
    }

    hireWorker(sender);
    return true;
  }

  private static void hireWorker(Player player) {
    if (!CityUtil.hasHousingSpace(player)) {
      ChatUtil.sendErrorMessage(player, "You need more housing to hire more.");
      return;
    }

    CityUtil.hireWorker(player);
    ChatUtil.sendSuccessMessage(player, "Successfully hired new worker.");
  }
}
