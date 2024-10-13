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

import cc.altoya.settlements.City.CityUtil;
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


    public static boolean exists(String name) {
        FileConfiguration allianceConfig = getAllianceConfig();
        return allianceConfig.contains("alliances." + name);
    }


    public static boolean isPlayerOwner(Player player) {
        FileConfiguration allianceConfig = getAllianceConfig();
        String allianceName = CityUtil.getPlayerAllianceName(player);
        String allianceOwnerUuid = allianceConfig.getString("alliances." + allianceName + ".owner");
        return GeneralUtil.getKeyFromPlayer(player).equals(allianceOwnerUuid);
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

        allianceConfig.set("alliances." + allianceName + ".owner", GeneralUtil.getKeyFromPlayer(player));

        List<String> alliancePlayers = new ArrayList<>();
        alliancePlayers.add(GeneralUtil.getKeyFromPlayer(player));

        allianceConfig.set("alliances." + allianceName + ".players", alliancePlayers);
        allianceConfig.set("alliances." + allianceName + ".allianceChat", false);

        AllianceUtil.saveAllianceConfig(allianceConfig);
        CityUtil.setPlayerAlliance(player, allianceName);
    }

    public static void delete(Player player) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();

        allianceConfig.set("alliances." + CityUtil.getPlayerAllianceName(player), null);
        AllianceUtil.saveAllianceConfig(allianceConfig);
        CityUtil.removePlayerAlliance(player);
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

        List<String> alliancePlayers = allianceConfig.getStringList("alliances." + allianceName + ".players");

        alliancePlayers.add(GeneralUtil.getKeyFromPlayer(player));
        allianceConfig.set("alliances." + allianceName + ".players", alliancePlayers);

        AllianceUtil.saveAllianceConfig(allianceConfig);
        CityUtil.setPlayerAlliance(player, allianceName);

    }

    public static void removeMember(Player player, String allianceName) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();

        List<String> alliancePlayers = allianceConfig.getStringList("alliances." + allianceName + ".players");
        alliancePlayers.removeIf((playerUuid) -> playerUuid.equals(GeneralUtil.getKeyFromPlayer(player)));
        allianceConfig.set("alliances." + allianceName + ".invites", alliancePlayers);

        allianceConfig.set("alliances." + allianceName + ".players", alliancePlayers);

        AllianceUtil.saveAllianceConfig(allianceConfig);
        CityUtil.setPlayerAlliance(player, allianceName);
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
        commands.put("/alliance info", "Prints out information about the alliance. Eg players, name, wealth.");
        commands.put("/alliance help", "The command you're looking at right now.");

        return commands;
    }

}
