package cc.altoya.settlements.Build;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.ItemUtil;

public class CommandNew {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2,
                "/build new {blueprintName}")) {
            return true;
        }
        if(!takePlayerCosts(sender, args[1])){
            return true;
        }
        setBuildConfig(sender, args[1]);
        generateBuildingFromBlueprint(sender, args[1]);
        return true;
    }

    public static boolean takePlayerCosts(Player player, String blueprintName){
        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return false;
        }
        FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();
        ConfigurationSection section = blueprintConfig.getConfigurationSection("blueprints." + blueprintName + ".cost");

        for(String key : section.getKeys(false)){
            Material resource = Material.getMaterial(key);
            Integer amount = Integer.parseInt(section.getString(key));
            if(!player.getInventory().contains(resource)){
                ChatUtil.sendErrorMessage(player, "You're missing the following resource: " + resource.toString());
                return false;
            }

            boolean tookResources = false;
            for(ItemStack item : player.getInventory().getContents()){
                if(item == null){
                    continue;
                }
                if(!item.getType().equals(resource)){
                    continue;
                }
                if(item.getAmount() < amount){
                    continue;
                }
                if(!ItemUtil.isItemCustom(item)){
                    continue;
                }
                tookResources = true;
                item.setAmount(item.getAmount() - amount);
                break;
            }
            if(!tookResources){
                ChatUtil.sendErrorMessage(player, "You don't have the right amount of: " + resource.toString() + ". You need " + amount + ", which must be a server-made resource.");
                return false;
            }
        }

        return true;
    }

    public static void setBuildConfig(Player player, String blueprintName){
        Integer version = BlueprintUtil.getVersion(blueprintName);

        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }
        FileConfiguration buildConfig = BuildUtil.getBuildConfig();

        int playerHeight = player.getLocation().getBlockY();
        Chunk chunk = player.getLocation().getChunk();
        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".owner", GeneralUtil.getKeyFromPlayer(player));
        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".blueprintName", blueprintName);
        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".version", version);
        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".playerHeight", playerHeight);

        FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();

        Block nonRelativeFirstBlock = chunk.getBlock(0, playerHeight, 0);

        String secondBlockKey = blueprintConfig.getString("blueprints." + blueprintName + ".second");
        Block blueprintSecondBlock = BlueprintUtil.turnStringIntoBlock(secondBlockKey);

        Location nonRelativeSecondLocation = BlueprintUtil.getNonRelativeLocation(nonRelativeFirstBlock, blueprintSecondBlock.getLocation());
        Block nonRelativeSecondBlock = chunk.getWorld().getBlockAt(nonRelativeSecondLocation.getBlockX(), nonRelativeSecondLocation.getBlockY(), nonRelativeSecondLocation.getBlockZ());
        
        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".first", BlueprintUtil.turnBlockIntoString(nonRelativeFirstBlock, nonRelativeFirstBlock.getLocation()));
        buildConfig.set("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".second", BlueprintUtil.turnBlockIntoString(nonRelativeSecondBlock, nonRelativeSecondLocation));

        BuildUtil.saveBuildConfig(buildConfig);

    }

    public static void generateBuildingFromBlueprint(Player player, String blueprintName) {
        Chunk chunk = player.getLocation().getChunk();
        if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }

        FileConfiguration blueprintConfig = BlueprintUtil.getBlueprintConfig();
        FileConfiguration buildConfig = BuildUtil.getBuildConfig();

        String buildFirstKey = buildConfig.getString("builds." + GeneralUtil.getKeyFromChunk(chunk) + ".first");
        Block buildFirstBlock = BlueprintUtil.turnStringIntoBlock(buildFirstKey);

        List<String> blocks = blueprintConfig.getStringList("blueprints." + blueprintName + ".blocks");

        BuildUtil.placeBlocksFromStringList(blocks, buildFirstBlock);

        ChatUtil.sendSuccessMessage(player, "Successfully generated structure from blueprint: " + blueprintName);
    }
}
