package cc.altoya.settlements.Commands.Domain;

import java.util.List;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DomainUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandList {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/domain list")) {
            return true;
        }
        sendPlayerOwnedChunks(sender);
        return true;
    }

    private static void sendPlayerOwnedChunks(Player player){
        List<String> chunks = DomainUtil.getPlayersChunks(player);
        if(chunks.size() == 0){
            ChatUtil.sendErrorMessage(player, "No claimed chunks. Try using /domain claim");
            return;
        }
        ChatUtil.sendSuccessMessage(player, "Claimed Chunks:");
        chunks.forEach((chunk) -> ChatUtil.sendSuccessMessage(player, " - " + chunk));
    }
}
