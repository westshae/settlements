package cc.altoya.settlements.Alliance;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandLeave {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }
        leaveAlliance(sender);
        return true;
    }

    private static void leaveAlliance(Player player) {
        if (!AllianceUtil.isPlayerMember(player)) {
            ChatUtil.sendErrorMessage(player, "You aren't in an alliance.");
            return;
        }
        String playerAllianceName = AllianceUtil.getPlayerAllianceName(player);

        AllianceUtil.removeMember(player, playerAllianceName);

        AllianceUtil.sendMessage(playerAllianceName, player.displayName() + " has left the alliance.");
        ChatUtil.sendSuccessMessage(player, "You've left the alliance.");
    }

}
