package cc.altoya.settlements.Alliance;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainAlliance implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("alliance")) {
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /alliance <create|delete|invite|kick|join|info|chat>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                return CommandCreate.handle((Player) sender, args);
            case "delete": 
                return CommandDelete.handle((Player) sender, args);
            case "invite":
                return CommandInvite.handle((Player) sender, args);
            case "join":
                return CommandJoin.handle((Player) sender, args);
            case "info":
                return CommandInfo.handle((Player) sender, args);
            case "chat":
                return CommandChat.handle((Player) sender, args);
        }

        return true;
    }

}
