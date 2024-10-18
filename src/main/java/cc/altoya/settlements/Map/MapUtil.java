package cc.altoya.settlements.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import java.awt.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapFont;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.map.MinecraftFont;

import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Util.GeneralUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MapUtil {
  public static FileConfiguration getMapConfig() {
    return GeneralUtil.getPluginConfig("settlements", "maps.yml");
  }

  public static void saveMapConfig(FileConfiguration config) {
    File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(), "maps.yml");
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static HashMap<String, String> getMapCommands() {
    HashMap<String, String> commands = new HashMap<>();
    commands.put("/map help", "The command you're looking at right now.");
    return commands;
  }

  public static void updateMap(ItemStack mapItem, boolean mapRender) {
    MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
    if (mapMeta != null) {
      MapView mapView = mapMeta.getMapView();
      mapView.setScale(Scale.FARTHEST);
      mapView.getRenderers().clear(); // Clear existing renderers

      // Add a new renderer to draw the chunks
      mapView.addRenderer(new MapRenderer() {
        @Override
        public void render(MapView view, MapCanvas canvas, Player player) {
          drawMap(view, canvas, player, mapRender);
        }
      });

      mapItem.setItemMeta(mapMeta);
    }
  }

  public static void drawMap(MapView view, MapCanvas canvas, Player player, boolean mapRender) {
    int sectionSize = 8; // pixels height/width per "square"
    int sectionCount = 128 / sectionSize; // how many sections in the width/height
    int playerSectionX = player.getChunk().getX() % sectionCount;
    int playerSectionZ = player.getChunk().getZ() % sectionCount;
    Chunk zerodChunk = player.getChunk().getWorld().getChunkAt(player.getChunk().getX() - playerSectionX,
        player.getChunk().getZ() - playerSectionZ);

    if (mapRender) {
      for (int sx = 0; sx < sectionCount; sx++) {
        for (int sz = 0; sz < sectionCount; sz++) {
          Chunk current = player.getChunk().getWorld().getChunkAt(zerodChunk.getX() + sx, zerodChunk.getZ() + sz);
          if (playerSectionX == sx && playerSectionZ == sz) {
            drawBlankBorderedColour(canvas, sectionSize, sx, sz, Color.DARK_GRAY);
          } else if (CityUtil.isChunkClaimed(current)) {
            drawBlankBorderedColour(canvas, sectionSize, sx, sz, Color.RED);
          } else {
            drawBlankBorderedColour(canvas, sectionSize, sx, sz, Color.WHITE);
          }
        }
      }
    }
  }

  public static void drawBlankBorderedColour(MapCanvas canvas, int sectionSize, int sectionX, int sectionY, Color color) {
    for (int x = 0; x < sectionSize; x++) {
      for (int z = 0; z < sectionSize; z++) {
          canvas.setPixelColor((sectionSize * sectionX) + x, (sectionSize * sectionY) + z, color);
      }
    }
  }

  public static void givePlayerCityMap(Player player) {
    ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
    MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();

    if (mapMeta != null) {
      MapView mapView = Bukkit.createMap(Bukkit.getWorlds().get(0));
      mapView.getRenderers().clear();
      mapMeta.setMapView(mapView);
      mapItem.setItemMeta(mapMeta);
      MapUtil.updateMap(mapItem, true);
    }
    player.getInventory().addItem(mapItem);
  }
}
