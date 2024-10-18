package cc.altoya.settlements.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;

public class MainMap implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!command.getName().equalsIgnoreCase("map")) {
      return true;
    }
    if (!(sender instanceof Player)) {
      return true;
    }

    if (args.length == 0) {
      ChatUtil.sendErrorMessage((Player) sender, "Requires more arguments. Use /map help");
      return true;
    }

    switch (args[0].toLowerCase()) {
      case "city":
        return CommandCity.handle((Player) sender, args);
      case "help":
        return CommandHelp.handle((Player) sender, args);
    }

    return true;
  }

}
