package cc.altoya.settlements.Util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class GeneralUtil {
  public static boolean handlePermissionsAndArguments(Player player, String permissionChild,
      String[] args, int requiredArgs) {
    if (!player.hasPermission("settlements." + permissionChild)) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return false;
    }
    if (args.length != requiredArgs) {
      ChatUtil.sendErrorMessage(player,
          "This command requires " + requiredArgs + " arguments. Use /" + args[0] + " help");
      return false;
    }

    return true;
  }

  public static FileConfiguration getPluginConfig(String pluginName, String configFilename) {
    File file = new File(Bukkit.getServer().getPluginManager().getPlugin(pluginName).getDataFolder(), configFilename);
    return YamlConfiguration.loadConfiguration(file);
  }

  public static void savePluginConfig(FileConfiguration config, String pluginName, String configFilename) {
    File file = new File(Bukkit.getServer().getPluginManager().getPlugin(pluginName).getDataFolder(), configFilename);
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getKeyFromChunk(Chunk chunk) {
    int chunkX = chunk.getX();
    int chunkZ = chunk.getZ();

    return "x" + chunkX + "z" + chunkZ;
  }

  public static Chunk getChunkFromKey(String key) {
    // Check if the key starts with "x" and contains "z"
    if (key.startsWith("x") && key.contains("z")) {
      // Extract the substring for chunkX and chunkZ
      String[] parts = key.split("z");
      String chunkXStr = parts[0].substring(1); // Remove the "x" prefix
      String chunkZStr = parts[1]; // Get the chunkZ part

      // Parse the chunk coordinates to integers
      int chunkX = Integer.parseInt(chunkXStr);
      int chunkZ = Integer.parseInt(chunkZStr);

      // Create and return the Chunk object (assuming you have a way to create a
      // Chunk)
      return Bukkit.getWorlds().get(0).getChunkAt(chunkX, chunkZ);
    }

    throw new IllegalArgumentException("Invalid key format: " + key);
  }

  public static String getKeyFromPlayer(Player player) {
    return player.getUniqueId().toString();
  }

  public static String getPlayerNameFromStringUuid(String uuidString) {
    try {
      UUID uuid = UUID.fromString(uuidString);
      Player owner = Bukkit.getServer().getPlayer(uuid);
      return PlainTextComponentSerializer.plainText().serialize(owner.displayName());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  public static Player getPlayerFromStringUuid(String uuidString) {
    UUID uuid = UUID.fromString(uuidString);
    return Bukkit.getServer().getPlayer(uuid);
  }

  public static JavaPlugin getPlugin() {
    JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("settlements");

    if (plugin == null) {
      return null;
    }

    if (!plugin.isEnabled()) {
      return null;
    }

    return plugin;
  }
}
