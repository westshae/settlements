package cc.altoya.settlements.Alliance;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandCreate {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
            return true;
        }

        createAlliance(sender, args[1]);
        return true;
    }

    private static void createAlliance(Player player, String allianceName) {
        if (AllianceUtil.exists(allianceName)) {
            ChatUtil.sendErrorMessage(player, "This alliance already exists.");
            return;
        }

        AllianceUtil.create(player, allianceName);
    
        ChatUtil.sendSuccessMessage(player, "Alliance successfully created.");
        return;
    }

}
