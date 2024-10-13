package cc.altoya.settlements.City;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandExpand {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", args, 1)) {
      return true;
    }
    expandPlayerCity(sender);
    return true;
  }

  private static void expandPlayerCity(Player player) {
    List<String> chunks = CityUtil.getPlayersChunks(player);
    for (String chunkKey : chunks) {
      Chunk chunk = GeneralUtil.getChunkFromKey(chunkKey);
      int[][] directions = {
          { -1, 0 }, // Left
          { 1, 0 }, // Right
          { 0, -1 }, // Down
          { 0, 1 } // Up
      };
      Random random = new Random();

      int randomIndex = random.nextInt(directions.length);
      int randomX = chunk.getX() + directions[randomIndex][0];
      int randomZ = chunk.getZ() + directions[randomIndex][1];
      World world = Bukkit.getWorlds().get(0);
      Chunk newChunk = world.getChunkAt(randomX, randomZ);
      if(!CityUtil.isChunkClaimed(newChunk)){
        CityUtil.claimChunk(player, newChunk);
        ChatUtil.sendSuccessMessage(player, "City expanded to new chunk X:" + randomX + " Z:" + randomZ);
        return;
      }

    }
  }
}
