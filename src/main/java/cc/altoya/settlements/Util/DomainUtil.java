package cc.altoya.settlements.Util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
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


  public static boolean isChunkClaimed(Chunk chunk) {
    FileConfiguration config = getDomainConfig();
    return config.contains("domains.claimed_tiles." + GeneralUtil.getKeyFromChunk(chunk));
  }

  public static boolean doesPlayerOwnChunk(Player player, Chunk chunk) {
    FileConfiguration config = getDomainConfig();
    String claim_owner_uuid = config.getString("domains.claimed_tiles." + GeneralUtil.getKeyFromChunk(chunk));
    return GeneralUtil.getKeyFromPlayer(player).equals(claim_owner_uuid);
  }

  public static void addPlayerChunk(Player player, Chunk chunk) {
    FileConfiguration config = getDomainConfig();
    if (isChunkClaimed(chunk)) {
      ChatUtil.sendErrorMessage(player, "This chunk is already claimed.");
      return;
    }
    config.set("domains.claimed_tiles." + GeneralUtil.getKeyFromChunk(chunk), GeneralUtil.getKeyFromPlayer(player));
    String playerPath = "domains." + GeneralUtil.getKeyFromPlayer(player) + ".claims";
    List<String> claims = GeneralUtil.createListFromString(config.getString(playerPath));
    claims.add(GeneralUtil.getKeyFromChunk(chunk));
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
    config.set("domains.claimed_tiles." + GeneralUtil.getKeyFromChunk(chunk), null);
    String playerPath = "domains." + GeneralUtil.getKeyFromPlayer(player) + ".claims";
    List<String> claims = GeneralUtil.createListFromString((String) config.get(playerPath));
    claims.removeIf(claim -> claim.equals(GeneralUtil.getKeyFromChunk(chunk)));
    config.set(playerPath, GeneralUtil.createStringFromList(claims));

    saveDomainConfig(config);
    ChatUtil.sendSuccessMessage(player, "Claim unclaimed.");
  }

  public static List<String> getPlayersChunks(Player player){
    FileConfiguration config = getDomainConfig();
    return GeneralUtil.createListFromString((String) config.get("domains." + GeneralUtil.getKeyFromPlayer(player) + ".claims"));
  }

  public static boolean isAllianceChatMode(Player player){
    FileConfiguration config = getDomainConfig();
    return config.getBoolean("domains." + GeneralUtil.getKeyFromPlayer(player) + ".allianceChat");
  }
}
