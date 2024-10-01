package cc.altoya.settlements.Commands.Chunk;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DatabaseUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUnclaim {
  public static boolean handle(Player player, String[] args) {
    if(!GeneralUtil.handlePermissionsAndArguments(player, "settlements", "unclaim", args, 1, "/chunk unclaim")){
      return true;
    }
    
    String uuid = player.getUniqueId().toString();
    int x = player.getLocation().getChunk().getX();
    int y = player.getLocation().getChunk().getZ();

    String deleteQuery = "DELETE FROM claims WHERE uuid = ? AND x = ? AND y = ?";

    try {
      PreparedStatement deleteStatement = DatabaseUtil.getConnection().prepareStatement(deleteQuery);
      deleteStatement.setString(1, uuid);
      deleteStatement.setInt(2, x);
      deleteStatement.setInt(3, y);

      int rowsAffected = deleteStatement.executeUpdate();

      if (rowsAffected > 0) {
        ChatUtil.sendSuccessMessage(player, "Chunk unclaimed successfully.");
      } else {
        ChatUtil.sendErrorMessage(player, "No claim found at your current location.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }
}
