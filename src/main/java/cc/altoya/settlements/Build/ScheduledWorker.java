package cc.altoya.settlements.Build;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Random;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class ScheduledWorker {
  public static Runnable getRunnable() {
    return new Runnable() {
      @Override
      public void run() {
        FileConfiguration buildsConfig = BuildUtil.getBuildConfig();
        FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();

        ConfigurationSection section = buildsConfig.getConfigurationSection("builds");
        List<String> chunkKeys = new ArrayList<>(section.getKeys(false));

        for (String key : chunkKeys) {
          Chunk chunk = GeneralUtil.getChunkFromKey(key);
          Integer wheatAmount = BuildUtil.getSuppliesFromStructure(chunk, Material.WHEAT);

          if(wheatAmount <= 0){
            continue;
          }

          String ownerUuid = buildsConfig.getString("builds." + key + ".owner");
          Player player = Bukkit.getPlayer(UUID.fromString(ownerUuid));

          Location playerChunk = player.getLocation();
          int distance = (int) playerChunk.distance(chunk.getBlock(0, player.getLocation().getBlockY(), 0).getLocation());          

          boolean isNearbyAndOnline = player.isOnline() && distance < 50;
          String blueprintName = BuildUtil.getStructureBlueprintName(chunk);

          List<String> resourceBlocks = blueprintConfig
              .getStringList("blueprints." + blueprintName + ".resourceBlocks");

          if (resourceBlocks.size() == 0) {
            continue;
          }

          if(new Random().nextInt(100) < 50){
            BuildUtil.editSupplies(player, chunk, Material.WHEAT, -1);
          }


          String randomResourceBlockKey = resourceBlocks.get(new Random().nextInt(resourceBlocks.size()));

          Block randomBlock = BlueprintUtil.turnStringIntoBlock(randomResourceBlockKey);

          String originBlockKey = buildsConfig.getString("builds." + key + ".first");

          Block originBlock = BlueprintUtil.turnStringIntoBlock(originBlockKey);

          Location nonRelativeRandomBlock = BlueprintUtil.getNonRelativeLocation(originBlock,
              randomBlock.getLocation());

          if (isNearbyAndOnline) {

            Villager villager = (Villager) nonRelativeRandomBlock.getWorld().spawnEntity(
                originBlock.getLocation(),
                EntityType.VILLAGER);
            villager.setInvulnerable(true);
            new BukkitRunnable() {
              @Override
              public void run() {
                villager.getPathfinder().moveTo(nonRelativeRandomBlock);
              }
            }.runTaskTimer(GeneralUtil.getPlugin(), 1, 20);
            new BukkitRunnable() {
              @Override
              public void run() {
                villager.remove();
              }
            }.runTaskLater(GeneralUtil.getPlugin(), 100);
          }

          new BukkitRunnable() {

            @Override
            public void run() {
              cancel();
              BlockBreakEvent breakEvent = new BlockBreakEvent(nonRelativeRandomBlock.getBlock(),
                  player);
              PlayerInteractEvent interactEvent = new PlayerInteractEvent(player,
                  Action.RIGHT_CLICK_BLOCK, null, nonRelativeRandomBlock.getBlock(), null);
              breakEvent.getBlock().setMetadata("serverCalled",
                  new FixedMetadataValue(GeneralUtil.getPlugin(), true));
              breakEvent.getBlock().setMetadata("serverCalled",
                  new FixedMetadataValue(GeneralUtil.getPlugin(), true));

              Bukkit.getPluginManager().callEvent(breakEvent);
              Bukkit.getPluginManager().callEvent(interactEvent);

              return;

            }
          }.runTaskLater(GeneralUtil.getPlugin(), 100);

        }
      }
    };
  }
}
