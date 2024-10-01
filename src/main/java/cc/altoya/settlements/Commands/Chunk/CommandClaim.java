package cc.altoya.settlements.Commands.Chunk;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DatabaseUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandClaim {
  public static boolean handle(Player player, String[] args) {
    if(!GeneralUtil.handlePermissionsAndArguments(player, "settlements", "child", args, 1, "/chunk claim")){
      return true;
    }

    String uuid = player.getUniqueId().toString();
    int x = player.getLocation().getChunk().getX();
    int y = player.getLocation().getChunk().getZ();

    int claimCount = claimCount(uuid);

    FileConfiguration config = GeneralUtil.getPluginConfig("settlements", "config.yml");

    int claimCountLimit = config.getInt("claimCountLimit");
    int claimCloseByHowManyChunks = config.getInt("claimCloseByHowManyChunks");
    int claimChunkBoundary = config.getInt("claimChunkBoundary");

    if (claimCount != 0 && !connectedToCurrentClaims(uuid, x, y)) {
      ChatUtil.sendErrorMessage(player,
          "Your claims must be within " + claimCloseByHowManyChunks + " chunk of each other.");
      return true;
    }

    if (claimCount > claimCountLimit) {
      ChatUtil.sendErrorMessage(player, "You have hit your claim limit of " + claimCountLimit + ".");
      return true;
    }

    if (!claimWithinBoundary(x, y)) {
      ChatUtil.sendErrorMessage(player, "You must claim within " + claimChunkBoundary + " chunks of spawn.");
      return true;
    }

    String trusted = DatabaseUtil.getStringFromJson(uuid);

    String query = "INSERT INTO claims (uuid, x, y, trusted) VALUES (?, ?, ?, ?)";
    try {
      PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(query);
      statement.setString(1, uuid);
      statement.setInt(2, x);
      statement.setInt(3, y);
      statement.setString(4, trusted);

      int rowsAffected = statement.executeUpdate();

      if (rowsAffected > 0) {
        ChatUtil.sendSuccessMessage(player, "Chunk claimed successfully.");
      }

    } catch (SQLIntegrityConstraintViolationException e) {
      ChatUtil.sendErrorMessage(player, "This land is already claimed.");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

  public static boolean claimWithinBoundary(int x, int y) {
    FileConfiguration config = GeneralUtil.getPluginConfig("settlement", "config.yml");

    int claimChunkBoundary = config.getInt("claimChunkBoundary");

    boolean withinX = Math.abs(claimChunkBoundary) - Math.abs(x) > 0;
    boolean withinY = Math.abs(claimChunkBoundary) - Math.abs(y) > 0;

    return withinX && withinY;
  }

  public static int claimCount(String uuid) {
    String query = "SELECT * FROM claims WHERE uuid = ?";
    int count = 0;
    try {
      PreparedStatement selectStatement = DatabaseUtil.getConnection().prepareStatement(query);
      selectStatement.setString(1, uuid);
      ResultSet resultSet = selectStatement.executeQuery();

      while (resultSet.next()) {
        count++;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return count;
  }

  public static boolean connectedToCurrentClaims(String uuid, int x, int y) {
    String query = "SELECT * FROM claims WHERE uuid = ?";
    try {
      PreparedStatement selectStatement = DatabaseUtil.getConnection().prepareStatement(query);
      selectStatement.setString(1, uuid);
      ResultSet resultSet = selectStatement.executeQuery();

      while (resultSet.next()) {
        int currentX = resultSet.getInt("x");
        int currentY = resultSet.getInt("y");
        boolean isWithinOneOfX = Math.abs(x - currentX) <= 1;
        boolean isWithinOneOfY = Math.abs(y - currentY) <= 1;
        if (isWithinOneOfX && isWithinOneOfY) {
          return true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }
}
