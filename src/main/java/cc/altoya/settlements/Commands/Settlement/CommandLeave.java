package cc.altoya.settlements.Commands.Settlement;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.GeneralUtil;

public class CommandLeave {
  public static boolean handle(Player player, String[] args) {
    if(!GeneralUtil.handlePermissionsAndArguments(player, "settlements", "leave", args, 1, "/settlement leave")){
      return true;
    }

    return true;
  }

}
