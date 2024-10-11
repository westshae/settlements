package cc.altoya.settlements.City;

import org.bukkit.Material;
import org.bukkit.entity.Enderman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EventGriefPrevention implements Listener {
  @EventHandler
  public void onEntityChangeBlock(EntityChangeBlockEvent event) {
    if (!CityUtil.isChunkClaimed(event.getBlock().getChunk())) {
      return;
    }

    if (event.getEntity() instanceof Enderman) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockForm(BlockFormEvent event) {
    if (!CityUtil.isChunkClaimed(event.getBlock().getChunk())) {
      return;
    }

    if (event.getNewState().getType() == Material.SNOW) {
      event.setCancelled(true);
    }
  }
}