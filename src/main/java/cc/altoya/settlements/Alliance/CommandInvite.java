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

        if (!AllianceUtil.isPlayerMember(allianceLeader)) {
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
        String allianceName = AllianceUtil.getPlayerAllianceName(allianceLeader);
        AllianceUtil.addInvite(allianceName, invitee);
        ChatUtil.sendSuccessMessage(allianceLeader, "Invite successfully sent.");
        ChatUtil.sendSuccessMessage(invitee, "You've been invited to " + allianceName);
    }

}
