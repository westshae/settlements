package cc.altoya.settlements.Alliance;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.AllianceUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DomainUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandDelete {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/alliance delete")) {
            return true;
        }

        deleteAlliance(sender);
        return true;
    }

    private static void deleteAlliance(Player player) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();
        if (!AllianceUtil.isPlayerInAlliance(player)) {
            ChatUtil.sendErrorMessage(player, "You aren't in an alliance.");
            return;
        }
        if (!AllianceUtil.isPlayerOwner(player)) {
            ChatUtil.sendErrorMessage(player, "You aren't the leader of this alliance.");
            return;
        }
        String allianceName = AllianceUtil.getPlayerAlliance(player);
        allianceConfig.set("alliances." + allianceName, null);
        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", null);
        AllianceUtil.saveAllianceConfig(allianceConfig);
        DomainUtil.saveDomainConfig(domainConfig);
        ChatUtil.sendSuccessMessage(player, "Alliance has been deleted.");
    }

}
