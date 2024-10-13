package cc.altoya.settlements.Alliance;

import java.util.List;
import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Util.ChatUtil;

public class EventAllianceOnJoin implements Listener {
    @EventHandler
    public void playerChatEvent(PlayerJoinEvent event) {
      List<String> alliances = List.of("red", "blue", "green", "yellow");
      
      if(!CityUtil.isPlayerMember(event.getPlayer())){
        int randomIndex = new Random().nextInt(alliances.size());
        AllianceUtil.addMember(event.getPlayer(), alliances.get(randomIndex));
        ChatUtil.sendAllPlayersAllianceJoin(event.getPlayer(), alliances.get(randomIndex));
      }
    }
}
