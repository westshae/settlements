package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandInfo {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }

        sendInfo(sender, sender.getLocation().getChunk());
        return true;
    }

    private static void sendInfo(Player player, Chunk chunk) {
      if(!CityUtil.isChunkClaimed(chunk)){
        ChatUtil.sendErrorMessage(player, "Chunk isn't claimed.");
        return;
      }

      BuildUtil.sendChunkInfo(player, chunk);
    }

}
