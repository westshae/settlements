package cc.altoya.settlements.Build;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cc.altoya.settlements.Util.ChatUtil;

public class EventInputSupplies implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Chunk chestChunk = event.getInventory().getLocation().getBlock().getChunk();
        if (!BuildUtil.isChunkStructure(chestChunk)) {
            return;
        }

        if (event.getInventory().getType() == InventoryType.CHEST) {
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();

            // Check if the item is not null and is being placed into the inventory
            if (item != null && item.getType() == Material.COAL && item.getAmount() > 0) {
                // Your custom logic here
                BuildUtil.editSupplies(player, chestChunk, item.getAmount());
                ChatUtil.sendSuccessMessage(player, "You put " + item.getAmount() + " of " + item.getType() + " into the chest.");
                item.setAmount(0);
            }
        }
    }
}
