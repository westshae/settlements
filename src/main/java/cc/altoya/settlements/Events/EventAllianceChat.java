package cc.altoya.settlements.Events;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import cc.altoya.settlements.Util.AllianceUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DomainUtil;

public class EventAllianceChat implements Listener {
    @EventHandler
    public void playerChatEvent(AsyncPlayerChatEvent event) {
        if(!DomainUtil.isAllianceChatMode(event.getPlayer())){
            return;
        }
        String allianceName = AllianceUtil.getPlayerAlliance(event.getPlayer());
        List<String> alliancePlayers = AllianceUtil.getAlliancePlayers(allianceName);
        alliancePlayers.forEach((playerUuidString) -> {
            UUID uuid = UUID.fromString(playerUuidString);
            Player player = Bukkit.getPlayer(uuid);
            if(!player.isOnline()){
                return;
            }
            ChatUtil.sendAllianceMessage(player, allianceName, event.getMessage());
        });
        event.setCancelled(true);

    }
}
