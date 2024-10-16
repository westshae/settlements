package cc.altoya.settlements.Build;

import java.util.List;
import java.util.Arrays;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class EventBreakResourceBlock implements Listener {
    @EventHandler
    public void onBreakInStructureChunk(BlockBreakEvent event) {
      Chunk chunk = event.getBlock().getChunk();
      if(!BuildUtil.isChunkStructure(chunk)){
        return;
      }
      List<Material> resourceMaterials = Arrays.asList(Material.COAL_ORE, Material.IRON_ORE, Material.WHEAT, Material.SUGAR_CANE, Material.OAK_LOG);
      Material material = event.getBlock().getType();
      if(resourceMaterials.contains(material)){
        new BukkitRunnable() {
          @Override
          public void run() {
            event.getBlock().setType(material);
          }
        }.runTaskLater(GeneralUtil.getPlugin(), 100L);
        event.getBlock().setType(Material.AIR);
        event.setCancelled(true);
        CityUtil.editCityResources(event.getPlayer(), material, 1);
        ChatUtil.sendSuccessBar(event.getPlayer(), "1 " + material.toString() + " collected.");
        return;
      }
      event.setCancelled(true);
    }
}
