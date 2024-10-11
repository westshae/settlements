package cc.altoya.settlements.Alliance;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandInfo {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }

        printAllianceInfo(sender);
        return true;
    }

    private static void printAllianceInfo(Player player) {
        if (!CityUtil.isPlayerMember(player)) {
            ChatUtil.sendErrorMessage(player, "You must be in an alliance to run this command.");
            return;
        }
        String allianceName = CityUtil.getPlayerAllianceName(player);
        ChatUtil.sendSuccessMessage(player, allianceName + " info.");
        List<String> alliancePlayers = AllianceUtil.getMembers(allianceName);
        alliancePlayers.forEach((playerUuid) -> {
            UUID uuid = UUID.fromString(playerUuid);
            Player currentPlayer = Bukkit.getPlayer(uuid);
            ChatUtil.sendSuccessMessage(player, currentPlayer.displayName().toString());
        });
    }

}
