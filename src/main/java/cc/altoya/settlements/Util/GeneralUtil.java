package cc.altoya.settlements.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
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

  public static List<String> createListFromString(String input) {
    if (input == null) {
      return new ArrayList<>();
    }
    String[] items = input.split(",");

    List<String> list = new ArrayList<>();

    for (String item : items) {
      list.add(item.trim());
    }
    return list;
  }

  public static String createStringFromList(List<String> list) {
    return String.join(", ", list.stream().toArray(String[]::new));
  }

  public static String getKeyFromChunk(Chunk chunk) {
    int chunkX = chunk.getX();
    int chunkZ = chunk.getZ();

    return "x" + chunkX + "y" + chunkZ;
  }

  public static Block getBlockFromKey(String key) {
    String[] parts = key.split("[xyz]");

    if (parts.length != 4) {
      return null;
    }

    try {
      int blockX = Integer.parseInt(parts[1]);
      int blockY = Integer.parseInt(parts[2]);
      int blockZ = Integer.parseInt(parts[3]);

      World world = Bukkit.getServer().getWorlds().get(0);

      return world.getBlockAt(blockX, blockY, blockZ);
    } catch (NumberFormatException e) {
      return null;
    }

  }

  public static String getKeyFromBlock(Block block) {
    int blockX = block.getX();
    int blockY = block.getY();
    int blockZ = block.getZ();

    return "x" + blockX + "y" + blockY + "z" + blockZ;
  }

  public static String getKeyFromPlayer(Player player) {
    return player.getUniqueId().toString();
  }

  public static JavaPlugin getPlugin() {
    JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("settlements");

    if(plugin == null){
      return null;
    }

    if(!plugin.isEnabled()){
      return null;
    }

    return plugin;
  }
}
