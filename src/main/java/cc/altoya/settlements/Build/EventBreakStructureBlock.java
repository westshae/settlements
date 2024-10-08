package cc.altoya.settlements.Build;

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

        if (!BuildUtil.isChunkStructure(block.getChunk())) {
            return;
        }

        if (!BuildUtil.getStructureOwner(block.getChunk()).equals(GeneralUtil.getKeyFromPlayer(player))) {
            ChatUtil.sendSuccessBar(player, "You don't own this structure.");
            return;
        }

        switch (block.getType()) {
            case COAL_ORE:
                handleOre(event, block, player);
                break;
            case IRON_ORE:
                handleOre(event, block, player);
                break;
            case WHEAT:
                handlePlants(event, block, player);
                break;
            case SUGAR_CANE:
                handlePlants(event, block, player);
                break;
            case PUMPKIN:
                handlePlants(event, block, player);
                break;
            case MELON:
                handlePlants(event, block, player);
                break;
            default:
                ChatUtil.sendErrorBar(player, "You cannot break this structure block.");
                event.setCancelled(true);
                break;
        }
    }


    private static void handleOre(BlockBreakEvent event, Block block, Player player) {
        Material currentMaterial = block.getType();
        event.setCancelled(true);
        event.getPlayer().playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);
        Material resource = BuildUtil.getResourceFromBlock(currentMaterial);
        BuildUtil.editResources(player, block.getChunk(), resource, 1);
        block.setType(Material.STONE);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(currentMaterial);
            }
        }.runTaskLater(GeneralUtil.getPlugin(), 100L);
    }

    private static void handlePlants(BlockBreakEvent event, Block block, Player player) {
        Material currentMaterial = block.getType();
        event.setCancelled(true);
        event.getPlayer().playSound(player.getLocation(), Sound.BLOCK_AZALEA_LEAVES_BREAK, 1.0f, 1.0f);
        Material resource = BuildUtil.getResourceFromBlock(currentMaterial);
        BuildUtil.editResources(player, block.getChunk(), resource, 1);
        block.setType(Material.AIR);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(currentMaterial);
            }
        }.runTaskLater(GeneralUtil.getPlugin(), 100L);

    }
}
