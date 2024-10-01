package cc.altoya.settlements.Commands.Chunk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.ClaimUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandList {
  public static boolean handle(Player player, String[] args) {
    if(!GeneralUtil.handlePermissionsAndArguments(player, "settlements", "list", args, 1, "/chunk list")){
      return true;
    }

    ResultSet claims = ClaimUtil.getPlayerClaims(player);

    try {
      while(claims.next()){
        player.sendMessage(ChatUtil.getPrimaryColour() + "=======================================");

        while (claims.next()) {
          String[] existingTrusted = claims.getString("trusted").replaceAll("[{}]", "").split(",");
          String names = "";
          for(String trusted : existingTrusted){
            names += Bukkit.getOfflinePlayer(UUID.fromString(trusted)).getName() + ",";
          }
          names = names.substring(0, names.length() - 1);

          int x = claims.getInt("x");
          int y = claims.getInt("y");

          player.sendMessage(ChatUtil.getSecondaryColour() + "X: " + x + " Y: " + y + " Trusted Users: " + names);
        }

        player.sendMessage(ChatUtil.getPrimaryColour() + "=======================================");

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

}
