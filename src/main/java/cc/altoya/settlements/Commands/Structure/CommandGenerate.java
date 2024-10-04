package cc.altoya.settlements.Commands.Structure;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.StructureUtil;

public class CommandGenerate {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2,
                "/structure generate {type}")) {
            return true;
        }
        switch (args[1]) {
            case "mine":
                generateMineBuilding(sender, sender.getLocation().getChunk());
                break;
        }
        return true;
    }

    private static void generateMineBuilding(Player player, Chunk chunk) {
        if (StructureUtil.isChunkStructure(chunk)) {
            ChatUtil.sendErrorMessage(player, "There is already a structure in this chunk");
            return;
        }
        StructureUtil.createNewStructure(player, "mine");
        Location centerLocation = player.getLocation();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                Location adjustedLocation = centerLocation.clone().add(i, 0, j);
                StructureUtil.placeInteractiveBlock(player, adjustedLocation, Material.COAL_ORE);
            }
        }

        ChatUtil.sendSuccessMessage(player, "Mine successfully generated");
    }

}
