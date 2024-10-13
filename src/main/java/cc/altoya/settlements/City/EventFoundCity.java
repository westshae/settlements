package cc.altoya.settlements.City;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class EventFoundCity implements Listener {
  @EventHandler
  public void playerChatEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (CityUtil.getPlayersChunks(player).isEmpty()) {// Player has no claims.
      World world = Bukkit.getServer().getWorlds().get(0);
      while (true) {
        Random random = new Random();
        int randomX = random.nextInt(128 + 1) - 64; // Random number between -64 and 64
        int randomZ = random.nextInt(128 + 1) - 64; // Random number between -64 and 64
        Chunk chunk = world.getChunkAt(randomX, randomZ);
        if (!CityUtil.isChunkClaimed(chunk)) {
          Block teleportBlock = chunk.getBlock(0, 0, 0);
          int highestY = world.getHighestBlockYAt(teleportBlock.getLocation());
          teleportBlock = chunk.getBlock(0, highestY, 0);
          if(!teleportBlock.getType().isSolid()){
            continue;
          } else {
            teleportBlock = chunk.getBlock(0, highestY + 2, 0);
          }
          player.setInvulnerable(true);
          new BukkitRunnable() {
            @Override
            public void run() {
              player.setInvulnerable(false);
            }
          }.runTaskLater(GeneralUtil.getPlugin(), 100L); // 100 ticks = 5 seconds
          CityUtil.claimChunk(player, chunk);
          ChatUtil.sendSuccessMessage(player,
              "You've been assigned the townhall chunk of X:" + randomX + " Z:" + randomZ);

          player.teleport(teleportBlock.getLocation());
          return;
        }
      }
    }
  }
}