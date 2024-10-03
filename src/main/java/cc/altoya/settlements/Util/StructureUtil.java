package cc.altoya.settlements.Util;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class StructureUtil {
    public static FileConfiguration getStructureConfig() {
        return GeneralUtil.getPluginConfig("settlements", "structures.yml");
    }

    public static void saveStructureConfig(FileConfiguration config) {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(),
                "structures.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewStructure(Player player) {
        FileConfiguration config = getStructureConfig();
        Chunk chunk = player.getLocation().getChunk();

        config.set("structures." + GeneralUtil.getKeyFromChunk(chunk) + ".owner", GeneralUtil.getKeyFromPlayer(player));
        saveStructureConfig(config);
    }

    public static void saveBlockAsStructureBlock(Block block) {
        FileConfiguration config = getStructureConfig();
        config.set("structures.all_blocks." + GeneralUtil.getKeyFromBlock(block), true);
        saveStructureConfig(config);
    }

    public static boolean isBlockStructureBlock(Block block) {
        FileConfiguration config = getStructureConfig();
        return config.contains("structures.all_blocks." + GeneralUtil.getKeyFromBlock(block));
    }

    public static void placeBlock(Player player) {
        Location location = player.getLocation();
        location.add(0, 3, 0);
        Block block = location.getBlock();

        block.setType(Material.ACACIA_BUTTON);
        saveBlockAsStructureBlock(block);
    }

}
