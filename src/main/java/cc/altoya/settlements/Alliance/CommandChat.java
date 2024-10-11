package cc.altoya.settlements.Alliance;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandChat {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }

        toggleAllianceChat(sender);
        return true;
    }

    private static void toggleAllianceChat(Player player) {
        if (!AllianceUtil.isPlayerMember(player)) {
            ChatUtil.sendErrorMessage(player, "You must be in an alliance to run this command");
            return;
        }

        AllianceUtil.setChatEnabled(player, !AllianceUtil.isChatEnabled(player));
        ChatUtil.sendSuccessMessage(player, "Alliance chat has now been " + (AllianceUtil.isChatEnabled(player) ? "disabled." : "enabled."));
    }
}
