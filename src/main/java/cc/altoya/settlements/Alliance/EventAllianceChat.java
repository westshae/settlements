package cc.altoya.settlements.Alliance;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class EventAllianceChat implements Listener {
    @EventHandler
    public void playerChatEvent(AsyncChatEvent event) {
        if(!AllianceUtil.isChatEnabled(event.getPlayer())){
            return;
        }
        String allianceName = AllianceUtil.getPlayerAllianceName(event.getPlayer());
        String plainTextMessage = PlainTextComponentSerializer.plainText().serialize(event.message());

        AllianceUtil.sendMessage(allianceName, plainTextMessage);
        event.setCancelled(true);

    }
}
