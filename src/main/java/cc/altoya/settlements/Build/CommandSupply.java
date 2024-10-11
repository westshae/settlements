package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandSupply {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 2)) {
      return true;
    }

    supplyFromHand(sender, args[1]);
    return true;
  }

  private static void supplyFromHand(Player player, String amountToSupplyString) {
    Chunk chunk = player.getLocation().getChunk();
    if (!BuildUtil.isChunkStructure(chunk)) {
      ChatUtil.sendErrorMessage(player, "This isn't a structure.");
      return;
    }
    if (!CityUtil.doesPlayerOwnChunk(player, chunk)) {
      ChatUtil.sendErrorMessage(player, "You don't own this structure");
      return;
    }

    ItemStack item = player.getInventory().getItemInMainHand();
    Integer amount = Integer.parseInt(amountToSupplyString);
    if(!BuildUtil.validSupplyItem(item, amount)){
      ChatUtil.sendErrorMessage(player, "This isn't a valid supply item/s");
      return;
    }
    
    BuildUtil.removeSupplyItems(player, chunk, item, amount);
    ChatUtil.sendSuccessMessage(player,
        "Supplied successfully.");
  }
}
