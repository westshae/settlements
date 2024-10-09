package cc.altoya.settlements.Build;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Random;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class ScheduledWorker {
    public static Runnable getRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                FileConfiguration buildsConfig = BuildUtil.getBuildConfig();
                FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();

                ConfigurationSection section = buildsConfig.getConfigurationSection("builds");
                List<String> chunkKeys = new ArrayList<>(section.getKeys(false));

                for (String key : chunkKeys) {
                    String ownerUuid = buildsConfig.getString("builds." + key + ".owner");

                    Player player = Bukkit.getPlayer(UUID.fromString(ownerUuid));
                    Chunk chunk = player.getLocation().getChunk();

                    boolean isNearbyAndOnline = key.equals(GeneralUtil.getKeyFromChunk(chunk)) && player.isOnline();
                    String blueprintName = BuildUtil.getStructureBlueprintName(chunk);

                    List<String> resourceBlocks = blueprintConfig
                            .getStringList("blueprints." + blueprintName + ".resourceBlocks");

                    if (resourceBlocks.size() == 0) {
                        continue;
                    }

                    String randomResourceBlockKey = resourceBlocks.get(new Random().nextInt(resourceBlocks.size()));

                    Block randomBlock = BlueprintUtil.turnStringIntoBlock(randomResourceBlockKey);

                    String originBlockKey = buildsConfig.getString("builds." + key + ".first");

                    Block originBlock = BlueprintUtil.turnStringIntoBlock(originBlockKey);

                    Location nonRelativeRandomBlock = BlueprintUtil.getNonRelativeLocation(originBlock,
                            randomBlock.getLocation());

                    if (isNearbyAndOnline) {
                        int yIncrease = 0;
                        if (!nonRelativeRandomBlock.getBlock().isPassable()) {
                            yIncrease = 1;
                        }

                        Villager villager = (Villager) nonRelativeRandomBlock.getWorld().spawnEntity(
                                nonRelativeRandomBlock.clone().add(0.5, yIncrease, 0.5),
                                EntityType.VILLAGER);
                        villager.setAI(false);
                        villager.setInvulnerable(true);
                        new BukkitRunnable() {
                            int timeElapsed = 0;

                            @Override
                            public void run() {
                                if (timeElapsed >= 5) {
                                    villager.remove();
                                    cancel();
                                    BlockBreakEvent breakEvent = new BlockBreakEvent(nonRelativeRandomBlock.getBlock(),
                                            player);
                                    PlayerInteractEvent interactEvent = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, null, nonRelativeRandomBlock.getBlock(), null);
                                    breakEvent.getBlock().setMetadata("serverCalled", new FixedMetadataValue(GeneralUtil.getPlugin(), true));
                                    breakEvent.getBlock().setMetadata("serverCalled", new FixedMetadataValue(GeneralUtil.getPlugin(), true));

                                    Bukkit.getPluginManager().callEvent(breakEvent);
                                    Bukkit.getPluginManager().callEvent(interactEvent);

                                    return;
                                }

                                // Spawn particles around the villager
                                nonRelativeRandomBlock.getWorld().spawnParticle(Particle.HEART,
                                        villager.getLocation().clone().add(0, 2, 0), 10);

                                timeElapsed++;
                            }
                        }.runTaskTimer(GeneralUtil.getPlugin(), 500, 20);
                    }

                }
            }
        };
    }
}
