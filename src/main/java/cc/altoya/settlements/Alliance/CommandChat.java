package cc.altoya.settlements.Alliance;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.AllianceUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DomainUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandChat {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/alliance chat")) {
            return true;
        }

        toggleAllianceChat(sender);
        return true;
    }

    private static void toggleAllianceChat(Player player) {
        FileConfiguration domainConfig = DomainUtil.getDomainConfig();
        if (!AllianceUtil.isPlayerInAlliance(player)) {
            ChatUtil.sendErrorMessage(player, "You must be in an alliance to run this command");
            return;
        }
        if (!domainConfig.contains("domains." + GeneralUtil.getKeyFromPlayer(player) + ".allianceChat")) {
            domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".allianceChat", true);
            ChatUtil.sendSuccessMessage(player, "Alliance chat has now been enabled.");
        } else {
            boolean allianceChatEnabled = domainConfig
                    .getBoolean("domains." + player.getUniqueId().toString() + ".allianceChat");
            domainConfig.set("domains." + GeneralUtil.getKeyFromPlayer(player) + ".allianceChat", !allianceChatEnabled);
            if (allianceChatEnabled) {
                ChatUtil.sendSuccessMessage(player, "Alliance chat has now been disabled");
            } else {
                ChatUtil.sendSuccessMessage(player, "Alliance chat has now been enabled.");
            }
        }

        DomainUtil.saveDomainConfig(domainConfig);
    }

}
