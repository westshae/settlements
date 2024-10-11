package cc.altoya.settlements.Build;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import cc.altoya.settlements.Util.ChatUtil;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class EventEnterStructureChunk implements Listener {
    @EventHandler
    public void onBreakInStructureChunk(PlayerMoveEvent event) {
      Block to = event.getTo().getBlock();
      Block from = event.getFrom().getBlock();
      if(to.getChunk().equals(from.getChunk())){
        return;
      }
      if(BuildUtil.isChunkStructure(to.getChunk())){
        String blueprintName = BuildUtil.getStructureBlueprintName(to.getChunk());
        String ownerUuidString = BuildUtil.getStructureOwner(to.getChunk());
        Player owner = Bukkit.getServer().getPlayer(UUID.fromString(ownerUuidString));
        String ownerDisplayName = PlainTextComponentSerializer.plainText().serialize(owner.displayName());
        ChatUtil.sendSuccessBar(event.getPlayer(), "You have entered " + ownerDisplayName + "'s " + blueprintName + " structure.");
      }
    }
}
