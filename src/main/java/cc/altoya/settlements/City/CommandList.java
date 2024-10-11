package cc.altoya.settlements.City;

import java.util.List;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandList {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }
        sendPlayerOwnedChunks(sender);
        return true;
    }

    private static void sendPlayerOwnedChunks(Player player){
        List<String> chunks = CityUtil.getPlayersChunks(player);
        if(chunks.size() == 0){
            ChatUtil.sendErrorMessage(player, "No claimed chunks. Try using /city claim");
            return;
        }
        ChatUtil.sendSuccessMessage(player, "Claimed Chunks:");
        chunks.forEach((chunk) -> ChatUtil.sendSuccessMessage(player, " - " + chunk));
    }
}
