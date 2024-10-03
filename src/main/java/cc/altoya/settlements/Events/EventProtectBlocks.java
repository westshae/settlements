package cc.altoya.settlements.Events;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DomainUtil;

public class EventProtectBlocks implements Listener {
    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {
        Chunk chunk = event.getBlock().getLocation().getChunk();
        Player player = event.getPlayer();
        if (!DomainUtil.isChunkClaimed(chunk)) {
            return;
        }
        if (!DomainUtil.doesPlayerOwnChunk(player, chunk)) {
            ChatUtil.sendErrorMessage(player, "This chunk is claimed, and you don't have access.");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent event) {
        Chunk chunk = event.getBlock().getLocation().getChunk();
        Player player = event.getPlayer();
        if (!DomainUtil.isChunkClaimed(chunk)) {
            return;
        }
        if (!DomainUtil.doesPlayerOwnChunk(player, chunk)) {
            ChatUtil.sendErrorMessage(player, "This chunk is claimed, and you don't have access.");
            event.setCancelled(true);
            return;
        }

    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null){
            return;
        }
        Chunk chunk = event.getClickedBlock().getChunk();
        Player player = event.getPlayer();
        if(chunk == null){
            return;
        }
        if (!DomainUtil.isChunkClaimed(chunk)) {
            return;
        }
        if (!DomainUtil.doesPlayerOwnChunk(player, chunk)) {
            ChatUtil.sendErrorMessage(player, "This chunk is claimed, and you don't have access.");
            event.setCancelled(true);
            return;
        }
    }
}