package cc.altoya.settlements.Alliance;

import org.bukkit.entity.Player;

import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandDelete {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }

        deleteAlliance(sender);
        return true;
    }

    private static void deleteAlliance(Player player) {
        if (!CityUtil.isPlayerMember(player)) {
            ChatUtil.sendErrorMessage(player, "You aren't in an alliance.");
            return;
        }
        if (!AllianceUtil.isPlayerOwner(player)) {
            ChatUtil.sendErrorMessage(player, "You aren't the leader of this alliance.");
            return;
        }
        AllianceUtil.delete(player);
        ChatUtil.sendSuccessMessage(player, "Alliance has been deleted.");
    }

}
