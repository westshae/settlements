package cc.altoya.settlements.Domain;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.GeneralUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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

  public static List<String> getPlayersChunks(Player player) {
    FileConfiguration config = getDomainConfig();
    return config.getStringList("domains." + GeneralUtil.getKeyFromPlayer(player) + ".claims");
  }

  public static boolean isAllianceChatMode(Player player) {
    FileConfiguration config = getDomainConfig();
    return config.getBoolean("domains." + GeneralUtil.getKeyFromPlayer(player) + ".allianceChat");
  }

  public static HashMap<String, String> getDomainCommands() {
    HashMap<String, String> commands = new HashMap<>();
    commands.put("/domain claim", "Claims the chunk you're currently in.");
    commands.put("/domain unclaim", "Unclaims the chunk you're currently in.");
    commands.put("/domain list", "Lists all your claimed chunks.");
    commands.put("/domain help", "The command you're looking at right now.");
    return commands;
  }
}
