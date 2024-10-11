package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.City.CityUtil;
import cc.altoya.settlements.Item.ItemUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandNew {
  public static boolean handle(Player player, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(player, "settlements", args, 2)) {
      return true;
    }

    generateBuilding(player, args[1]);
    return true;
  }

  public static void generateBuilding(Player player, String blueprintName) {
    Chunk chunk = player.getLocation().getChunk();

    if (!BlueprintUtil.doesBlueprintExist(blueprintName)) {
      ChatUtil.sendErrorMessage(player, "Blueprint doesn't exist.");
      return;
    }

    if (!BuildUtil.isBuildAreaEmpty(player, blueprintName)) {
      ChatUtil.sendErrorMessage(player, "The require required isn't empty of blocks.");
      return;
    }

    if (BuildUtil.isChunkStructure(chunk)) {
      ChatUtil.sendErrorMessage(player, "This chunk is already a structure");
      return;
    }

    if (CityUtil.isChunkClaimed(chunk) && !CityUtil.doesPlayerOwnChunk(player, chunk)) {
      ChatUtil.sendErrorMessage(player, "This chunk is owned by someone else.");
      return;
    }

    if (!CityUtil.isChunkClaimed(chunk)) {
      CityUtil.claimChunk(player, chunk);
    }

    ConfigurationSection costs = BlueprintUtil.getBlueprintCosts(blueprintName);

    if (costs != null && !ItemUtil.hasCosts(player, costs)) {
      ChatUtil.sendErrorMessage(player, "You're missing the resources required for this structure.");
      return;
    }

    if(costs != null){
      ItemUtil.removeItems(player, costs);
    }

    BuildUtil.setBuildConfig(player, blueprintName);

    BuildUtil.generateBuildingFromBlueprint(player, blueprintName);

    ChatUtil.sendSuccessMessage(player, "Successfully generated structure from blueprint: " + blueprintName);
  }
}
