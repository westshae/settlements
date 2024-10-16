package cc.altoya.settlements.City;

import java.util.List;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class ScheduledCollection {
  public static Runnable getRunnable() {
    return new BukkitRunnable() {
      FileConfiguration config = CityUtil.getCityConfig();

      List<String> cityKeys = (config.getConfigurationSection("cities") != null)
          ? new ArrayList<>(config.getConfigurationSection("cities").getKeys(false))
          : null;

      int index = 0;

      @Override
      public void run() {
        if (cityKeys == null) {
          cityKeys = (config.getConfigurationSection("cities") != null)
              ? new ArrayList<>(config.getConfigurationSection("cities").getKeys(false))
              : null;
          this.runTaskLater(GeneralUtil.getPlugin(), 1000L);
        }
        String cityKey = cityKeys.get(index); // Get the UUID key
        ConfigurationSection citySection = config.getConfigurationSection("cities." + cityKey);


        FileConfiguration config = CityUtil.getCityConfig();

        HashMap<String, Integer> structureCountMap = new HashMap<>();

        ConfigurationSection structuresSection = citySection.getConfigurationSection("structures");
        structuresSection.getKeys(false).forEach(structureKey -> {

          String materialString = structuresSection.getString(structureKey + ".material");

          if (structureCountMap.containsKey(materialString)) {
            structureCountMap.put(materialString, structureCountMap.get(materialString) + 1);
          } else {
            structureCountMap.put(materialString, 1);
          }
        });

        int structureTotalCount = structuresSection.getKeys(false).size();

        for (Entry<String, Integer> entry : structureCountMap.entrySet()) {
          Player player = Bukkit.getPlayer(UUID.fromString(cityKey));
          Integer workerCount = CityUtil.getWorkers(player);

          double workerRatio = (double) workerCount / structureTotalCount;
          if(workerRatio > 1.5){
            workerRatio = 1.5;
          }
          double resourcesCollected = entry.getValue() * workerRatio;

          Material material = Material.getMaterial(entry.getKey());
          CityUtil.editCityResources(player, material, resourcesCollected);
          ChatUtil.sendSuccessMessage(player,
              "City structures collected " + resourcesCollected + " " + material.toString());
        }

        index++;

        if (index == cityKeys.size()) {
          cityKeys = new ArrayList<String>(config.getConfigurationSection("cities").getKeys(false));
          index = 0;
          this.runTaskLater(GeneralUtil.getPlugin(), 1000L);

        } else {
          this.runTaskLater(GeneralUtil.getPlugin(), 20L);
        }
      }
    };
  }
}
