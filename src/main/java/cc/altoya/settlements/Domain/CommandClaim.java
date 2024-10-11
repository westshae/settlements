package cc.altoya.settlements.Domain;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandClaim {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }
        addPlayerChunk(sender, sender.getLocation().getChunk());
        return true;
    }

    public static void addPlayerChunk(Player player, Chunk chunk) {
        FileConfiguration config = DomainUtil.getDomainConfig();
        if (DomainUtil.isChunkClaimed(chunk)) {
            ChatUtil.sendErrorMessage(player, "This chunk is already claimed.");
            return;
        }
        config.set("domains.claimed_tiles." + GeneralUtil.getKeyFromChunk(chunk), GeneralUtil.getKeyFromPlayer(player));
        String playerPath = "domains." + GeneralUtil.getKeyFromPlayer(player) + ".claims";
        List<String> claims = config.getStringList(playerPath);
        claims.add(GeneralUtil.getKeyFromChunk(chunk));
        config.set(playerPath, claims);
        DomainUtil.saveDomainConfig(config);
        ChatUtil.sendSuccessMessage(player, "Chunk claimed");
    }

}
