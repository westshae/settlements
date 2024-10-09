package cc.altoya.settlements.Build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class BuildUtil {
  public static FileConfiguration getBuildConfig() {
    return GeneralUtil.getPluginConfig("settlements", "builds.yml");
  }

  public static void saveBuildConfig(FileConfiguration config) {
    File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(),
        "builds.yml");
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Integer getStructurePlayerheight(Chunk chunk, Player player) {
    FileConfiguration buildConfig = getBuildConfig();
    if (!buildConfig.contains("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".playerHeight")) {
      return null;
    } else {
      return buildConfig.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".playerHeight");
    }
  }

  public static String getStructureBlueprintName(Chunk chunk) {
    FileConfiguration config = getBuildConfig();
    if (!isChunkStructure(chunk)) {
      return null;
    }
    return config.getString("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".blueprintName");
  }

  public static boolean isBlockStructureBlock(Chunk chunk) {
    FileConfiguration config = getBuildConfig();
    return config.contains("builds." + GeneralUtil.getKeyFromChunk(chunk));
  }

  public static boolean isChunkStructure(Chunk chunk) {
    FileConfiguration config = getBuildConfig();
    return config.contains("builds." + GeneralUtil.getKeyFromChunk(chunk));
  }

  public static String getStructureOwner(Chunk chunk) {
    FileConfiguration config = getBuildConfig();
    return config.getString("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".owner");
  }

  public static void placeBlockForStructure(Location location, Material material,
      BlockData blockData) {
    Block block = location.getBlock();
    block.setType(material, false);
    block.setBlockData(blockData, false);
  }

  public static void setBlueprintName(Chunk chunk, Player player, String newBlueprintName) {
    if (!BuildUtil.isChunkStructure(chunk)) {
      return;
    }
    FileConfiguration buildConfig = getBuildConfig();
    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".blueprintName", newBlueprintName);

    saveBuildConfig(buildConfig);
  }

  public static void setVersion(Chunk chunk, Player player, int version) {
    if (!BuildUtil.isChunkStructure(chunk)) {
      return;
    }
    FileConfiguration buildConfig = getBuildConfig();
    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".version", version);

    saveBuildConfig(buildConfig);
  }

  public static List<Material> getValidSupplyTypes() {
    return List.of(Material.COAL);
  }

  public static boolean isValidSupplyType(Material material) {
    return getValidSupplyTypes().contains(material);
  }

  public static void editSupplies(Player player, Chunk chunk, Material material, int amount) {
    FileConfiguration config = getBuildConfig();
    if (!isChunkStructure(chunk)) {
      return;
    }

    if (!isValidSupplyType(material)) {
      return;
    }

    int currentAmount = config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".supplies." + material);
    config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".supplies." + material, currentAmount + amount);

    saveBuildConfig(config);
  }

  public static Integer getSuppliesFromStructure(Chunk chunk, Material material) {
    FileConfiguration config = getBuildConfig();
    if (!isChunkStructure(chunk)) {
      return null;
    }

    if (!isValidSupplyType(material)) {
      return null;
    }
    return config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".supplies." + material);
  }

  public static void editResources(Player player, Chunk chunk, Material material, int amount) {
    FileConfiguration config = getBuildConfig();
    if (!isChunkStructure(chunk)) {
      return;
    }
    int currentAmount = config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".resources." + material);
    config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".resources." + material, currentAmount + amount);

    saveBuildConfig(config);
  }

  public static Integer getResourcesFromStructure(Chunk chunk, Material material) {
    FileConfiguration config = getBuildConfig();
    if (!isChunkStructure(chunk)) {
      return null;
    }
    return config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".resources." + material);
  }

  public static void placeBlocksFromStringList(List<String> blocks, Block firstBlock) {
    new BukkitRunnable() {
      int index = 0;

      @Override
      public void run() {
        int blocksProcessed = 0;

        while (index < blocks.size() && blocksProcessed < 1) {
          String blockString = blocks.get(index);
          Block block = BlueprintUtil.turnStringIntoBlock(blockString);

          if (block != null) {
            Location relativeLocation = block.getLocation();
            Location nonRelativeLocation = BlueprintUtil.getNonRelativeLocation(firstBlock,
                relativeLocation);

            BuildUtil.placeBlockForStructure(nonRelativeLocation, block.getType(), block.getBlockData());

            blocksProcessed++;
          }
          index++;
        }

        // If there are more blocks to process, schedule the next run after a brief
        // pause
        if (index < blocks.size()) {
          // Pause for 1 tick (50 milliseconds) to allow server to process other tasks
          this.runTaskLater(GeneralUtil.getPlugin(), 1); // Replace MyPlugin with your plugin instance class
        } else {
          this.cancel(); // Stop the runnable if all blocks have been processed
        }
      }
    }.runTaskTimer(GeneralUtil.getPlugin(), 0, 1); // Start immediately and run every tick

  }

  public static List<String> getPlayerBuilds(Player player) {
    FileConfiguration config = BuildUtil.getBuildConfig();
    List<String> playerBuilds = new ArrayList<>();
    UUID playerUUID = player.getUniqueId();

    // Get all keys under "builds"
    Set<String> buildsKeys = config.getConfigurationSection("builds").getKeys(false);

    // Iterate through each build key
    for (String key : buildsKeys) {
      // Get the owner's UUID as a string
      String ownerUUIDString = config.getString("builds." + key + ".owner");

      // Check if the owner matches the player's UUID
      if (ownerUUIDString != null && ownerUUIDString.equals(playerUUID.toString())) {
        // Add the key to the list if it matches
        playerBuilds.add(key);
      }
    }

    return playerBuilds;
  }

  public static boolean isBuildAreaEmpty(Player player, String blueprintName) {
    Chunk chunk = player.getLocation().getChunk();
    FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();

    int playerHeight = player.getLocation().getBlockY();

    Block firstBlock = chunk.getBlock(0, playerHeight, 0);

    String secondBlockKey = blueprintConfig.getString("blueprints." + blueprintName + ".second");
    Block blueprintSecondBlock = BlueprintUtil.turnStringIntoBlock(secondBlockKey);

    Location nonRelativeSecondLocation = BlueprintUtil.getNonRelativeLocation(firstBlock,
        blueprintSecondBlock.getLocation());
    Block secondBlock = chunk.getWorld().getBlockAt(nonRelativeSecondLocation.getBlockX(),
        nonRelativeSecondLocation.getBlockY(), nonRelativeSecondLocation.getBlockZ());

    // Check if all blocks between the two points are air
    for (int x = firstBlock.getX() ; x <= secondBlock.getX(); x++) {
      for (int y = firstBlock.getY(); y <= secondBlock.getY(); y++) {
        for (int z = firstBlock.getZ(); z <= secondBlock.getZ(); z++) {
          Block block = chunk.getWorld().getBlockAt(x, y, z);
          if (block.getType() != Material.AIR) {
            ChatUtil.sendErrorMessage(player, "The area you wish you place your structure isn't empty.");
            ChatUtil.sendErrorMessage(player, "Use /build plot to see the outline. Clear between Y=" + firstBlock.getY() + " and Y="
                + secondBlock.getY() + " within the particles.");

            return false; // Found a block that is not air
          }
        }
      }
    }

    return true; // All blocks in the area are air
  }

}