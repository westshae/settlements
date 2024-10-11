package cc.altoya.settlements.Alliance;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import cc.altoya.settlements.Util.ChatUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class EventAllianceChat implements Listener {
    @EventHandler
    public void playerChatEvent(AsyncChatEvent event) {
        if(!AllianceUtil.isAllianceChatEnabled(event.getPlayer())){
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
            String plainTextMessage = PlainTextComponentSerializer.plainText().serialize(event.message());
            ChatUtil.sendAllianceMessage(player, allianceName, plainTextMessage);
        });
        event.setCancelled(true);

    }
}
