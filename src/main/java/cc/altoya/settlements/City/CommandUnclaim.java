package cc.altoya.settlements.City;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Build.BuildUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUnclaim {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
      return true;
    }
    removePlayerChunk(sender, sender.getLocation().getChunk());
    return true;
  }

  private static void removePlayerChunk(Player player, Chunk chunk) {
    if (!CityUtil.isChunkClaimed(chunk)) {
      ChatUtil.sendErrorMessage(player, "This chunk isn't claimed.");
      return;
    }
    if (!CityUtil.doesPlayerOwnChunk(player, chunk)) {
      ChatUtil.sendErrorMessage(player, "You aren't the owner of this claim.");
      return;
    }
    if(BuildUtil.isChunkStructure(chunk)){
      ChatUtil.sendErrorMessage(player, "This chunk is a structure, remove it first before unclaiming.");
      return;
    }

    CityUtil.unclaimChunk(player, chunk);
    ChatUtil.sendSuccessMessage(player, "Claim unclaimed.");
  }

}
