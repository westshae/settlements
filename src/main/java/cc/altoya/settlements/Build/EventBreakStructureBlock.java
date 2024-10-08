package cc.altoya.settlements.Build;

import java.util.List;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
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
            ChatUtil.sendErrorMessage(player, "You don't own this structure.");
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
                ChatUtil.sendErrorMessage(player, "You cannot break this structure block.");
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onInteractInStructureChunk(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (!BuildUtil.isChunkStructure(block.getChunk())) {
            return;
        }

        if (block.getType() == Material.CHEST) {
            return;
        }

        switch (BlueprintUtil.getBuildingType(BuildUtil.getStructureBlueprintName(block.getChunk()))) {
            case "factory":
                handleFactory(event, block, player);
                break;
            default:
                event.setCancelled(true);
                return;
        }

    }

    private static void handleOre(BlockBreakEvent event, Block block, Player player) {
        Material currentMaterial = block.getType();
        event.setCancelled(true);
        event.getPlayer().playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);
        BuildUtil.editResources(player, block.getChunk(), 1);
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
        BuildUtil.editResources(player, block.getChunk(), 1);
        block.setType(Material.AIR);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(currentMaterial);
            }
        }.runTaskLater(GeneralUtil.getPlugin(), 100L);

    }

    private static void handleFactory(PlayerInteractEvent event, Block block, Player player) {
        Material currentMaterial = block.getType();
        List<Material> allowedInteractables = Arrays.asList(Material.LEVER, Material.STONE_BUTTON, Material.OAK_BUTTON,
                Material.STONE_PRESSURE_PLATE);
        if (!allowedInteractables.contains(currentMaterial)) {
            event.setCancelled(true);
            return;
        }
        if (BuildUtil.getSuppliesFromStructure(block.getChunk()) <= 0) {
            ChatUtil.sendErrorMessage(player, "You are out of supplies, please restock.");
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        event.getPlayer().playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
        BuildUtil.editResources(player, block.getChunk(), 5);
        BuildUtil.editSupplies(player, block.getChunk(), -1);
        block.setType(Material.AIR);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(currentMaterial);
            }
        }.runTaskLater(GeneralUtil.getPlugin(), 100L);
    }

}
