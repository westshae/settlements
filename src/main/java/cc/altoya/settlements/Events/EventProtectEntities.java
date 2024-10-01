package cc.altoya.settlements.Events;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.ClaimUtil;

public class EventProtectEntities implements Listener {
  @EventHandler
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    Location location = event.getRightClicked().getLocation();
    if (!ClaimUtil.isLocationClaimed(location)) {
      return;
    } else {
      if (!ClaimUtil.isPlayerTrusted(event.getPlayer(), location)) {
        event.setCancelled(true);
        ChatUtil.sendErrorMessage(event.getPlayer(), "You are not trusted within this claim!");
        return;
      }
    }
  }

  @EventHandler
  public void onEntityDamage(EntityDamageByEntityEvent event) {
    Location location = event.getEntity().getLocation();
    if(event.getDamager().getType() != EntityType.PLAYER){
      return;
    }

    if (!ClaimUtil.isLocationClaimed(location)) {
      return;
    } else {
      if(event.getEntity().getType() == EntityType.PLAYER){
        event.setCancelled(true);
        ChatUtil.sendErrorMessage((Player) event.getDamager(), "You cannot PVP within claimed land!");
        return;
      }
      if (!ClaimUtil.isPlayerTrusted((Player) event.getDamager() , location)) {
        event.setCancelled(true);
        ChatUtil.sendErrorMessage((Player) event.getDamager(), "You are not trusted within this claim!");
        return;
      }
    }

  }
}
