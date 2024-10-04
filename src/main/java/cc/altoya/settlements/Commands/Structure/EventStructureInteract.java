package cc.altoya.settlements.Commands.Structure;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.StructureUtil;

public class EventStructureInteract implements Listener {
    @EventHandler
    public void onBreakInMineStructureChunk(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (!StructureUtil.isChunkStructure(block.getChunk())) {
            return;
        }

        if (!StructureUtil.isBlockInteractiveBlock(block)) {
            ChatUtil.sendErrorMessage(player, "Cannot break blocks within a structure chunk.");
            event.setCancelled(true);
            return;
        }
        if(!StructureUtil.getStructureType(block.getChunk()).equals("mine")){
            event.setCancelled(true);
            return;
        }
        Material currentMaterial = block.getType();
        if(currentMaterial == Material.STONE){
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        event.getPlayer().playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);
        StructureUtil.editResources(player, block.getChunk(), 1);
        block.setType(Material.STONE);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(currentMaterial);
            }
        }.runTaskLater(GeneralUtil.getPlugin(), 100L);

    }
}
