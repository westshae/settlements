package cc.altoya.settlements.City;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Alliance.AllianceUtil;
import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

  public static UUID getChunkOwnerUUID(Chunk chunk){
    FileConfiguration config = getCityConfig();
    String claim_owner_uuid = config.getString("cities.claimedTiles." + GeneralUtil.getKeyFromChunk(chunk));
    return UUID.fromString(claim_owner_uuid);
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

  public static void addStructureToCity(Player player, String blueprintName, Chunk chunk, Material material) {
    FileConfiguration cityConfig = CityUtil.getCityConfig();
    cityConfig.set(
        "cities." + GeneralUtil.getKeyFromPlayer(player) + ".structures." + GeneralUtil.getKeyFromChunk(chunk)
            + ".type",
        blueprintName);
    cityConfig.set(
        "cities." + GeneralUtil.getKeyFromPlayer(player) + ".structures." + GeneralUtil.getKeyFromChunk(chunk)
            + ".level",
        1);
    cityConfig.set(
        "cities." + GeneralUtil.getKeyFromPlayer(player) + ".structures." + GeneralUtil.getKeyFromChunk(chunk)
            + ".material",
        material.toString());

    CityUtil.saveCityConfig(cityConfig);
  }

  public static void sendCityInfo(Player player) {
    String playerName = PlainTextComponentSerializer.plainText().serialize(player.displayName());
    int housingCount = CityUtil.getCityHousing(player);

    String header = playerName + "'s City.";
    List<String> points = new ArrayList<String>();
    points.add("Housing: " + housingCount);
    points.add("See claims via /city list");

    ChatUtil.sendPlayerListedMessage(player, header, points);
  }

  public static int getCityHousing(Player player) {
    FileConfiguration cityConfig = CityUtil.getCityConfig();
    return cityConfig.getInt("cities." + GeneralUtil.getKeyFromPlayer(player) + ".housing");
  }

  public static void editCityHousing(Player player, int amount) {
    FileConfiguration cityConfig = CityUtil.getCityConfig();
    Integer housingCount = cityConfig.getInt("cities." + GeneralUtil.getKeyFromPlayer(player) + ".housing");
    cityConfig.set("cities." + GeneralUtil.getKeyFromPlayer(player) + ".housing", housingCount + amount);
    saveCityConfig(cityConfig);
  }

  public static void upgradeStructureInCity(Player player, String blueprintName, Chunk chunk) {
    FileConfiguration cityConfig = CityUtil.getCityConfig();
    Integer currentLevel = cityConfig
        .getInt("cities." + GeneralUtil.getKeyFromPlayer(player) + ".structures." + GeneralUtil.getKeyFromChunk(chunk)
            + ".level");
    cityConfig.set(
        "cities." + GeneralUtil.getKeyFromPlayer(player) + ".structures." + GeneralUtil.getKeyFromChunk(chunk)
            + ".level",
        currentLevel + 1);
    CityUtil.saveCityConfig(cityConfig);
  }

  public static void deductResourcesForBlueprint(Player player, String blueprintName) {
    ConfigurationSection blueprintCosts = BlueprintUtil.getBlueprintCosts(blueprintName);

    if (blueprintCosts == null) {
        return;
    }

    for (String itemMaterialString : blueprintCosts.getKeys(false)) {
        Material material = Material.matchMaterial(itemMaterialString);
        if (material == null) {
            continue;
        }

        int requiredAmount = blueprintCosts.getInt(itemMaterialString);

        CityUtil.editCityResources(player, material, -requiredAmount);
    }

    return;
}


  public static void editCityResources(Player player, Material material, double amount) {
    FileConfiguration cityConfig = CityUtil.getCityConfig();
    Double resourceCount = cityConfig
        .getDouble("cities." + GeneralUtil.getKeyFromPlayer(player) + ".resources." + material.toString());
    cityConfig.set(
        "cities." + GeneralUtil.getKeyFromPlayer(player) + ".resources." + material.toString(), resourceCount + amount);
    saveCityConfig(cityConfig);
  }

  public static Integer getWorkers(Player player){
    FileConfiguration cityConfig = CityUtil.getCityConfig();
    return cityConfig.getInt("cities." + GeneralUtil.getKeyFromPlayer(player) + ".workers");
  }

  public static boolean hasHousingSpace(Player player){
    Integer workerCount = getWorkers(player);
    Integer housingCount = getCityHousing(player);
    return workerCount + 1 <= housingCount;
  }

  public static void hireWorker(Player player){
    FileConfiguration cityConfig = CityUtil.getCityConfig();

    Integer workerCount = getWorkers(player);
    Integer housingCount = getCityHousing(player);
    if(workerCount + 1 <= housingCount){
      cityConfig.set("cities." + GeneralUtil.getKeyFromPlayer(player) + ".workers", workerCount + 1);
      CityUtil.saveCityConfig(cityConfig);
    }
  }

  public static boolean hasResourcesAvailable(Player player, Material material, double needed){
    FileConfiguration cityConfig = CityUtil.getCityConfig();
    Double resourceCount = cityConfig
        .getDouble("cities." + GeneralUtil.getKeyFromPlayer(player) + ".resources." + material.toString());

    return resourceCount > needed;
  }

  public static String getChunkAlliance(Chunk chunk){
    UUID ownerUuid = getChunkOwnerUUID(chunk);
    Player owner = Bukkit.getServer().getPlayer(ownerUuid);
    return CityUtil.getPlayerAllianceName(owner);
    
  }

  public static void sendCityResources(Player player){
    FileConfiguration cityConfig = CityUtil.getCityConfig();

    String cityKey = "cities." + GeneralUtil.getKeyFromPlayer(player) + ".resources";
    ConfigurationSection resourceSection = cityConfig.getConfigurationSection(cityKey);
    String playerName = PlainTextComponentSerializer.plainText().serialize(player.displayName());

    String header = playerName + "'s City Resources";
    List<String> points = new ArrayList<String>();

    if (resourceSection == null) {
        points.add("No resources available.");
        ChatUtil.sendPlayerListedMessage(player, header, points);
        return;
    }

    for (String materialName : resourceSection.getKeys(false)) {
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            continue;
        }

        double amount = resourceSection.getDouble(materialName);
        points.add(materialName + ": " + amount);
    }
    ChatUtil.sendPlayerListedMessage(player, header, points);
  }

  public static HashMap<String, String> getCityCommands() {
    HashMap<String, String> commands = new HashMap<>();
    commands.put("/city list", "Lists all your claimed chunks.");
    commands.put("/city expand", "Expands your city claims by a random additional chunk.");
    commands.put("/city hire", "Hires a new worker if you have available housing.");
    commands.put("/city resources", "Sends you info about your cities' resources.");
    commands.put("/city info", "Sends you information about your city.");
    commands.put("/city help", "The command you're looking at right now.");
    return commands;
  }
}
