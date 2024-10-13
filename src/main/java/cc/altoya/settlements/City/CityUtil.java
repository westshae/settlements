package cc.altoya.settlements.City;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.GeneralUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CityUtil {

  public static FileConfiguration getCityConfig() {
    return GeneralUtil.getPluginConfig("settlements", "cities.yml");
  }

  public static void saveCityConfig(FileConfiguration config) {
    File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(), "cities.yml");
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean isChunkClaimed(Chunk chunk) {
    FileConfiguration config = getCityConfig();
    return config.contains("cities.claimedTiles." + GeneralUtil.getKeyFromChunk(chunk));
  }

  public static boolean doesPlayerOwnChunk(Player player, Chunk chunk) {
    FileConfiguration config = getCityConfig();
    String claim_owner_uuid = config.getString("cities.claimedTiles." + GeneralUtil.getKeyFromChunk(chunk));
    return GeneralUtil.getKeyFromPlayer(player).equals(claim_owner_uuid);
  }

  public static List<String> getPlayersChunks(Player player) {
    FileConfiguration config = getCityConfig();
    return config.getStringList("cities." + GeneralUtil.getKeyFromPlayer(player) + ".claims");
  }

  public static void claimChunk(Player player, Chunk chunk) {
    FileConfiguration config = CityUtil.getCityConfig();
    config.set("cities.claimedTiles." + GeneralUtil.getKeyFromChunk(chunk), GeneralUtil.getKeyFromPlayer(player));
    String playerPath = "cities." + GeneralUtil.getKeyFromPlayer(player) + ".claims";
    List<String> claims = config.getStringList(playerPath);
    claims.add(GeneralUtil.getKeyFromChunk(chunk));
    config.set(playerPath, claims);
    CityUtil.saveCityConfig(config);
  }

  public static void setPlayerAlliance(Player player, String allianceName) {
    FileConfiguration config = CityUtil.getCityConfig();

    config.set("cities." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", allianceName);
    saveCityConfig(config);
  }

  public static void removePlayerAlliance(Player player) {
    FileConfiguration config = CityUtil.getCityConfig();

    config.set("cities." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", null);
    saveCityConfig(config);
  }

  public static boolean isPlayerMember(Player player) {
    FileConfiguration cityConfig = CityUtil.getCityConfig();
    return cityConfig.contains("cities." + GeneralUtil.getKeyFromPlayer(player) + ".alliance");
  }

  public static String getPlayerAllianceName(Player player) {
    FileConfiguration cityConfig = CityUtil.getCityConfig();
    return cityConfig.getString("cities." + GeneralUtil.getKeyFromPlayer(player) + ".alliance");
  }

  public static boolean isChatEnabled(Player player) {
    FileConfiguration cityConfig = CityUtil.getCityConfig();

    return cityConfig.getBoolean("cities." + player.getUniqueId().toString() + ".allianceChat");
  }

  public static void setAllianceChatEnabled(Player player, boolean toggle) {
    FileConfiguration cityConfig = CityUtil.getCityConfig();

    cityConfig.set("cities." + GeneralUtil.getKeyFromPlayer(player) + ".allianceChat",
        !isChatEnabled(player));
    CityUtil.saveCityConfig(cityConfig);
  }

  public static void addStructureToCity(Player player, String blueprintName, Chunk chunk) {
    FileConfiguration cityConfig = CityUtil.getCityConfig();
    cityConfig.set(
        "cities." + GeneralUtil.getKeyFromPlayer(player) + ".structures." + GeneralUtil.getKeyFromChunk(chunk)
            + ".type",
        blueprintName);
    cityConfig.set(
        "cities." + GeneralUtil.getKeyFromPlayer(player) + ".structures." + GeneralUtil.getKeyFromChunk(chunk)
            + ".level",
        1);
    CityUtil.saveCityConfig(cityConfig);
  }

  public static void upgradeStructureInCity(Player player, String blueprintName, Chunk chunk) {
    FileConfiguration cityConfig = CityUtil.getCityConfig();
    Integer currentLevel = cityConfig.getInt("cities." + GeneralUtil.getKeyFromPlayer(player) + ".structures." + GeneralUtil.getKeyFromChunk(chunk)
            + ".level");
    cityConfig.set(
        "cities." + GeneralUtil.getKeyFromPlayer(player) + ".structures." + GeneralUtil.getKeyFromChunk(chunk)
            + ".level",
            currentLevel + 1);
    CityUtil.saveCityConfig(cityConfig);
  }


  public static HashMap<String, String> getCityCommands() {
    HashMap<String, String> commands = new HashMap<>();
    commands.put("/city list", "Lists all your claimed chunks.");
    commands.put("/city expand", "Expands your city claims by a random additional chunk.");
    commands.put("/city help", "The command you're looking at right now.");
    return commands;
  }
}
