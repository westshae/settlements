package cc.altoya.settlements.Events;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.StructureUtil;

public class EventStructureInteract implements Listener {
    @EventHandler
    public void onButtonPress(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();

        if(block == null){
            return;
        }

        if(StructureUtil.isBlockStructureBlock(block)){
            ChatUtil.sendSuccessMessage(event.getPlayer(), "You clicked the block");
        }
    }
}
