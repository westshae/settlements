package cc.altoya.settlements.City;


import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandClaim {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }
        addPlayerChunk(sender);
        return true;
    }

    public static void addPlayerChunk(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        if (CityUtil.isChunkClaimed(chunk)) {
            ChatUtil.sendErrorMessage(player, "This chunk is already claimed.");
            return;
        }
        CityUtil.claimChunk(player, chunk);
        ChatUtil.sendSuccessMessage(player, "Chunk claimed");
    }

}
