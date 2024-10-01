package cc.altoya.settlements.Commands.Settlement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainSettlement implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!command.getName().equalsIgnoreCase("settlement")) {
      return true;
    }
    if (!(sender instanceof Player)) {
      return true;
    }
    switch (args[0].toLowerCase()) {
      case "new":
        return CommandNew.handle((Player) sender, args);
      case "delete":
        return CommandDelete.handle((Player) sender, args);
      case "info":
        return CommandInfo.handle((Player) sender, args);
      case "invite":
        return CommandInvite.handle((Player) sender, args);
      case "uninvite":
        return CommandUninvite.handle((Player) sender, args);
      case "join":
        return CommandJoin.handle((Player) sender, args);
      case "leave":
        return CommandLeave.handle((Player) sender, args);
      case "kick":
        return CommandKick.handle((Player) sender, args);
    }

    return true;
  }
}
