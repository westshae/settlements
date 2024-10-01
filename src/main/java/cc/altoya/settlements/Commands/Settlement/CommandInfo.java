package cc.altoya.settlements.Commands.Settlement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DatabaseUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.SettlementsUtil;

public class CommandInfo {
  public static boolean handle(Player player, String[] args) {
    if(!GeneralUtil.handlePermissionsAndArguments(player, "settlements", "info", args, 2, "/settlement info {settlement-name}")){
      return true;
    }

    UUID playerUUID = player.getUniqueId();
    ResultSet settlement = SettlementsUtil.getSettlementViaUUID(playerUUID);
    String name;
    String description;
    String[] uuids;
    String[] invited;
    try {
      name = settlement.getString("name");
      description = settlement.getString("description");
      uuids = DatabaseUtil.getListFromJson(settlement.getString("uuids")); 
      invited = DatabaseUtil.getListFromJson(settlement.getString("invited_uuids")); 
      
      for(int i = 0; i < uuids.length; i++){
        uuids[i] = Bukkit.getOfflinePlayer(uuids[i]).getName();
      }

      for(int i = 0; i < invited.length; i++){
        invited[i] = Bukkit.getOfflinePlayer(invited[i]).getName();
      }

  
      ChatUtil.sendSuccessMessage(player, name);
      ChatUtil.sendSuccessMessage(player, description);
      ChatUtil.sendSuccessMessage(player, DatabaseUtil.getStringFromJson(uuids));
      ChatUtil.sendSuccessMessage(player, DatabaseUtil.getStringFromJson(invited));
  
    } catch (SQLException e) {
      e.printStackTrace();
    }

    //Get settlement from user
    //Send information to player
    // String settlementsTable = "CREATE TABLE `settlements` (`id` INT PRIMARY KEY AUTO_INCREMENT,`name` TEXT,`description` TEXT,`uuids` TEXT,`votesIds` TEXT)";

    return true;
  }

}
