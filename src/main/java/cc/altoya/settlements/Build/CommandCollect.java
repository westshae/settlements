package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

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

    BuildUtil.collectResources(player, chunk);
    ChatUtil.sendSuccessMessage(player, "Resources collected.");
  }
}
