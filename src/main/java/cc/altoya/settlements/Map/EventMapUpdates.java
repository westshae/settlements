package cc.altoya.settlements.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Util.GeneralUtil;

public class EventMapUpdates implements Listener {
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    new BukkitRunnable() {
      @Override
      public void run() {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.FILLED_MAP && MapUtil.isMapCityMap(itemInHand)) {
          MapUtil.updateMap(itemInHand, !event.getFrom().getChunk().equals(event.getTo().getChunk()));
        }
      }
    }.runTask(GeneralUtil.getPlugin());
  }
}
