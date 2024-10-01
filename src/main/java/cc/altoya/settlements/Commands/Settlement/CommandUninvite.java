package cc.altoya.settlements.Commands.Settlement;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUninvite {
  public static boolean handle(Player player, String[] args) {
    if(!GeneralUtil.handlePermissionsAndArguments(player, "settlements", "uninvite", args, 2, "/settlement uninvite {username}")){
      return true;
    }

    //Get settlement from player
    //Check has an invite currently for user
    //Start vote for user being removed from invites

    return true;
  }

}
