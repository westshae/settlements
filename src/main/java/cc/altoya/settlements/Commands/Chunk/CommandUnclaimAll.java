package cc.altoya.settlements.Commands.Chunk;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DatabaseUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUnclaimAll {
  public static boolean handle(Player player, String[] args) {
    if(!GeneralUtil.handlePermissionsAndArguments(player, "settlements", "unclaimall", args, 1, "/chunk unclaimall")){
      return true;
    }

    String uuid = player.getUniqueId().toString();

    String deleteQuery = "DELETE FROM claims WHERE uuid = ?";

    try {
      PreparedStatement deleteStatement = DatabaseUtil.getConnection().prepareStatement(deleteQuery);
      deleteStatement.setString(1, uuid);

      int rowsAffected = deleteStatement.executeUpdate();

      if (rowsAffected > 0) {
        ChatUtil.sendSuccessMessage(player, "All your claims have been successfully removed.");
      } else {
        ChatUtil.sendErrorMessage(player, "No claims found for your UUID.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

}
