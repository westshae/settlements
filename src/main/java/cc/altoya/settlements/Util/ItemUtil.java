package cc.altoya.settlements.Util;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

public class ItemUtil {
  public static String getItemPersistentValue(ItemStack item) {
    if (item == null || !item.hasItemMeta()) {
      return null; // Return null if item is invalid or has no meta
    }

    ItemMeta meta = item.getItemMeta();
    PersistentDataContainer data = meta.getPersistentDataContainer();
    NamespacedKey key = new NamespacedKey(GeneralUtil.getPlugin(), "settlements_resource_item");

    return data.get(key, PersistentDataType.STRING);
  }

  public static boolean isItemCustom(ItemStack item){
    if(!item.hasItemMeta()){
      return false;
    }
    String metadata = getItemPersistentValue(item);
    return metadata.equals(item.getType().toString());
  }

  public static void givePlayerCustomItem(Player player, Material material, int amount) {
    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();

    NamespacedKey key = new NamespacedKey(GeneralUtil.getPlugin(), "settlements_resource_item");
    PersistentDataContainer data = meta.getPersistentDataContainer();

    meta.setDisplayName("" + ChatColor.YELLOW + ChatColor.MAGIC + "O" + ChatColor.RESET + ChatColor.YELLOW
        + ChatColor.BOLD + formatItemId(material.toString()) + ChatColor.RESET + ChatColor.YELLOW + ChatColor.MAGIC
        + "O");
    item.setAmount(amount);
    meta.addEnchant(Enchantment.UNBREAKING, 1, true);
    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    data.set(key, PersistentDataType.STRING, material.toString());
    meta.setLore(
        List.of(
            getMaterialLore(material),
            "",
            "" + ChatColor.GRAY + ChatColor.BOLD + "Resource Item"));
    item.setItemMeta(meta);

    player.getInventory().addItem(item);
  }

  public static List<Material> getAllResourceBlocks(Chunk chunk) {
    return List.of(Material.COAL_ORE, Material.IRON_ORE, Material.WHEAT, Material.SUGAR_CANE, Material.PUMPKIN,
        Material.MELON, Material.LEVER, Material.STONE_PRESSURE_PLATE, Material.STONE_BUTTON, Material.OAK_LOG);
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

  public static List<Material> getAllResourceMaterials() {
    return List.of(Material.COAL, Material.IRON_INGOT, Material.WHEAT, Material.SUGAR, Material.PUMPKIN,
        Material.MELON_SLICE, Material.REDSTONE_LAMP, Material.OAK_PLANKS);
  }

  public static String getMaterialLore(Material material) {
    switch (material) {
      case COAL:
        return ChatColor.YELLOW + "Used to fuel your factories";
      case IRON_INGOT:
        return ChatColor.YELLOW + "Used in your factories";
      case WHEAT:
        return ChatColor.YELLOW + "Used to feed your workers";
      case SUGAR:
        return ChatColor.YELLOW + "Used to speed up your workers";
      case PUMPKIN:
        return ChatColor.YELLOW + "Used to speed up your workers";
      case MELON_SLICE:
        return ChatColor.YELLOW + "Used to speed up your workers";
      case REDSTONE_LAMP:
        return ChatColor.YELLOW + "GDP Item";
      case OAK_PLANKS:
        return ChatColor.YELLOW + "Used to build structures";
      default:
        return "";
    }
  }

  public static String formatItemId(String itemName) {
    // Split the string by underscores
    String[] parts = itemName.split("_");
    StringBuilder formattedName = new StringBuilder();

    // Loop through each part, capitalize the first letter and add to the result
    for (String part : parts) {
      if (formattedName.length() > 0) {
        formattedName.append(" "); // Add a space before appending the next word
      }
      // Capitalize the first letter and add the rest of the string in lower case
      formattedName.append(part.charAt(0)).append(part.substring(1).toLowerCase());
    }

    return formattedName.toString();
  }
}
