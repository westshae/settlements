package cc.altoya.settlements.Commands.Settlement;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.SettlementsUtil;

public class CommandKick {
  public static boolean handle(Player player, String[] args) {
    if(!GeneralUtil.handlePermissionsAndArguments(player, "settlements", "kick", args, 2, "/settlement kick {username}")){
      return true;
    }

    ResultSet settlement = SettlementsUtil.getSettlementViaUUID(player.getUniqueId());

    try {
      if(settlement.next()){
        OfflinePlayer kicked = Bukkit.getOfflinePlayer(args[1]);
        if(!kicked.hasPlayedBefore()){
          ChatUtil.sendErrorMessage(player, "This player can't be found.");
          return true;
        }
        String uuids = settlement.getString("uuids");
        if(uuids.contains(kicked.getUniqueId().toString())){
          SettlementsUtil.createVoteFor(settlement.getString("name"), 1, args);
          ChatUtil.sendSuccessMessage(player, "New vote has been created!");
          return true;
        } else {
          ChatUtil.sendErrorMessage(player, "This player isn't in the settlement.");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    //Get settlement from user
    //Check if user exists in settlement
    //Start vote on kick

    return true;
  }

}
