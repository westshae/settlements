package cc.altoya.settlements.Alliance;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Domain.DomainUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandLeave {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }
        leaveAlliance(sender);
        return true;
    }

    private static void leaveAlliance(Player player) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        if (!AllianceUtil.isPlayerInAlliance(player)) {
            ChatUtil.sendErrorMessage(player, "You aren't in an alliance.");
            return;
        }
        String playerAllianceName = AllianceUtil.getPlayerAlliance(player);

        List<String> alliancePlayers = allianceConfig.getStringList("alliances." + playerAllianceName + ".players");
        alliancePlayers.removeIf((playerUuid) -> playerUuid.equals(GeneralUtil.getKeyFromPlayer(player)));
        allianceConfig.set("alliances." + playerAllianceName + ".players", alliancePlayers);

        ChatUtil.sendSuccessMessage(player, "You've left the alliance.");
        alliancePlayers.forEach((playerUuid) -> {
            ChatUtil.sendSuccessMessage(Bukkit.getPlayer(UUID.fromString(playerUuid)),
                    player.displayName() + " has left the alliance.");
        });
        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", null);

        AllianceUtil.saveAllianceConfig(allianceConfig);
        DomainUtil.saveDomainConfig(domainConfig);
    }

}
