package cc.altoya.settlements.Events;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.StructureUtil;

public class EventStructureInteract implements Listener {
    @EventHandler
    public void onInteractWithStructureChunk(BlockBreakEvent event) {
        Block block = event.getBlock();

        if(!StructureUtil.isChunkStructure(block.getChunk())){
            return;
        }

        if(!StructureUtil.isBlockInteractiveBlock(block)){
            ChatUtil.sendErrorMessage(event.getPlayer(), "Cannot break blocks within a structure chunk.");
            event.setCancelled(true);
            return;
        } else {
            event.setCancelled(true);
            StructureUtil.editResources(event.getPlayer(), block.getChunk(), 1);
        }
    }
}
