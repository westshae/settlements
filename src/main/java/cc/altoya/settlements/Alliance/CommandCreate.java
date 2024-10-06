package cc.altoya.settlements.Alliance;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Domain.DomainUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

import java.util.List;
import java.util.ArrayList;

public class CommandCreate {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2,
                "/alliance create {name}")) {
            return true;
        }

        createAlliance(sender, args[1]);
        return true;
    }

    private static void createAlliance(Player player, String allianceName) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();
        if (AllianceUtil.isPlayerInAlliance(player)) {
            ChatUtil.sendErrorMessage(player, "You are already in an alliance.");
            return;
        }
        if (AllianceUtil.doesAllianceExist(allianceName)) {
            ChatUtil.sendErrorMessage(player, "This alliance already exists.");
            return;
        }
        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", allianceName);
        allianceConfig.set("alliances." + allianceName + ".owner", GeneralUtil.getKeyFromPlayer(player));
        List<String> alliancePlayers = new ArrayList<>();
        alliancePlayers.add(GeneralUtil.getKeyFromPlayer(player));
        allianceConfig.set("alliances." + allianceName + ".players", GeneralUtil.createStringFromList(alliancePlayers));

        DomainUtil.saveDomainConfig(domainConfig);
        AllianceUtil.saveAllianceConfig(allianceConfig);
        ChatUtil.sendSuccessMessage(player, "Alliance successfully created.");
        return;
    }

}
