package cc.altoya.settlements.Commands.Structure;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.StructureUtil;

public class CommandGenerate {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 2,
                "/structure generate {type}")) {
            return true;
        }
        switch (args[1]) {
            case "mine":
                generateMineBuilding(sender, sender.getLocation().getChunk());
                break;
            default: 
                generateBuildingFromBlueprint(sender, args[1]);
        }
        return true;
    }

    private static void generateBuildingFromBlueprint(Player player, String blueprintName) {
        Chunk chunk = player.getLocation().getChunk();
    
        if (StructureUtil.isChunkStructure(chunk)) {
            ChatUtil.sendErrorMessage(player, "There is already a structure in this chunk.");
            return;
        }
    
        if (!StructureUtil.doesStructureNameExist(blueprintName)) {
            ChatUtil.sendErrorMessage(player, "This blueprint doesn't exist.");
            return;
        }
    
        // Retrieve the structure configuration
        FileConfiguration config = StructureUtil.getStructureConfig();
    
        // Get the block list from the saved structure
        List<String> blockList = config.getStringList("structures.blueprints." + blueprintName + ".blocks");
    
        // Retrieve the list of interactive blocks from the configuration
        List<String> interactiveBlocks = GeneralUtil.createListFromString((String) config.get("structures.blueprints." + blueprintName + ".interactive"));
    
        // Get the player's height
        int playerHeight = player.getLocation().getBlockY();
        int originalY = config.getInt("structures.blueprints." + blueprintName + ".originalY");
    
        // Retrieve the first block's position as the origin for the structure
        String firstBlockKey = config.getString("structures.blueprints." + blueprintName + ".firstpoint");
        Block firstBlock = GeneralUtil.getBlockFromKey(firstBlockKey);
    
        if (firstBlock == null) {
            ChatUtil.sendErrorMessage(player, "Error retrieving the first block from key: " + firstBlockKey);
            return;
        }
    
        // Get the original coordinates of the first block
        int originX = firstBlock.getX();
        int originZ = firstBlock.getZ();
    
        // Loop through the block list and place each block
        for (String blockKey : blockList) {
            Block block = GeneralUtil.getBlockFromKey(blockKey);
    
            if (block != null) {
                // Calculate the relative positions to place the blocks
                int relativeY = block.getY() - originalY + playerHeight;
                int x = block.getX(); // Get the original X position from the block
                int z = block.getZ(); // Get the original Z position from the block
    
                // Calculate the adjusted positions based on the first block as the origin
                int adjustedX = (chunk.getX() * 16 + x) - originX;
                int adjustedZ = (chunk.getZ() * 16 + z) - originZ;
    
                // Create the location for placing the block
                Location blockLocation = new Location(chunk.getWorld(), adjustedX, relativeY, adjustedZ);
    
                // Check if the block is in the interactive blocks list
                if (interactiveBlocks.contains(blockKey)) {
                    StructureUtil.placeInteractiveBlock(player, blockLocation, block.getType());
                } else {
                    StructureUtil.placeStructureBlock(player, blockLocation, block.getType());
                }
            } else {
                ChatUtil.sendErrorMessage(player, "Error retrieving block from key: " + blockKey);
            }
        }
    
        ChatUtil.sendSuccessMessage(player, "Successfully generated structure from blueprint: " + blueprintName);
    }
    
    
    private static void generateMineBuilding(Player player, Chunk chunk) {
        if (StructureUtil.isChunkStructure(chunk)) {
            ChatUtil.sendErrorMessage(player, "There is already a structure in this chunk.");
            return;
        }
        StructureUtil.createNewStructure(player, "mine");
        Location centerLocation = player.getLocation();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                Location adjustedLocation = centerLocation.clone().add(i, 0, j);
                StructureUtil.placeInteractiveBlock(player, adjustedLocation, Material.COAL_ORE);
            }
        }

        ChatUtil.sendSuccessMessage(player, "Mine successfully generated");
    }

}
