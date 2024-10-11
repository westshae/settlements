package cc.altoya.settlements.Build;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.World;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandPlot {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
            return true;
        }

        displayPlot(sender);
        return true;
    }

    private static void displayPlot(Player player) {
        int y = player.getLocation().getBlockY();
        World world = player.getWorld();
        Location chunkLocation = player.getLocation();
        int chunkX = chunkLocation.getChunk().getX() * 16;
        int chunkZ = chunkLocation.getChunk().getZ() * 16;

        // Schedule a repeating task to show the border for 10 seconds
        new BukkitRunnable() {
            int ticksElapsed = 0;

            @Override
            public void run() {
                if (ticksElapsed > 200) {
                    this.cancel();
                    return;
                }

                // Display particles along the edges of the chunk
                displayChunkBorder(world, chunkX, y, chunkZ);

                ticksElapsed += 5;
            }
        }.runTaskTimer(GeneralUtil.getPlugin(), 0L, 5);
    }

    private static void displayChunkBorder(World world, int chunkX, int y, int chunkZ) {
        Particle particleType = Particle.HAPPY_VILLAGER; // Example particle type
        double particleOffset = 0.5; // To center particles on block edges
        int particleCount = 1; // Number of particles to spawn at each point

        // Loop over each corner of the chunk (inside the chunk, so we stop 1 block before the edge)
        for (int x = chunkX; x < chunkX + 16; x++) {
            // Draw the border along the Z edges (North and South)
            world.spawnParticle(particleType, x + particleOffset, y, chunkZ + particleOffset, particleCount);
            world.spawnParticle(particleType, x + particleOffset, y, chunkZ + 15 + particleOffset, particleCount);
        }

        for (int z = chunkZ; z < chunkZ + 16; z++) {
            // Draw the border along the X edges (West and East)
            world.spawnParticle(particleType, chunkX + particleOffset, y, z + particleOffset, particleCount);
            world.spawnParticle(particleType, chunkX + 15 + particleOffset, y, z + particleOffset, particleCount);
        }
    }
}
