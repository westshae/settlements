package cc.altoya.settlements.Build;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Blueprint.BlueprintUtil;
import cc.altoya.settlements.City.CityUtil;
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

    if (BuildUtil.isChunkStructure(chunk)) {
      ChatUtil.sendErrorMessage(player, "This chunk is already a structure");
      return;
    }

    if (!CityUtil.isChunkClaimed(chunk)) {
      ChatUtil.sendErrorMessage(player, "This chunk isn't claimed. You can only build structures in claimed land.");
      return;
    }


    if (CityUtil.isChunkClaimed(chunk) && !CityUtil.doesPlayerOwnChunk(player, chunk)) {
      ChatUtil.sendErrorMessage(player, "This chunk is owned by someone else.");
      return;
    }

    if(!BlueprintUtil.canAffordBlueprint(player, blueprintName)){
      ChatUtil.sendErrorMessage(player, "You can't afford this blueprint. Check you have enough.");
      return;
    }
    CityUtil.deductResourcesForBlueprint(player, blueprintName);

    BuildUtil.setBuildConfig(player, blueprintName);

    BuildUtil.generateBuildingFromBlueprint(player, blueprintName);

    ChatUtil.sendSuccessMessage(player, "Successfully generated structure from blueprint: " + blueprintName);
  }
}
