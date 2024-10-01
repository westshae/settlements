package cc.altoya.settlements.Commands.Chunk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainChunk implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!command.getName().equalsIgnoreCase("chunk")) {
      return true;
    }
    if (!(sender instanceof Player)) {
      return true;
    }
    switch (args[0].toLowerCase()) {
      case "claim":
        return CommandClaim.handle((Player) sender, args);
      case "unclaim":
        return CommandUnclaim.handle((Player) sender, args);
      case "unclaimall":
        return CommandUnclaimAll.handle((Player) sender, args);
      case "trust":
        return CommandTrust.handle((Player) sender, args);
      case "untrust":
        return CommandUntrust.handle((Player) sender, args);
      case "list":
        return CommandList.handle((Player) sender, args);
    }

    return true;
  }
}
