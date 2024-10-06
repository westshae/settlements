package cc.altoya.settlements.Build;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
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
        FileConfiguration config = BuildUtil.getStructureConfig();
        if (!DomainUtil.doesPlayerOwnChunk(player, chunk)) {
            ChatUtil.sendErrorMessage(player, "You don't own this structure");
            return;
        }
        List<String> allBlocks = new ArrayList<>();
        allBlocks.addAll(GeneralUtil.createListFromString(
                (String) config.get("structures." + GeneralUtil.getKeyFromChunk(chunk) + ".interactiveBlocks")));
        allBlocks.addAll(GeneralUtil.createListFromString(
                (String) config.get("structures." + GeneralUtil.getKeyFromChunk(chunk) + ".blocks")));

        for (String blockKey : allBlocks) {
            String allBlockPath = "structures.all_blocks." + blockKey;
            if (config.contains(allBlockPath)) {
                Block block = GeneralUtil.getBlockFromKey(blockKey);
                block.setType(Material.AIR);
                config.set(allBlockPath, null);
            }
        }

        config.set("structures." + GeneralUtil.getKeyFromChunk(chunk), null);

        BuildUtil.saveStructureConfig(config);

        ChatUtil.sendSuccessMessage(player, "Structure successfully deleted.");
    }

}