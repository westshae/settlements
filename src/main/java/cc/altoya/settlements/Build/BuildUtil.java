package cc.altoya.settlements.Build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.City.CityUtil;
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
    return List.of(Material.COAL, Material.WHEAT);
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

  public static void placeNonRelativeBlocksFromStringList(List<String> blocks, Block firstBlock, String blueprintName) {
    new BukkitRunnable() {
      List<String> removedBlocked = new ArrayList<String>();
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

            removedBlocked.add(BlueprintUtil.turnBlockIntoString(nonRelativeLocation.getBlock(), nonRelativeLocation));

            BuildUtil.placeBlockForStructure(nonRelativeLocation, block.getType(), block.getBlockData());

            blocksProcessed++;
          }
          index++;
        }

        if (index < blocks.size()) {
          this.runTaskLater(GeneralUtil.getPlugin(), 1);
        } else {
          BuildUtil.saveDeletedBlocksToBuild(removedBlocked, firstBlock.getChunk());
          this.cancel();
        }
      }
    }.runTaskTimer(GeneralUtil.getPlugin(), 0, 1);

  }

  public static void placeBlocksFromStringList(List<String> blocks) {
    new BukkitRunnable() {
      int index = 0;

      @Override
      public void run() {
        int blocksProcessed = 0;

        while (index < blocks.size() && blocksProcessed < 1) {
          String blockString = blocks.get(index);
          Block block = BlueprintUtil.turnStringIntoBlock(blockString);

          if (block != null) {
            BuildUtil.placeBlockForStructure(block.getLocation(), block.getType(), block.getBlockData());

            blocksProcessed++;
          }
          index++;
        }

        if (index < blocks.size()) {
          this.runTaskLater(GeneralUtil.getPlugin(), 1);
        } else {
          this.cancel();
        }
      }
    }.runTaskTimer(GeneralUtil.getPlugin(), 0, 1);

  }

  public static void saveDeletedBlocksToBuild(List<String> deletedBlocksList, Chunk chunk) {
    FileConfiguration buildConfig = getBuildConfig();
    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".deletedBlocks", deletedBlocksList);
    saveBuildConfig(buildConfig);
  }

  public static List<String> getDeletedBlocks(Chunk chunk) {
    FileConfiguration buildConfig = getBuildConfig();
    return buildConfig.getStringList("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".deletedBlocks");
  }

  public static List<String> getPlayerBuilds(Player player) {
    FileConfiguration config = BuildUtil.getBuildConfig();
    List<String> playerBuilds = new ArrayList<>();
    UUID playerUUID = player.getUniqueId();

    Set<String> buildsKeys = config.getConfigurationSection("builds").getKeys(false);

    for (String key : buildsKeys) {
      String ownerUUIDString = config.getString("builds." + key + ".owner");

      if (ownerUUIDString != null && ownerUUIDString.equals(playerUUID.toString())) {
        playerBuilds.add(key);
      }
    }

    return playerBuilds;
  }

  public static Block getFirstBlock(Chunk chunk) {
    FileConfiguration buildConfig = BuildUtil.getBuildConfig();

    String buildFirstKey = buildConfig.getString("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".first");
    return BlueprintUtil.turnStringIntoBlock(buildFirstKey);
  }

  public static void deleteData(Player player, Chunk chunk) {
    FileConfiguration buildConfig = BuildUtil.getBuildConfig();
    int housingCount = buildConfig.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".housing");
    CityUtil.editCityHousing(player, -housingCount);
    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk), null);
    saveBuildConfig(buildConfig);
  }

  public static void deleteBlocks(Player player) {
    FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();

    Chunk chunk = player.getLocation().getChunk();
    String blueprintName = BuildUtil.getStructureBlueprintName(chunk);

    Block firstBlock = getFirstBlock(chunk);

    List<String> blocks = blueprintConfig.getStringList("blueprints." + blueprintName + ".blocks");

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
            Location nonRelativeLocation = BlueprintUtil.getNonRelativeLocation(firstBlock, relativeLocation);
            Block nonRelativeBlock = nonRelativeLocation.getBlock();
            nonRelativeBlock.setType(Material.AIR, false);

            blocksProcessed++;
          } else {
            ChatUtil.sendErrorMessage(player, "Error converting block from string: " + blockString);
          }
          index++;
        }

        if (index < blocks.size()) {
          this.runTaskLater(GeneralUtil.getPlugin(), 1);
        } else {
          this.cancel();
        }
      }
    }.runTaskTimer(GeneralUtil.getPlugin(), 0, 1);
  }

  public static boolean hasHousingRoom(Chunk chunk) {
    FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();
    FileConfiguration buildConfig = BuildUtil.getBuildConfig();
    String structureBlueprintName = BuildUtil.getStructureBlueprintName(chunk);

    Integer housing = blueprintConfig.getInt("blueprints." + structureBlueprintName + ".housing");
    Integer workerCount = buildConfig.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".workers");

    return housing < workerCount;
  }

  public static void setBuildConfig(Player player, String blueprintName) {
    Integer version = BlueprintUtil.getVersion(blueprintName);

    FileConfiguration buildConfig = BuildUtil.getBuildConfig();

    int playerHeight = player.getLocation().getBlockY();
    Chunk chunk = player.getLocation().getChunk();
    Material material = Material.getMaterial(BlueprintUtil.getBlueprintMaterial(blueprintName));
    int housingCount = BlueprintUtil.getHousing(blueprintName);

    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".owner",
        GeneralUtil.getKeyFromPlayer(player));
    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".blueprintName", blueprintName);
    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".version", version);
    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".housing", housingCount);
    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".material",
        material.toString());
    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".playerHeight", playerHeight);

    Block newFirstBlock = chunk.getBlock(0, playerHeight - 5, 0);
    Block newSecondBlock = BlueprintUtil.getRelativeSecondBlock(newFirstBlock, blueprintName);

    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".first",
        BlueprintUtil.turnBlockIntoString(newFirstBlock, newFirstBlock.getLocation()));
    buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".second",
        BlueprintUtil.turnBlockIntoString(newSecondBlock, newSecondBlock.getLocation()));

    BuildUtil.saveBuildConfig(buildConfig);

    CityUtil.addStructureToCity(player, blueprintName, chunk, material);
    CityUtil.editCityHousing(player, housingCount);
  }

  public static void generateBuildingFromBlueprint(Player player, String blueprintName) {
    Chunk chunk = player.getLocation().getChunk();

    FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();

    Block buildFirstBlock = BuildUtil.getFirstBlock(chunk);

    List<String> blocks = blueprintConfig.getStringList("blueprints." + blueprintName + ".blocks");

    BuildUtil.placeNonRelativeBlocksFromStringList(blocks, buildFirstBlock, blueprintName);
  }

  public static void undoBuilding(Chunk chunk) {
    List<String> deletedBlocks = getDeletedBlocks(chunk);

    BuildUtil.placeBlocksFromStringList(deletedBlocks);
  }

  public static void displayParticleChunkBorder(Chunk chunk, int yLevel) {
    new BukkitRunnable() {
      int ticksElapsed = 0;

      @Override
      public void run() {
        if (ticksElapsed > 200) {
          this.cancel();
          return;
        }

        World world = chunk.getWorld();
        int chunkX = chunk.getX() * 16;
        int chunkZ = chunk.getZ() * 16;

        Particle particleType = Particle.HAPPY_VILLAGER;
        double particleOffset = 0.5;
        int particleCount = 1;

        for (int x = chunkX; x < chunkX + 16; x++) {
          world.spawnParticle(particleType, x + particleOffset, yLevel, chunkZ + particleOffset, particleCount);
          world.spawnParticle(particleType, x + particleOffset, yLevel, chunkZ + 15 + particleOffset, particleCount);
        }

        for (int z = chunkZ; z < chunkZ + 16; z++) {
          world.spawnParticle(particleType, chunkX + particleOffset, yLevel, z + particleOffset, particleCount);
          world.spawnParticle(particleType, chunkX + 15 + particleOffset, yLevel, z + particleOffset, particleCount);
        }

        ticksElapsed += 5;
      }
    }.runTaskTimer(GeneralUtil.getPlugin(), 0L, 5);
  }

  public static void removeSupplyItems(Player player, Chunk chunk, ItemStack item, int amount) {
    BuildUtil.editSupplies(player, chunk, item.getType(), amount);
    item.setAmount(item.getAmount() - amount);
  }

  public static void upgradeStructure(Player player, String nextBlueprintName) {
    Chunk chunk = player.getLocation().getChunk();
    BuildUtil.undoBuilding(chunk);
    BuildUtil.setBlueprintName(chunk, player, nextBlueprintName);
    BuildUtil.generateBuildingFromBlueprint(player, nextBlueprintName);
  }

  public static void sendChunkInfo(Player player, Chunk chunk) {
    String header = "Chunk X" + chunk.getX() + ":Y" + chunk.getZ() + " Information.";

    String blueprintName = BuildUtil.getStructureBlueprintName(chunk);
    String structureOwner = BuildUtil.getStructureOwner(chunk);
    String ownerName = GeneralUtil.getPlayerNameFromStringUuid(structureOwner);

    List<String> points = new ArrayList<String>();

    points.add("Structure Blueprint: " + (blueprintName != null ? blueprintName : "N/A"));
    points.add("Structure Owner: " + (ownerName != null ? ownerName : "N/A"));

    ChatUtil.sendPlayerListedMessage(player, header, points);
  }

  public static void sendBuildCost(Player player, String blueprintName){
    ConfigurationSection resourceSection = BlueprintUtil.getBlueprintCosts(blueprintName);
    String header =  blueprintName + " Resource Cost";

    List<String> points = new ArrayList<String>();

    if (resourceSection == null) {
        points.add("No costs required.");
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

  public static HashMap<String, String> getBuildCommands() {
    HashMap<String, String> commands = new HashMap<>();
    commands.put("/build delete", "Deletes any structure within the chunk you are in.");
    commands.put("/build new {blueprintName}", "Generates a structure in the chunk you are in.");
    commands.put("/build refresh", "Deletes then builds the structure of the chunk you are in.");
    commands.put("/build info", "Sends you information about the chunk you are in.");
    commands.put("/build plot",
        "Shows you where the bottom level of the structure will begin to generate to allow you to terraform.");
    commands.put("/build upgrade", "Upgrades your structure to the next level.");
    commands.put("/build undo", "Undoes building and restores the land to previous.");
    commands.put("/build cost {blueprintName}", "Gets the resource cost for a build.");
    commands.put("/build help", "The command you're looking at right now.");
    return commands;
  }
}