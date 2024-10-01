package cc.altoya.settlements.Util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DatabaseUtil {
  private static Connection connection = null;

  public static void initializeConnection() {
    File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(), "config.yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    String port = config.getString("databasePort");
    String ip = config.getString("databaseUrl");
    String username = config.getString("databaseUsername");
    String password = config.getString("databasePassword");

    String url = "jdbc:mysql://" + ip + ":" + port;

    String selectDatabaseQuery = "USE mysql";

    try {
      // Establish the connection using the provided username and password
      connection = DriverManager.getConnection(url, username, password);
      PreparedStatement selectDatabaseStatement = connection.prepareStatement(selectDatabaseQuery);
      selectDatabaseStatement.execute();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static Connection getConnection(){
    return connection;
  }

  public static String[] getListFromJson(String jsonString){
    return jsonString.replaceAll("[{}]", "").split(",");
  }

  public static String getStringFromJson(String[] jsonList){
    String jsonString = "{";
    for(String current : jsonList){
      jsonString += current + ",";
    }

    if(jsonList.length != 0){
      jsonString = jsonString.substring(0, jsonString.length() - 1);
    }

    return jsonString;
  }

  public static String getStringFromJson(String object){
    return "{" + object + "}";
  }
}
