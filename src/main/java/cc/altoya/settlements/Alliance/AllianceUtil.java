package cc.altoya.settlements.Alliance;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Domain.DomainUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class AllianceUtil {
    public static FileConfiguration getAllianceConfig() {
        return GeneralUtil.getPluginConfig("settlements", "alliances.yml");
    }

    public static void saveAllianceConfig(FileConfiguration config) {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(),
                "alliances.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isPlayerInAlliance(Player player) {
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();
        return domainConfig.contains("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance");
    }

    public static boolean doesAllianceExist(String name) {
        FileConfiguration allianceConfig = getAllianceConfig();
        return allianceConfig.contains("alliances." + name);
    }

    public static String getPlayerAlliance(Player player) {
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();
        return domainConfig.getString("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance");
    }

    public static boolean isPlayerOwner(Player player) {
        FileConfiguration allianceConfig = getAllianceConfig();
        String allianceName = getPlayerAlliance(player);
        String allianceOwnerUuid = allianceConfig.getString("alliances." + allianceName + ".owner");
        return GeneralUtil.getKeyFromPlayer(player).equals(allianceOwnerUuid);
    }

    public static boolean doesPlayerHaveInvite(Player player, String allianceName) {
        FileConfiguration allianceConfig = getAllianceConfig();
        List<String> invites = GeneralUtil
                .createListFromString((String) allianceConfig.get("alliances." + allianceName + ".invites"));
        return invites.contains(GeneralUtil.getKeyFromPlayer(player));
    }

    public static List<String> getAlliancePlayers(String allianceName) {
        FileConfiguration allianceConfig = getAllianceConfig();

        return GeneralUtil.createListFromString((String) allianceConfig.get("alliances." + allianceName + ".players"));
    }
}
