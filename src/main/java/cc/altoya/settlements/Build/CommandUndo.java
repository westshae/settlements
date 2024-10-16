package cc.altoya.settlements.Build;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUndo {
  public static boolean handle(Player player, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(player, "settlements", args, 1)) {
      return true;
    }

    undoBuild(player);
    return true;
  }

  public static void undoBuild(Player player) {
    BuildUtil.undoBuilding(player.getLocation().getChunk());
    BuildUtil.deleteData(player, player.getChunk());
    ChatUtil.sendSuccessMessage(player, "Building undone.");
  }
}
