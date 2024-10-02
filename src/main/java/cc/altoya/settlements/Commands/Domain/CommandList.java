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
        List<String> chunks = DomainUtil.getPlayersChunks(sender);
        if(chunks.size() == 0){
            ChatUtil.sendErrorMessage(sender, "No claimed chunks. Try using /domain claim");
            return true;
        }
        ChatUtil.sendSuccessMessage(sender, "Claimed Chunks:");
        chunks.forEach((chunk) -> ChatUtil.sendSuccessMessage(sender, " - " + chunk));
        return true;
    }
}
