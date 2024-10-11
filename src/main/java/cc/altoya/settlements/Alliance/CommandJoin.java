package cc.altoya.settlements.Alliance;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandJoin {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
            return true;
        }
        joinAlliance(sender, args[1]);
        return true;
    }

    private static void joinAlliance(Player player, String allianceName) {

        if (AllianceUtil.isPlayerInAlliance(player)) {
            ChatUtil.sendErrorMessage(player, "You are already in an alliance.");
            return;
        }
        if (!AllianceUtil.doesAllianceExist(allianceName)) {
            ChatUtil.sendErrorMessage(player, "This alliance doesn't exist.");
            return;
        }
        if (!AllianceUtil.doesPlayerHaveInvite(player, allianceName)) {
            ChatUtil.sendErrorMessage(player, "You don't have an invite.");
            return;
        }

        AllianceUtil.acceptAllianceInvite(player, allianceName);

        ChatUtil.sendSuccessMessage(player, "You've joined the alliance.");
        AllianceUtil.sendMessageToAlliance(allianceName, player.displayName() + " has joined the alliance.");
    }

}
