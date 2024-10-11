package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cc.altoya.settlements.Domain.DomainUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.ItemUtil;

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
    if (!DomainUtil.doesPlayerOwnChunk(player, chunk)) {
      ChatUtil.sendErrorMessage(player, "You don't own this structure");
      return;
    }

    ItemStack item = player.getInventory().getItemInMainHand();
    Integer amountToSupply = Integer.parseInt(amountToSupplyString);
    if(amountToSupply < 0){
      ChatUtil.sendErrorMessage(player, "You can't supply negative amounts.");
      return;
    }
    if(item.getAmount() < amountToSupply){
      ChatUtil.sendErrorMessage(player, "You don't have " + amountToSupply + " to supply." );
      return;
    }
    if (!BuildUtil.isValidSupplyType(item.getType())) {
      ChatUtil.sendErrorMessage(player, "This isn't a valid supplies type.");
      return;
    }
    if(!ItemUtil.isItemCustom(item)){
      ChatUtil.sendErrorMessage(player, "This isn't a server-resource.");
      return;
    }
    BuildUtil.editSupplies(player, chunk, item.getType(), amountToSupply);
    ChatUtil.sendSuccessMessage(player,
        "You put " + amountToSupply + " of " + item.getType() + " into the chest.");
    item.setAmount(item.getAmount() - amountToSupply);
  }
}
