package cc.altoya.settlements.Build;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class EventBreakStructureBlock implements Listener {
    @EventHandler
    public void onBreakInStructureChunk(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        boolean serverCalled = event.getBlock().hasMetadata("serverCalled");
        if (!serverCalled) {
            event.getBlock().removeMetadata("serverCalled", GeneralUtil.getPlugin());
        }

        if (!BuildUtil.isChunkStructure(block.getChunk())) {
            return;
        }

        if (!BuildUtil.getStructureOwner(block.getChunk()).equals(GeneralUtil.getKeyFromPlayer(player))) {
            if (serverCalled) {
                ChatUtil.sendErrorBar(player, "You don't own this structure.");
            }
            return;
        }

        switch (block.getType()) {
            case COAL_ORE:
                handleOre(event, block, player, serverCalled);
                break;
            case IRON_ORE:
                handleOre(event, block, player, serverCalled);
                break;
            case WHEAT:
                handlePlants(event, block, player, serverCalled);
                break;
            case SUGAR_CANE:
                handlePlants(event, block, player, serverCalled);
                break;
            case PUMPKIN:
                handlePlants(event, block, player, serverCalled);
                break;
            case MELON:
                handlePlants(event, block, player, serverCalled);
                break;
            case OAK_LOG:
                handleWood(event, block, player, serverCalled);
                break;
            default:
                if (!serverCalled) {
                    ChatUtil.sendErrorBar(player, "You cannot break this structure block.");
                }
                event.setCancelled(true);
                break;
        }
    }

    private static void handleWood(BlockBreakEvent event, Block block, Player player, boolean serverCalled) {
        Material currentMaterial = block.getType();
        event.setCancelled(true);
        Material resource = BuildUtil.getResourceFromBlock(currentMaterial);
        BuildUtil.editResources(player, block.getChunk(), resource, 1);
        if (!serverCalled) {
            ChatUtil.sendSuccessBar(player, "Resources now at " + BuildUtil.getResourcesFromStructure(block.getChunk(), resource));
            event.getPlayer().playSound(player.getLocation(), Sound.BLOCK_WOOD_BREAK, 1.0f, 1.0f);
        }
        block.setType(Material.AIR);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(currentMaterial);
            }
        }.runTaskLater(GeneralUtil.getPlugin(), 100L);
    }

    private static void handleOre(BlockBreakEvent event, Block block, Player player, boolean serverCalled) {
        Material currentMaterial = block.getType();
        event.setCancelled(true);
        Material resource = BuildUtil.getResourceFromBlock(currentMaterial);
        BuildUtil.editResources(player, block.getChunk(), resource, 1);
        if (!serverCalled) {
            ChatUtil.sendSuccessBar(player, "Resources now at " + BuildUtil.getResourcesFromStructure(block.getChunk(), resource));
            event.getPlayer().playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);
        }
        block.setType(Material.STONE);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(currentMaterial);
            }
        }.runTaskLater(GeneralUtil.getPlugin(), 100L);
    }

    private static void handlePlants(BlockBreakEvent event, Block block, Player player, boolean serverCalled) {
        Material currentMaterial = block.getType();
        event.setCancelled(true);
        Material resource = BuildUtil.getResourceFromBlock(currentMaterial);
        BuildUtil.editResources(player, block.getChunk(), resource, 1);
        if (!serverCalled) {
            ChatUtil.sendSuccessBar(player, "Resources now at " + BuildUtil.getResourcesFromStructure(block.getChunk(), resource));
            event.getPlayer().playSound(player.getLocation(), Sound.BLOCK_AZALEA_LEAVES_BREAK, 1.0f, 1.0f);
        }
        block.setType(Material.AIR);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(currentMaterial);
            }
        }.runTaskLater(GeneralUtil.getPlugin(), 100L);

    }
}
