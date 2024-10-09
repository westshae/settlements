package cc.altoya.settlements.Build;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.GeneralUtil;

public class BuildUtil {
    public static FileConfiguration getBuildConfig() {
        return GeneralUtil.getPluginConfig("settlements", "builds.yml");
    }

    public static void saveBuildConfig(FileConfiguration config) {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("settlements").getDataFolder(),
                "builds.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Integer getStructurePlayerheight(Chunk chunk, Player player) {
        FileConfiguration buildConfig = getBuildConfig();
        if (!buildConfig.contains("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".playerHeight")) {
            return null;
        } else {
            return buildConfig.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".playerHeight");
        }
    }

    public static String getStructureBlueprintName(Chunk chunk) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }
        return config.getString("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".blueprintName");
    }

    public static boolean isBlockStructureBlock(Chunk chunk) {
        FileConfiguration config = getBuildConfig();
        return config.contains("builds." + GeneralUtil.getKeyFromChunk(chunk));
    }

    public static boolean isChunkStructure(Chunk chunk) {
        FileConfiguration config = getBuildConfig();
        return config.contains("builds." + GeneralUtil.getKeyFromChunk(chunk));
    }

    public static String getStructureOwner(Chunk chunk) {
        FileConfiguration config = getBuildConfig();
        return config.getString("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".owner");
    }

    public static void placeBlockForStructure(Player player, Location location, Material material,
            BlockData blockData) {
        Block block = location.getBlock();
        block.setType(material, false);
        block.setBlockData(blockData, false);
    }

    public static void setBlueprintName(Chunk chunk, Player player, String newBlueprintName) {
        if (!BuildUtil.isChunkStructure(chunk)) {
            return;
        }
        FileConfiguration buildConfig = getBuildConfig();
        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".blueprintName", newBlueprintName);

        saveBuildConfig(buildConfig);
    }

    public static void setVersion(Chunk chunk, Player player, int version) {
        if (!BuildUtil.isChunkStructure(chunk)) {
            return;
        }
        FileConfiguration buildConfig = getBuildConfig();
        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".version", version);

        saveBuildConfig(buildConfig);
    }

    public static List<Material> getValidSupplyTypes() {
        return List.of(Material.COAL);
    }

    public static boolean isValidSupplyType(Material material) {
        return getValidSupplyTypes().contains(material);
    }

    public static void editSupplies(Player player, Chunk chunk, Material material, int amount) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return;
        }

        if (!isValidSupplyType(material)) {
            return;
        }

        int currentAmount = config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".supplies." + material);
        config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".supplies." + material, currentAmount + amount);

        saveBuildConfig(config);
    }

    public static Integer getSuppliesFromStructure(Chunk chunk, Material material) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }

        if (!isValidSupplyType(material)) {
            return null;
        }
        return config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".supplies." + material);
    }

    public static void editResources(Player player, Chunk chunk, Material material, int amount) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return;
        }
        int currentAmount = config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".resources." + material);
        config.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".resources." + material, currentAmount + amount);

        saveBuildConfig(config);
    }

    public static Integer getResourcesFromStructure(Chunk chunk, Material material) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }
        return config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".resources." + material);
    }
}