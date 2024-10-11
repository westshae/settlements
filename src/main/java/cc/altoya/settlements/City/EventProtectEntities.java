package cc.altoya.settlements.City;

import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import cc.altoya.settlements.Util.ChatUtil;

public class EventProtectEntities implements Listener {
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Chunk chunk = event.getRightClicked().getLocation().getChunk();
        Player player = event.getPlayer();
        if (!CityUtil.isChunkClaimed(chunk)) {
            return;
        }
        if (!CityUtil.doesPlayerOwnChunk(player, chunk)) {
            ChatUtil.sendErrorMessage(player, "This chunk is claimed, and you don't have access.");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Chunk chunk = event.getEntity().getLocation().getChunk();
        if (event.getDamager().getType() != EntityType.PLAYER) {
            return;
        }


        if (!CityUtil.isChunkClaimed(chunk)) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (event.getDamager().getType() == EntityType.PLAYER) {
            ChatUtil.sendErrorMessage(player, "No pvp is enabled within claimed chunks.");
            event.setCancelled(true);
            return;
        }
        if (!CityUtil.doesPlayerOwnChunk(player, chunk)) {
            ChatUtil.sendErrorMessage(player, "This chunk is claimed, and you don't have access.");
            event.setCancelled(true);
            return;
        }
    }
}