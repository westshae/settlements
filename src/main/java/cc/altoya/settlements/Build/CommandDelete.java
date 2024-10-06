package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Domain.DomainUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandDelete {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/build delete")) {
            return true;
        }

        deleteStructure(sender, sender.getLocation().getChunk());
        return true;
    }

    private static void deleteStructure(Player player, Chunk chunk) {
        if (!DomainUtil.doesPlayerOwnChunk(player, chunk)) {
            ChatUtil.sendErrorMessage(player, "You don't own this structure");
            return;
        }

        BuildUtil.deleteBlocksFromBlueprint(chunk, player);
        ChatUtil.sendSuccessMessage(player, "Structure successfully deleted.");
    }

}
