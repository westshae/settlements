package cc.altoya.settlements.Commands.Structure;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainStructure implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("structure")) {
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /structure <generate|delete>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "generate":
                return CommandGenerate.handle((Player) sender, args);
            case "delete":
                return CommandDelete.handle((Player) sender, args);
        }

        return true;
    }

}
