package cc.altoya.settlements.Alliance;

import org.bukkit.entity.Player;

import cc.altoya.settlements.City.CityUtil;
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
        if (!CityUtil.isPlayerMember(player)) {
            ChatUtil.sendErrorMessage(player, "You must be in an alliance to run this command");
            return;
        }

        CityUtil.setAllianceChatEnabled(player, !CityUtil.isChatEnabled(player));
        ChatUtil.sendSuccessMessage(player, "Alliance chat has now been " + (CityUtil.isChatEnabled(player) ? "disabled." : "enabled."));
    }
}
