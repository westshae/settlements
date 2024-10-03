package cc.altoya.settlements.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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

    public static void createAlliance(Player player, String allianceName) {
        FileConfiguration allianceConfig = getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();
        if (isPlayerInAlliance(player)) {
            ChatUtil.sendErrorMessage(player, "You are already in an alliance.");
            return;
        }
        if (doesAllianceExist(allianceName)) {
            ChatUtil.sendErrorMessage(player, "This alliance already exists.");
            return;
        }
        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", allianceName);
        allianceConfig.set("alliances." + allianceName + ".owner", GeneralUtil.getKeyFromPlayer(player));
        List<String> alliancePlayers = new ArrayList<>();
        alliancePlayers.add(GeneralUtil.getKeyFromPlayer(player));
        allianceConfig.set("alliances." + allianceName + ".players", GeneralUtil.createStringFromList(alliancePlayers));

        DomainUtil.saveDomainConfig(domainConfig);
        saveAllianceConfig(allianceConfig);
        ChatUtil.sendSuccessMessage(player, "Alliance successfully created.");
        return;
    }

    public static void deleteAlliance(Player player) {
        FileConfiguration allianceConfig = getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();
        if (!isPlayerInAlliance(player)) {
            ChatUtil.sendErrorMessage(player, "You aren't in an alliance.");
            return;
        }
        if (!isPlayerOwner(player)) {
            ChatUtil.sendErrorMessage(player, "You aren't the leader of this alliance.");
            return;
        }
        String allianceName = getPlayerAlliance(player);
        allianceConfig.set("alliances." + allianceName, null);
        domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".alliance", null);
        saveAllianceConfig(allianceConfig);
        DomainUtil.saveDomainConfig(domainConfig);
        ChatUtil.sendSuccessMessage(player, "Alliance has been deleted.");
    }

    public static void invitePlayerToAlliance(Player allianceLeader, String inviteeName) {
        FileConfiguration allianceConfig = getAllianceConfig();

        if (!isPlayerInAlliance(allianceLeader)) {
            ChatUtil.sendErrorMessage(allianceLeader, "You aren't in an alliance.");
            return;
        }
        if (!isPlayerOwner(allianceLeader)) {
            ChatUtil.sendErrorMessage(allianceLeader, "You aren't the alliance leader.");
            return;
        }

        Player invitee = Bukkit.getPlayerExact(inviteeName);
        if (invitee == null) {
            ChatUtil.sendErrorMessage(allianceLeader, "This player doesn't exist.");
            return;
        }
        if (GeneralUtil.getKeyFromPlayer(invitee).equals(GeneralUtil.getKeyFromPlayer(allianceLeader))) {
            ChatUtil.sendErrorMessage(allianceLeader, "You can't invite yourself.");
            return;
        }
        String allianceName = getPlayerAlliance(allianceLeader);
        List<String> allianceInvites = GeneralUtil
                .createListFromString((String) allianceConfig.get("alliances." + allianceName + ".invites"));
        allianceInvites.add(GeneralUtil.getKeyFromPlayer(invitee));
        allianceConfig.set("alliances." + allianceName + ".invites", GeneralUtil.createStringFromList(allianceInvites));
        saveAllianceConfig(allianceConfig);
        ChatUtil.sendSuccessMessage(allianceLeader, "Invite successfully sent.");
        ChatUtil.sendSuccessMessage(invitee, "You've been invited to " + allianceName);
    }

    public static void joinAlliance(Player player, String allianceName) {
        FileConfiguration allianceConfig = getAllianceConfig();
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();

        if (isPlayerInAlliance(player)) {
            ChatUtil.sendErrorMessage(player, "You are already in an alliance.");
            return;
        }
        if (!doesAllianceExist(allianceName)) {
            ChatUtil.sendErrorMessage(player, "This alliance doesn't exist.");
            return;
        }
        if (!doesPlayerHaveInvite(player, allianceName)) {
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

        saveAllianceConfig(allianceConfig);
        DomainUtil.saveDomainConfig(domainConfig);
    }

    public static void printAllianceInfo(Player player) {
        if (!isPlayerInAlliance(player)) {
            ChatUtil.sendErrorMessage(player, "You must be in an alliance to run this command.");
            return;
        }
        String allianceName = getPlayerAlliance(player);
        ChatUtil.sendSuccessMessage(player, allianceName + " info.");
        List<String> alliancePlayers = getAlliancePlayers(allianceName);
        alliancePlayers.forEach((playerUuid) -> {
            UUID uuid = UUID.fromString(playerUuid);
            Player currentPlayer = Bukkit.getPlayer(uuid);
            ChatUtil.sendSuccessMessage(player, currentPlayer.getDisplayName());
        });
    }

    public static void toggleAllianceChat(Player player){
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();
        if(!isPlayerInAlliance(player)){
            ChatUtil.sendErrorMessage(player, "You must be in an alliance to run this command");
            return;
        }
        if(!domainConfig.contains("domains." + GeneralUtil.getKeyFromPlayer(player) + ".allianceChat")){
            domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".allianceChat", true);
            ChatUtil.sendSuccessMessage(player, "Alliance chat has now been enabled.");
        } else {
            boolean allianceChatEnabled = domainConfig.getBoolean("domains." + player.getUniqueId().toString() + ".allianceChat");
            domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".allianceChat", !allianceChatEnabled);
            if(allianceChatEnabled){
                ChatUtil.sendSuccessMessage(player, "Alliance chat has now been disabled");
            } else {
                ChatUtil.sendSuccessMessage(player, "Alliance chat has now been enabled.");
            }
        }

        DomainUtil.saveDomainConfig(domainConfig);
    }
}
