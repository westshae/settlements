package cc.altoya.settlements.Alliance;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import cc.altoya.settlements.Domain.DomainUtil;
import cc.altoya.settlements.Util.ChatUtil;
import io.papermc.paper.event.player.AsyncChatEvent;

public class EventAllianceChat implements Listener {
    @EventHandler
    public void playerChatEvent(AsyncChatEvent event) {
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
            ChatUtil.sendAllianceMessage(player, allianceName, event.message().toString());
        });
        event.setCancelled(true);

    }
}
