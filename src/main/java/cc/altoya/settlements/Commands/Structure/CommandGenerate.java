package cc.altoya.settlements.Commands.Structure;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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
        generateBuildingFromBlueprint(sender, args[1]);
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
    
        FileConfiguration config = StructureUtil.getStructureConfig();
    
        String type = config.getString("structures.blueprints." + blueprintName + ".type");
    
        StructureUtil.createNewStructure(player, chunk, type);
    
        // Get the block list from the saved structure (contains both block type and block data)
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
        for (String blockString : blockList) {
            // Convert string representation to a block
            Block block = StructureUtil.turnStringIntoBlock(blockString);
    
            if (block != null) {
                // Calculate relative positions
                int relativeY = block.getY() - originalY + playerHeight;
                int x = block.getX();
                int z = block.getZ();
    
                int adjustedX = (chunk.getX() * 16 + x) - originX;
                int adjustedZ = (chunk.getZ() * 16 + z) - originZ;
    
                Location blockLocation = new Location(chunk.getWorld(), adjustedX, relativeY, adjustedZ);
    
                // Place the block with its type
                // Block targetBlock = chunk.getWorld().getBlockAt(blockLocation);
                // targetBlock.setType(block.getType()); // Set block type from the converted block
                // targetBlock.setBlockData(block.getBlockData());
    
                // Check if the block is in the interactive blocks list
                if (interactiveBlocks.contains(blockString)) {
                    StructureUtil.placeInteractiveBlock(player, blockLocation, block.getType(), block.getBlockData());
                } else {
                    StructureUtil.placeStructureBlock(player, blockLocation, block.getType(), block.getBlockData());
                }
            } else {
                ChatUtil.sendErrorMessage(player, "Error converting block from string: " + blockString);
            }
        }
    
        ChatUtil.sendSuccessMessage(player, "Successfully generated structure from blueprint: " + blueprintName);
    }
}
