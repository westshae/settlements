package cc.altoya.settlements.Alliance;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandInvite {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
            return true;
        }

        invitePlayerToAlliance(sender, args[1]);
        return true;
    }

    private static void invitePlayerToAlliance(Player allianceLeader, String inviteeName) {

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
        AllianceUtil.invitePlayerToAlliance(allianceLeader, invitee);
        ChatUtil.sendSuccessMessage(allianceLeader, "Invite successfully sent.");
        ChatUtil.sendSuccessMessage(invitee, "You've been invited to " + AllianceUtil.getPlayerAlliance(allianceLeader));
    }

}
