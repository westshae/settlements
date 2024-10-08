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

        List<Material> materials = BuildUtil.getAllResourceMaterials(chunk);
        for (Material material : materials) {
            Integer resourceAmount = BuildUtil.getResourcesFromStructure(chunk, material);
            if(resourceAmount == 0){
                continue;
            }
            BuildUtil.editResources(player, chunk, material, -resourceAmount);

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            NamespacedKey key = new NamespacedKey(GeneralUtil.getPlugin(), "resource_item");
            PersistentDataContainer data = meta.getPersistentDataContainer();

            meta.setDisplayName(ChatColor.AQUA + material.toString());
            item.setAmount(resourceAmount);
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
            data.set(key, PersistentDataType.STRING, material + "_resource_item");
            meta.setLore(
                    List.of(ChatColor.AQUA + "Resource Item", ChatColor.AQUA + BuildUtil.getMaterialLore(material)));

            player.getInventory().addItem(item);

            ChatUtil.sendSuccessMessage(player, "You have collected " + resourceAmount + " " + material);
        }
    }
}
