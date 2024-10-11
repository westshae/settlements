package cc.altoya.settlements.Alliance;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import cc.altoya.settlements.City.CityUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class EventAllianceChat implements Listener {
    @EventHandler
    public void playerChatEvent(AsyncChatEvent event) {
        if(!CityUtil.isChatEnabled(event.getPlayer())){
            return;
        }
        String allianceName = CityUtil.getPlayerAllianceName(event.getPlayer());
        String plainTextMessage = PlainTextComponentSerializer.plainText().serialize(event.message());

        AllianceUtil.sendMessage(allianceName, plainTextMessage);
        event.setCancelled(true);

    }
}
