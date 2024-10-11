package cc.altoya.settlements.Alliance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Domain.DomainUtil;
import cc.altoya.settlements.Util.ChatUtil;
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

    public static boolean isPlayerMember(Player player) {
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();
        return domainConfig.contains("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance");
    }

    public static boolean exists(String name) {
        FileConfiguration allianceConfig = getAllianceConfig();
        return allianceConfig.contains("alliances." + name);
    }

    public static String getPlayerAllianceName(Player player) {
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();
        return domainConfig.getString("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance");
    }

    public static boolean isPlayerOwner(Player player) {
        FileConfiguration allianceConfig = getAllianceConfig();
        String allianceName = getPlayerAllianceName(player);
        String allianceOwnerUuid = allianceConfig.getString("alliances." + allianceName + ".owner");
        return GeneralUtil.getKeyFromPlayer(player).equals(allianceOwnerUuid);
    }

    public static boolean isChatEnabled(Player player) {
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        return domainConfig.getBoolean("domains." + player.getUniqueId().toString() + ".allianceChat");
    }

    public static void setChatEnabled(Player player, boolean toggle) {
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".allianceChat",
                !isChatEnabled(player));
        DomainUtil.saveDomainConfig(domainConfig);
    }

    public static boolean doesPlayerHaveInvite(Player player, String allianceName) {
        FileConfiguration allianceConfig = getAllianceConfig();
        List<String> invites = allianceConfig.getStringList("alliances." + allianceName + ".invites");
        return invites.contains(GeneralUtil.getKeyFromPlayer(player));
    }

    public static List<String> getMembers(String allianceName) {
        FileConfiguration allianceConfig = getAllianceConfig();

        return allianceConfig.getStringList("alliances." + allianceName + ".players");
    }

    public static void create(Player player, String allianceName) {
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

    public static void delete(Player player) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        allianceConfig.set("alliances." + AllianceUtil.getPlayerAllianceName(player), null);
        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", null);
        AllianceUtil.saveAllianceConfig(allianceConfig);
        DomainUtil.saveDomainConfig(domainConfig);
    }

    public static void addInvite(String allianceName, Player invitee) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();

        List<String> allianceInvites = allianceConfig.getStringList("alliances." + allianceName + ".invites");
        allianceInvites.add(GeneralUtil.getKeyFromPlayer(invitee));

        allianceConfig.set("alliances." + allianceName + ".invites", allianceInvites);
        AllianceUtil.saveAllianceConfig(allianceConfig);
    }

    public static void removeInvite(String allianceName, Player player) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();

        List<String> allianceInvites = allianceConfig.getStringList("alliances." + allianceName + ".invites");
        allianceInvites.removeIf((playerUuid) -> playerUuid.equals(GeneralUtil.getKeyFromPlayer(player)));
        allianceConfig.set("alliances." + allianceName + ".invites", allianceInvites);

        saveAllianceConfig(allianceConfig);
    }

    public static void addMember(Player player, String allianceName) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        List<String> alliancePlayers = allianceConfig.getStringList("alliances." + allianceName + ".players");

        alliancePlayers.add(GeneralUtil.getKeyFromPlayer(player));

        allianceConfig.set("alliances." + allianceName + ".players", alliancePlayers);
        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", allianceName);

        AllianceUtil.saveAllianceConfig(allianceConfig);
        DomainUtil.saveDomainConfig(domainConfig);
    }

    public static void removeMember(Player player, String allianceName) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        List<String> alliancePlayers = allianceConfig.getStringList("alliances." + allianceName + ".players");
        alliancePlayers.removeIf((playerUuid) -> playerUuid.equals(GeneralUtil.getKeyFromPlayer(player)));
        allianceConfig.set("alliances." + allianceName + ".invites", alliancePlayers);

        allianceConfig.set("alliances." + allianceName + ".players", alliancePlayers);
        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", null);

        AllianceUtil.saveAllianceConfig(allianceConfig);
        DomainUtil.saveDomainConfig(domainConfig);
    }

    public static void sendMessage(String allianceName, String message) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();

        List<String> alliancePlayers = allianceConfig.getStringList("alliances." + allianceName + ".players");

        alliancePlayers.forEach((playerUuid) -> {
            ChatUtil.sendSuccessMessage(Bukkit.getPlayer(UUID.fromString(playerUuid)), message);
        });
    }

    public static HashMap<String, String> getAllianceCommands() {
        HashMap<String, String> commands = new HashMap<>();
        commands.put("/alliance create {allianceName}", "The first command to create an alliance.");
        commands.put("/alliance delete", "The command to delete your alliance.");
        commands.put("/alliance info", "Prints out information about the alliance. Eg players, name, wealth.");
        commands.put("/alliance invite {username}", "Extends an invitation to the player specified.");
        commands.put("/alliance join {allianceName}", "Allows you to join an alliance if you've been invited.");
        commands.put("/alliance leave", "Allows you to leave your alliance.");
        commands.put("/alliance help", "The command you're looking at right now.");

        return commands;
    }

}
