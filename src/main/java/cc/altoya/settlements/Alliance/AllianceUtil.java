package cc.altoya.settlements.Alliance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    public static boolean isAllianceChatEnabled(Player player) {
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        return domainConfig.getBoolean("domains." + player.getUniqueId().toString() + ".allianceChat");
    }

    public static void setAllianceChatEnabled(Player player, boolean toggle) {
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".allianceChat",
                !isAllianceChatEnabled(player));
        DomainUtil.saveDomainConfig(domainConfig);
    }

    public static boolean doesPlayerHaveInvite(Player player, String allianceName) {
        FileConfiguration allianceConfig = getAllianceConfig();
        List<String> invites = allianceConfig.getStringList("alliances." + allianceName + ".invites");
        return invites.contains(GeneralUtil.getKeyFromPlayer(player));
    }

    public static List<String> getAlliancePlayers(String allianceName) {
        FileConfiguration allianceConfig = getAllianceConfig();

        return allianceConfig.getStringList("alliances." + allianceName + ".players");
    }

    public static void createAlliance(Player player, String allianceName) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", allianceName);
        allianceConfig.set("alliances." + allianceName + ".owner", GeneralUtil.getKeyFromPlayer(player));
        List<String> alliancePlayers = new ArrayList<>();
        alliancePlayers.add(GeneralUtil.getKeyFromPlayer(player));
        allianceConfig.set("alliances." + allianceName + ".players", alliancePlayers);
        allianceConfig.set("alliances." + allianceName + ".allianceChat", false);

        DomainUtil.saveDomainConfig(domainConfig);
        AllianceUtil.saveAllianceConfig(allianceConfig);
    }

    public static void deleteAlliance(Player player){
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        allianceConfig.set("alliances." + AllianceUtil.getPlayerAlliance(player), null);
        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", null);
        AllianceUtil.saveAllianceConfig(allianceConfig);
        DomainUtil.saveDomainConfig(domainConfig);
    }

    public static void invitePlayerToAlliance(Player allianceLeader, Player invitee){
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();

        String allianceName = AllianceUtil.getPlayerAlliance(allianceLeader);

        List<String> allianceInvites = allianceConfig.getStringList("alliances." + allianceName + ".invites");
        allianceInvites.add(GeneralUtil.getKeyFromPlayer(invitee));

        allianceConfig.set("alliances." + allianceName + ".invites", allianceInvites);
        AllianceUtil.saveAllianceConfig(allianceConfig);
    }
}
