package cc.altoya.settlements.Alliance;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Domain.DomainUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandJoin {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2,
                "/alliance join {alliance}")) {
            return true;
        }
        joinAlliance(sender, args[1]);
        return true;
    }

    private static void joinAlliance(Player player, String allianceName) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        if (AllianceUtil.isPlayerInAlliance(player)) {
            ChatUtil.sendErrorMessage(player, "You are already in an alliance.");
            return;
        }
        if (!AllianceUtil.doesAllianceExist(allianceName)) {
            ChatUtil.sendErrorMessage(player, "This alliance doesn't exist.");
            return;
        }
        if (!AllianceUtil.doesPlayerHaveInvite(player, allianceName)) {
            ChatUtil.sendErrorMessage(player, "You don't have an invite.");
            return;
        }
        List<String> allianceInvites = GeneralUtil
                .createListFromString((String) allianceConfig.get("alliances." + allianceName + ".invites"));
        allianceInvites.removeIf((playerUuid) -> playerUuid.equals(GeneralUtil.getKeyFromPlayer(player)));
        allianceConfig.set("alliances." + allianceName + ".invites", GeneralUtil.createStringFromList(allianceInvites));

        List<String> alliancePlayers = GeneralUtil
                .createListFromString((String) allianceConfig.get("alliances." + allianceName + ".players"));

        ChatUtil.sendSuccessMessage(player, "You've joined the alliance.");
        alliancePlayers.forEach((playerUuid) -> {
            ChatUtil.sendSuccessMessage(Bukkit.getPlayer(UUID.fromString(playerUuid)),
                    player.getDisplayName() + " has joined the alliance.");
        });

        alliancePlayers.add(GeneralUtil.getKeyFromPlayer(player));
        allianceConfig.set("alliances." + allianceName + ".players", GeneralUtil.createStringFromList(alliancePlayers));

        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", allianceName);

        AllianceUtil.saveAllianceConfig(allianceConfig);
        DomainUtil.saveDomainConfig(domainConfig);
    }

}
