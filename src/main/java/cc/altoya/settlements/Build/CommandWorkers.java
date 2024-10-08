package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandWorkers {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2, "/build workers")) {
            return true;
        }

        setWorkers(sender, args[1]);
        return true;
    }

    private static void setWorkers(Player player, String amount) {
        Chunk chunk = player.getLocation().getChunk();
        if(!BuildUtil.isChunkStructure(chunk)){
            ChatUtil.sendErrorMessage(player, "There is no structure in this chunk.");
            return;
        }

        FileConfiguration config = BuildUtil.getBuildConfig();

        Integer amountInt = Integer.parseInt(amount);
        config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".workers", amountInt);

        ChatUtil.sendSuccessMessage(player, "Structure workers set to " + amount);
        BuildUtil.saveBuildConfig(config);
    }

}
