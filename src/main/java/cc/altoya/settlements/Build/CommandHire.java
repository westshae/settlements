package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandHire {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }

        hireWorker(sender);
        return true;
    }

    private static void hireWorker(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        if(!BuildUtil.isChunkStructure(chunk)){
            ChatUtil.sendErrorMessage(player, "There is no structure in this chunk.");
            return;
        }   
        if(!BuildUtil.hasHousingRoom(chunk)){
            ChatUtil.sendErrorMessage(player, "You are out of housing.");
            return;
        }

        BuildUtil.hireWorker(player, chunk);

        ChatUtil.sendSuccessMessage(player, "A new worker has been hired.");
    }

}
