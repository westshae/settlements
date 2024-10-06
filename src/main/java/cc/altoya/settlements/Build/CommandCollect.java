package cc.altoya.settlements.Build;

import java.util.List;

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

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Domain.DomainUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import net.md_5.bungee.api.ChatColor;

public class CommandCollect {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/build collect")) {
            return true;
        }

        collectResources(sender, sender.getLocation().getChunk());
        return true;
    }

    private static void collectResources(Player player, Chunk chunk) {
        if (!DomainUtil.doesPlayerOwnChunk(player, chunk)) {
            ChatUtil.sendErrorMessage(player, "You don't own this structure");
            return;
        }

        String resourceType = BlueprintUtil.getResourceType(BuildUtil.getStructureBlueprintName(chunk));
        Integer resourceAmount = BuildUtil.getResourcesFromStructure(chunk);
        BuildUtil.editResources(player, chunk, -resourceAmount);
        ItemStack item = new ItemStack(Material.AIR);
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(GeneralUtil.getPlugin(), "resource_item");
        PersistentDataContainer data = meta.getPersistentDataContainer();

        ChatUtil.sendErrorMessage(player, resourceType);
        switch (resourceType) {
            case "wheat":
                item.setType(Material.WHEAT);
                meta.setDisplayName(ChatColor.AQUA + "Wheat");
                meta.setLore(List.of(ChatColor.AQUA + "Resource Item", ChatColor.AQUA + "Used to feed your workers"));
                data.set(key, PersistentDataType.STRING, "wheat_resource_item");

                ChatUtil.sendSuccessMessage(player, "You have collected " + resourceAmount + " wheat.");
                break;
            case "sugar":
                item.setType(Material.SUGAR);
                meta.setDisplayName(ChatColor.AQUA + "Sugar");
                meta.setLore(
                        List.of(ChatColor.AQUA + "Resource Item", ChatColor.AQUA + "Used to speed up your workers"));
                data.set(key, PersistentDataType.STRING, "sugar_resource_item");
                ChatUtil.sendSuccessMessage(player, "You have collected " + resourceAmount + " sugar.");

                break;
            case "coal":
                item.setType(Material.COAL);
                meta.setDisplayName(ChatColor.AQUA + "Coal");
                meta.setLore(List.of(ChatColor.AQUA + "Resource Item", ChatColor.AQUA + "Used to fuel your factories"));
                data.set(key, PersistentDataType.STRING, "coal_resource_item");

                ChatUtil.sendSuccessMessage(player, "You have collected " + resourceAmount + " coal.");

                break;
            case "iron":
                item.setType(Material.IRON_INGOT);
                meta.setDisplayName(ChatColor.AQUA + "Iron");
                meta.setLore(List.of(ChatColor.AQUA + "Resource Item", ChatColor.AQUA + "Used in your factories"));
                data.set(key, PersistentDataType.STRING, "iron_resource_item");

                ChatUtil.sendSuccessMessage(player, "You have collected " + resourceAmount + " iron.");

                break;
            case "manufacturedGoods":
                item.setType(Material.RAIL);
                meta.setDisplayName(ChatColor.AQUA + "Manufactured Goods");
                meta.setLore(List.of(ChatColor.AQUA + "Resource Item",
                        ChatColor.AQUA + "Product of your factories, indicates GDP"));
                data.set(key, PersistentDataType.STRING, "manufacturedGoods_resource_item");

                ChatUtil.sendSuccessMessage(player, "You have collected " + resourceAmount + " manufactured goods.");

                break;
            case "militaryGoods":
                item.setType(Material.DISPENSER);
                meta.setDisplayName(ChatColor.AQUA + "Military Goods");
                meta.setLore(List.of(ChatColor.AQUA + "Resource Item",
                        ChatColor.AQUA + "Product of your factories, indicates military strength"));
                data.set(key, PersistentDataType.STRING, "militaryGoods_resource_item");

                ChatUtil.sendSuccessMessage(player, "You have collected " + resourceAmount + " military goods.");

                break;
        }
        item.setAmount(resourceAmount);
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        player.getInventory().addItem(item);

    }

}
