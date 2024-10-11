package cc.altoya.settlements.Build;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class ScheduledCleanUp {
  public static Runnable getRunnable() {
    return new Runnable() {
      @Override
      public void run() {
        FileConfiguration buildsConfig = BuildUtil.getBuildConfig();

        ConfigurationSection section = buildsConfig.getConfigurationSection("builds");
        List<String> chunkKeys = new ArrayList<>(section.getKeys(false));

        for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
          if (entity instanceof Villager && entity.isInvulnerable()) {
            entity.remove();
          }
        }

        for (String key : chunkKeys) {
          String blockString = buildsConfig.getString("builds." + key + ".first");
          Block block = BlueprintUtil.turnStringIntoBlock(blockString);
          Chunk chunk = block.getLocation().getChunk();

          String blueprintName = BuildUtil.getStructureBlueprintName(chunk);

          if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            return;
          }

          FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();

          String buildFirstKey = buildsConfig.getString("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".first");
          Block buildFirstBlock = BlueprintUtil.turnStringIntoBlock(buildFirstKey);

          List<String> resourceBlocks = blueprintConfig
              .getStringList("blueprints." + blueprintName + ".resourceBlocks");

          BuildUtil.placeBlocksFromStringList(resourceBlocks, buildFirstBlock);
        }
      }
    };
  }
}