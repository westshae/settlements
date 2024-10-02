package cc.altoya.settlements.Util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DomainUtil {

  public static FileConfiguration getDomainConfig() {
    return GeneralUtil.getPluginConfig("settlements", "domains.yml");
  }

  public static void saveDomainConfig(FileConfiguration config) {
    File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(), "domains.yml");
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static Chunk getChunkFromKey(World world, String key) {
    String[] parts = key.split("y");
    int chunkX = Integer.parseInt(parts[0].substring(1));
    int chunkZ = Integer.parseInt(parts[1]);

    return world.getChunkAt(chunkX, chunkZ);
  }

  private static String getKeyFromChunk(Chunk chunk) {
    int chunkX = chunk.getX();
    int chunkZ = chunk.getZ();

    return "x" + chunkX + "y" + chunkZ;
  }

  public static boolean isChunkClaimed(Chunk chunk) {
    FileConfiguration config = getDomainConfig();
    return config.contains("domains.claimed_tiles." + getKeyFromChunk(chunk));
  }

  public static boolean doesPlayerOwnChunk(Player player, Chunk chunk) {
    FileConfiguration config = getDomainConfig();
    String claim_owner_uuid = config.getString("domains.claimed_tiles." + getKeyFromChunk(chunk));
    return player.getUniqueId().toString().equals(claim_owner_uuid);
  }

  public static void addPlayerChunk(Player player, Chunk chunk) {
    FileConfiguration config = getDomainConfig();
    if (isChunkClaimed(chunk)) {
      ChatUtil.sendErrorMessage(player, "This chunk is already claimed.");
      return;
    }
    config.set("domains.claimed_tiles." + getKeyFromChunk(chunk), player.getUniqueId().toString());
    String playerPath = "domains." + player.getUniqueId().toString() + ".claims";
    List<String> claims = GeneralUtil.createListFromString(config.getString(playerPath));
    claims.add(getKeyFromChunk(chunk));
    config.set(playerPath, GeneralUtil.createStringFromList(claims));
    saveDomainConfig(config);
    ChatUtil.sendSuccessMessage(player, "Chunk claimed");
  }

  public static void removePlayerChunk(Player player, Chunk chunk) {
    FileConfiguration config = getDomainConfig();
    if (!isChunkClaimed(chunk)) {
      ChatUtil.sendErrorMessage(player, "This chunk isn't claimed.");
      return;
    }
    if (!doesPlayerOwnChunk(player, chunk)) {
      ChatUtil.sendErrorMessage(player, "You aren't the owner of this claim.");
      return;
    }
    config.set("domains.claimed_tiles." + getKeyFromChunk(chunk), null);
    String playerPath = "domains." + player.getUniqueId().toString() + ".claims";
    List<String> claims = GeneralUtil.createListFromString((String) config.get(playerPath));
    claims.removeIf(claim -> claim.equals(getKeyFromChunk(chunk)));
    config.set(playerPath, GeneralUtil.createStringFromList(claims));

    saveDomainConfig(config);
    ChatUtil.sendSuccessMessage(player, "Claim unclaimed.");
  }

  public static List<String> getPlayersChunks(Player player){
    FileConfiguration config = getDomainConfig();
    return GeneralUtil.createListFromString((String) config.get("domains." + player.getUniqueId().toString() + ".claims"));
  }

}
