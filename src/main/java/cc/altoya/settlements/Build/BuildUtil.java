package cc.altoya.settlements.Build;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import cc.altoya.settlements.Util.ChatUtil;
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

    public static String getItemPersistentValue(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null; // Return null if item is invalid or has no meta
        }

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(GeneralUtil.getPlugin(), "resource_item");

        return data.get(key, PersistentDataType.STRING);
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
        ChatUtil.sendSuccessBar(player, "Supplies are now at " + getSuppliesFromStructure(chunk, material));
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
        ChatUtil.sendSuccessBar(player, "Resources now at " + getResourcesFromStructure(chunk, material));
    }

    public static Integer getResourcesFromStructure(Chunk chunk, Material material) {
        FileConfiguration config = getBuildConfig();
        if (!isChunkStructure(chunk)) {
            return null;
        }
        return config.getInt("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".resources." + material);
    }


    public static Material getResourceFromBlock(Material blockType) {
        switch (blockType) {
            case COAL_ORE:
                return Material.COAL;
            case IRON_ORE:
                return Material.IRON_INGOT;
            case WHEAT:
                return Material.WHEAT;
            case SUGAR_CANE:
                return Material.SUGAR;
            case PUMPKIN:
                return Material.PUMPKIN;
            case MELON:
                return Material.MELON_SLICE;
            case LEVER:
                return Material.REDSTONE_LAMP;
            case STONE_PRESSURE_PLATE:
                return Material.REDSTONE_LAMP;
            case STONE_BUTTON:
                return Material.REDSTONE_LAMP;
            case OAK_LOG:
                return Material.OAK_PLANKS;
            default:
                return null;
        }
    }

    public static List<Material> getAllResourceMaterials(Chunk chunk) {
        return List.of(Material.COAL, Material.IRON_INGOT, Material.WHEAT, Material.SUGAR, Material.PUMPKIN, Material.MELON_SLICE, Material.REDSTONE_LAMP, Material.OAK_PLANKS);
    }

    public static String getMaterialLore(Material material) {
        switch (material) {
            case COAL:
                return "Used to fuel your factories";
            case IRON_INGOT:
                return "Used in your factories";
            case WHEAT:
                return "Used to feed your workers";
            case SUGAR:
                return "Used to speed up your workers";
            case PUMPKIN:
                return "Used to speed up your workers";
            case MELON_SLICE:
                return "Used to speed up your workers";
            case REDSTONE_LAMP:
                return "GDP Item";
            case OAK_PLANKS:
                return "Used to build structures";
            default:
                return "";
        }
    }

}