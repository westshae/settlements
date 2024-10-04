package cc.altoya.settlements.Commands.Structure;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.StructureUtil;

public class CommandLoad {
    public static boolean handle(Player sender, String[] args) {
        if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 3,
                "/structure load {subcommand} {blueprintname} {var}")) {
            return true;
        }
        switch (args[1]) {
            case "create":
                createBoilerplate(sender, args[2], args[3]);
                break;
            case "firstpoint":
                setFirstPoint(sender, args[2]);
                break;
            case "secondpoint":
                setSecondPoint(sender, args[2]);
                break;
            case "interactive":
                setInteractiveBlock(sender, args[2]);
                break;
            case "save":
                saveCurrentStructure(sender, args[2]);
                break;

        }
        return true;
    }

    private static void createBoilerplate(Player player, String name, String type) {
        FileConfiguration config = StructureUtil.getStructureConfig();
        if (StructureUtil.doesStructureNameExist(name)) {
            ChatUtil.sendErrorMessage(player, "A structure named this already exists.");
            return;
        }
        config.set("structures.blueprints." + name + ".type", type);
        ChatUtil.sendSuccessMessage(player, "Blueprint boilerplate created.");
        StructureUtil.saveStructureConfig(config);
    }

    private static void setFirstPoint(Player player, String name) {
        FileConfiguration config = StructureUtil.getStructureConfig();
        if (!StructureUtil.doesStructureNameExist(name)) {
            ChatUtil.sendErrorMessage(player, "This structure doesn't exist, use create first.");
            return;
        }
        Block targettedBlock = player.getTargetBlock(null, 10);
        if(!(targettedBlock.getX() % 16 == 0 && targettedBlock.getZ() % 16 == 0)){
            ChatUtil.sendErrorMessage(player, "FirstBlock of a blueprint must be placed at a chunk's [0, ~, 0]");
            return;
        }
        String blockKey = GeneralUtil.getKeyFromBlock(targettedBlock);
        config.set("structures.blueprints." + name + ".firstpoint", blockKey);
        ChatUtil.sendSuccessMessage(player, "Successfully added first point to blueprint.");
        StructureUtil.saveStructureConfig(config);
    }

    private static void setSecondPoint(Player player, String name) {
        FileConfiguration config = StructureUtil.getStructureConfig();
        if (!StructureUtil.doesStructureNameExist(name)) {
            ChatUtil.sendErrorMessage(player, "This structure doesn't exist, use create first.");
            return;
        }
        Block targettedBlock = player.getTargetBlock(null, 10);
        if(!(targettedBlock.getX() % 16 == -1 && targettedBlock.getZ() % 16 == -1)){//-1 is 15 from this setup
            ChatUtil.sendErrorMessage(player, "SecondBlock of a blueprint must be placed at a chunk's [15, ~, 15]");
            return;
        }
        Block firstPointBlock = GeneralUtil.getBlockFromKey(config.getString("structures.blueprints." + name +".firstpoint"));

        if(targettedBlock.getX() != firstPointBlock.getX() + 15 && targettedBlock.getZ() != firstPointBlock.getZ() + 15){
            ChatUtil.sendErrorMessage(player, "Second point must be in the same chunk as first point.");
            return;
        }
        String blockKey = GeneralUtil.getKeyFromBlock(targettedBlock);
        config.set("structures.blueprints." + name + ".secondpoint", blockKey);
        ChatUtil.sendSuccessMessage(player, "Successfully added second point to blueprint.");
        StructureUtil.saveStructureConfig(config);

    }

    private static void setInteractiveBlock(Player player, String name) {
        FileConfiguration config = StructureUtil.getStructureConfig();
        if (!StructureUtil.doesStructureNameExist(name)) {
            ChatUtil.sendErrorMessage(player, "This structure doesn't exist, use create first.");
            return;
        }
        Block targettedBlock = player.getTargetBlock(null, 10);
        String blockKey = GeneralUtil.getKeyFromBlock(targettedBlock);
        List<String> interactiveBlocks = GeneralUtil
                .createListFromString((String) config.get("structures.blueprints." + name + ".interactive"));
        interactiveBlocks.add(blockKey);
        config.set("structures.blueprints." + name + ".interactive",
                GeneralUtil.createStringFromList(interactiveBlocks));
        ChatUtil.sendSuccessMessage(player, "Added block to blueprints interactive block list.");
        StructureUtil.saveStructureConfig(config);
    }

private static void saveCurrentStructure(Player player, String name) {
    FileConfiguration config = StructureUtil.getStructureConfig();

    if (!StructureUtil.doesStructureNameExist(name)) {
        ChatUtil.sendErrorMessage(player, "This structure doesn't exist, use create first.");
        return;
    }

    String firstPointKey = config.getString("structures.blueprints." + name + ".firstpoint");
    String secondPointKey = config.getString("structures.blueprints." + name + ".secondpoint");

    if (firstPointKey == null || secondPointKey == null) {
        ChatUtil.sendErrorMessage(player, "Both first and second points must be set before saving the structure.");
        return;
    }

    Block firstBlock = GeneralUtil.getBlockFromKey(firstPointKey);
    Block secondBlock = GeneralUtil.getBlockFromKey(secondPointKey);

    if (firstBlock == null || secondBlock == null) {
        ChatUtil.sendErrorMessage(player, "Error retrieving blocks from the given points.");
        return;
    }

    int x1 = firstBlock.getX();
    int y1 = firstBlock.getY();
    int z1 = firstBlock.getZ();

    int x2 = secondBlock.getX();
    int y2 = secondBlock.getY();
    int z2 = secondBlock.getZ();

    List<String> blockList = new ArrayList<>();

    for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) {
                Block block = firstBlock.getWorld().getBlockAt(x, y, z);
                
                if (block.getType() == Material.AIR) {
                    continue;
                }
                String blockString = StructureUtil.turnBlockIntoString(block);
                blockList.add(blockString);
            }
        }
    }

    config.set("structures.blueprints." + name + ".blocks", blockList);
    config.set("structures.blueprints." + name + ".originalY", firstBlock.getY());
    StructureUtil.saveStructureConfig(config);

    ChatUtil.sendSuccessMessage(player, "Successfully saved structure with " + blockList.size() + " blocks.");
}
}
