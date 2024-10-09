package cc.altoya.settlements.Build;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.ItemUtil;

public class EventInteractStructure implements Listener {

    @EventHandler
    public void onInteractInStructureChunk(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.PHYSICAL)) {
            return;
        }

        if (!BuildUtil.isChunkStructure(block.getChunk())) {
            return;
        }

        boolean serverCalled = event.getClickedBlock().hasMetadata("serverCalled");
        if (serverCalled) {
            event.getClickedBlock().removeMetadata("serverCalled", GeneralUtil.getPlugin());
        }

        switch (block.getType()) {
            case LEVER:
                handleInteractables(event, block, player, Material.COAL, serverCalled);
                break;
            case STONE_PRESSURE_PLATE:
                handleInteractables(event, block, player, Material.COAL, serverCalled);
                break;
            case STONE_BUTTON:
                handleInteractables(event, block, player, Material.COAL, serverCalled);
                break;
            case CHEST:
                break;
            default:
                if (!serverCalled) {
                    ChatUtil.sendErrorBar(player, "You cannot interact with this structure block.");
                }
                event.setCancelled(true);
                break;
        }
    }

    private static void handleInteractables(PlayerInteractEvent event, Block block, Player player, Material supply,
            boolean serverCalled) {
        if (BuildUtil.getSuppliesFromStructure(block.getChunk(), supply) <= 0) {
            ChatUtil.sendErrorBar(player, "You are out of supplies. Type: " + supply.toString());
            event.setCancelled(true);
            return;
        }
        Material currentMaterial = block.getType();
        BlockData data = block.getBlockData();
        event.setCancelled(true);
        Material resource = ItemUtil.getResourceFromBlock(currentMaterial);
        BuildUtil.editResources(player, block.getChunk(), resource, 1);
        BuildUtil.editSupplies(player, block.getChunk(), supply, -1);
        if (!serverCalled) {
            event.getPlayer().playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
            ChatUtil.sendSuccessBar(player,
                    "Supplies are now at " + BuildUtil.getSuppliesFromStructure(block.getChunk(), resource));
            ChatUtil.sendSuccessBar(player,
                    "Resources now at " + BuildUtil.getResourcesFromStructure(block.getChunk(), resource));

        }
        block.setType(Material.AIR);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(currentMaterial);
                block.setBlockData(data);
            }
        }.runTaskLater(GeneralUtil.getPlugin(), 100L);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Chunk chestChunk = event.getInventory().getLocation().getBlock().getChunk();
        if (!BuildUtil.isChunkStructure(chestChunk)) {
            return;
        }

        if (event.getInventory().getType() == InventoryType.CHEST) {
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();

            if (!BuildUtil.isValidSupplyType(item.getType())) {
                ChatUtil.sendErrorMessage(player, "This isn't a valid supplies type.");
                return;
            }

            BuildUtil.editSupplies(player, chestChunk, item.getType(), item.getAmount());
            ChatUtil.sendSuccessMessage(player,
                    "You put " + item.getAmount() + " of " + item.getType() + " into the chest.");
            item.setAmount(0);

        }
    }
}
