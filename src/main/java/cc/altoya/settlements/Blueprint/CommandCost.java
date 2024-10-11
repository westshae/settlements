package cc.altoya.settlements.Blueprint;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandCost {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 3)) {
            return true;
        }

        setCost(sender, args[1], args[2]);
        return true;
    }

    private static void setCost(Player player, String blueprintName, String amount) {
        FileConfiguration config = BlueprintUtil.getBlueprintConfig();
        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand().clone();
        item.setAmount(Integer.parseInt(amount));
        config.set("blueprints." + blueprintName + ".cost." + item.getType(), Integer.parseInt(amount));

        ChatUtil.sendSuccessMessage(player, "Blueprint cost for " + item.getType() + " set to " + amount);
        BlueprintUtil.saveBlueprintConfig(config);
    }

}
