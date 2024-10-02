package cc.altoya.settlements.Commands.Domain;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainDomain implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("domain")) {
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /domain <claim|unclaim|list>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "claim":
                return CommandClaim.handle((Player) sender, args);
            case "unclaim": 
                return CommandUnclaim.handle((Player) sender, args);
            case "list":
                return CommandList.handle((Player) sender, args);
        }

        return true;
    }

}
