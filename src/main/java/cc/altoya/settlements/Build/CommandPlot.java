package cc.altoya.settlements.Build;

import org.bukkit.entity.Player;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandPlot {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }

        displayPlot(sender);
        return true;
    }

    private static void displayPlot(Player player) {
        BuildUtil.displayParticleChunkBorder(player.getChunk(), player.getLocation().getBlockY());
    }

}
