package cc.altoya.settlements.Blueprint;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandTP {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
            return true;
        }

        teleportPlayer(sender, args[1]);
        return true;
    }

    private static void teleportPlayer(Player player, String blueprintName) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();
        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }
        String firstBlockString = config.getString("blueprints." + blueprintName + ".first");
        Block firstBlock = BlueprintUtil.turnStringIntoBlock(firstBlockString);

        Location teleportLocation = firstBlock.getLocation().clone().add(0, 2, 0);
        player.teleport(teleportLocation);

        ChatUtil.sendSuccessMessage(player, "Teleported Successfully.");
    }

}
