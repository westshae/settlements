package cc.altoya.settlements.Events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.ClaimUtil;

public class EventProtectBlocks implements Listener{
  @EventHandler
  public void blockBreakEvent(BlockBreakEvent event){
    if(!ClaimUtil.isPlayerTrusted(event.getPlayer(), event.getBlock().getLocation())){
      event.setCancelled(true);
      ChatUtil.sendErrorMessage(event.getPlayer(), "You are not trusted within this claim!");
      return;
    }
  }

  @EventHandler
  public void blockPlaceEvent(BlockPlaceEvent event){
    if(!ClaimUtil.isPlayerTrusted(event.getPlayer(), event.getBlock().getLocation())){
      event.setCancelled(true);
      ChatUtil.sendErrorMessage(event.getPlayer(), "You are not trusted within this claim!");
      return;
    }
  }

  @EventHandler
  public void playerInteractEvent(PlayerInteractEvent event){
    Location location = event.getInteractionPoint();
    if(location == null){
      return;
    }
    if(!ClaimUtil.isPlayerTrusted(event.getPlayer(), location)){
      event.setCancelled(true);
      ChatUtil.sendErrorMessage(event.getPlayer(), "You are not trusted within this claim!");
      return;
    }
  }
}
