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

public class EventStructureInteract implements Listener {
    @EventHandler
    public void onBreakInStructureChunk(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (!BuildUtil.isChunkStructure(block.getChunk())) {
            return;
        }

        switch (BlueprintUtil.getBuildingType(BuildUtil.getStructureBlueprintName(block.getChunk()))) {
            case "mine":
                handleMine(event, block, player);
                break;
            case "farm":
                handleFarm(event, block, player);
                break;
            default:
                event.setCancelled(true);
                return;
        }
    }

    @EventHandler
    public void onInteractInStructureChunk(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (!BuildUtil.isChunkStructure(block.getChunk())) {
            return;
        }

        if(block.getType() == Material.CHEST){
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

    private static void handleMine(BlockBreakEvent event, Block block, Player player) {
        Material currentMaterial = block.getType();
        if (currentMaterial == Material.STONE) {
            event.setCancelled(true);
            return;
        }
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

    private static void handleFarm(BlockBreakEvent event, Block block, Player player) {
        Material currentMaterial = block.getType();
        List<Material> allowedCrops = Arrays.asList(Material.SUGAR_CANE, Material.WHEAT, Material.PUMPKIN, Material.MELON);
        if(!allowedCrops.contains(currentMaterial)){
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        event.getPlayer().playSound(player.getLocation(), Sound.BLOCK_PUMPKIN_CARVE, 1.0f, 1.0f);
        BuildUtil.editResources(player, block.getChunk(), 1);
        block.setType(Material.AIR);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(currentMaterial);
            }
        }.runTaskLater(GeneralUtil.getPlugin(), 100L);
    }

    private static void handleFactory(PlayerInteractEvent event, Block block, Player player){
        Material currentMaterial = block.getType();
        List<Material> allowedInteractables = Arrays.asList(Material.LEVER, Material.STONE_BUTTON, Material.OAK_BUTTON, Material.STONE_PRESSURE_PLATE);
        if(!allowedInteractables.contains(currentMaterial)){
            event.setCancelled(true);
            return;
        }
        if(BuildUtil.getSuppliesFromStructure(block.getChunk()) <= 0){
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
