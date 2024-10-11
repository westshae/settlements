package cc.altoya.settlements.Util;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GeneralUtil {
  public static boolean handlePermissionsAndArguments(Player player, String permissionRoot, String permissionChild,
      String[] args, int requiredArgs, String commandString) {
    if (!player.hasPermission(permissionRoot + "." + permissionChild)) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return false;
    }
    if (args.length != requiredArgs) {
      ChatUtil.sendErrorMessage(player, "This command requires " + requiredArgs + " arguments. " + commandString);
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

        // Create and return the Chunk object (assuming you have a way to create a Chunk)
        return Bukkit.getWorlds().get(0).getChunkAt(chunkX, chunkZ);
    }
    
    throw new IllegalArgumentException("Invalid key format: " + key);
}


  public static String getKeyFromPlayer(Player player) {
    return player.getUniqueId().toString();
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
