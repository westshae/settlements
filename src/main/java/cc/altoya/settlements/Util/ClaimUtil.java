package cc.altoya.settlements.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ClaimUtil {
  public static boolean isLocationClaimed(Location location){
    int x = location.getChunk().getX();
    int y = location.getChunk().getZ();

    try {
      if(getClaimViaCoords(x, y).next()){
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static ResultSet getClaimViaCoords(int x, int y){
    String query = "SELECT * FROM claims WHERE x = ? AND y = ?";

    try {
      PreparedStatement selectStatement = DatabaseUtil.getConnection().prepareStatement(query);
      selectStatement.setInt(1, x);
      selectStatement.setInt(2, y);
      ResultSet resultSet = selectStatement.executeQuery();
      if(resultSet.next()){
        return resultSet;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }


  public static boolean isPlayerTrusted(Player player, Location location){
    if(!isLocationClaimed(location)){
      return true;
    }
    String uuid = player.getUniqueId().toString();
    int x = location.getChunk().getX();
    int y = location.getChunk().getZ();

    String query = "SELECT * FROM claims WHERE x = ? AND y = ?";

    try {
      PreparedStatement selectStatement = DatabaseUtil.getConnection().prepareStatement(query);
      selectStatement.setInt(1, x);
      selectStatement.setInt(2, y);
      ResultSet resultSet = selectStatement.executeQuery();

      if (resultSet.next()) {
        String trusted = resultSet.getString("trusted");

        if (trusted.contains(uuid)) {
          return true;
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  public static ResultSet getPlayerClaims(Player player){
    String uuid = player.getUniqueId().toString();

    String query = "SELECT * FROM claims WHERE uuid = ?";
    try {
      PreparedStatement selectStatement = DatabaseUtil.getConnection().prepareStatement(query);
      selectStatement.setString(1, uuid);
      ResultSet resultSet = selectStatement.executeQuery();

      return resultSet;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }
}
