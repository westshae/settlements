package cc.altoya.settlements.Util;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
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

    return "x" + chunkX + "y" + chunkZ;
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
