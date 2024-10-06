package cc.altoya.settlements.Alliance;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.AllianceUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandInvite {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2,
                "/alliance invite {username}")) {
            return true;
        }

        invitePlayerToAlliance(sender, args[1]);
        return true;
    }

    private static void invitePlayerToAlliance(Player allianceLeader, String inviteeName) {
        FileConfiguration allianceConfig = AllianceUtil.getAllianceConfig();

        if (!AllianceUtil.isPlayerInAlliance(allianceLeader)) {
            ChatUtil.sendErrorMessage(allianceLeader, "You aren't in an alliance.");
            return;
        }
        if (!AllianceUtil.isPlayerOwner(allianceLeader)) {
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
        String allianceName = AllianceUtil.getPlayerAlliance(allianceLeader);
        List<String> allianceInvites = GeneralUtil
                .createListFromString((String) allianceConfig.get("alliances." + allianceName + ".invites"));
        allianceInvites.add(GeneralUtil.getKeyFromPlayer(invitee));
        allianceConfig.set("alliances." + allianceName + ".invites", GeneralUtil.createStringFromList(allianceInvites));
        AllianceUtil.saveAllianceConfig(allianceConfig);
        ChatUtil.sendSuccessMessage(allianceLeader, "Invite successfully sent.");
        ChatUtil.sendSuccessMessage(invitee, "You've been invited to " + allianceName);
    }

}
