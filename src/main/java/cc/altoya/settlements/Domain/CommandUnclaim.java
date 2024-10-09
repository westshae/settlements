package cc.altoya.settlements.Domain;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Build.BuildUtil;
import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandUnclaim {
  public static boolean handle(Player sender, String[] args) {
    if (!GeneralUtil.handlePermissionsAndArguments(sender, "settlements", "child", args, 1, "/domain unclaim")) {
      return true;
    }
    removePlayerChunk(sender, sender.getLocation().getChunk());
    return true;
  }

  private static void removePlayerChunk(Player player, Chunk chunk) {
    FileConfiguration config = DomainUtil.getDomainConfig();
    if (!DomainUtil.isChunkClaimed(chunk)) {
      ChatUtil.sendErrorMessage(player, "This chunk isn't claimed.");
      return;
    }
    if (!DomainUtil.doesPlayerOwnChunk(player, chunk)) {
      ChatUtil.sendErrorMessage(player, "You aren't the owner of this claim.");
      return;
    }
    if(BuildUtil.isChunkStructure(chunk)){
      ChatUtil.sendErrorMessage(player, "This chunk is a structure, remove it first before unclaiming.");
      return;
    }
    config.set("domains.claimed_tiles." + GeneralUtil.getKeyFromChunk(chunk), null);
    String playerPath = "domains." + GeneralUtil.getKeyFromPlayer(player) + ".claims";
    List<String> claims = config.getStringList(playerPath);
    claims.removeIf(claim -> claim.equals(GeneralUtil.getKeyFromChunk(chunk)));
    config.set(playerPath, claims);

    DomainUtil.saveDomainConfig(config);
    ChatUtil.sendSuccessMessage(player, "Claim unclaimed.");
  }

}
