package cc.altoya.settlements.Alliance;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;

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
            ChatUtil.sendErrorMessage((Player) sender, "Usage: /alliance <create|delete|invite|kick|join|leave|info|chat|help>");
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
            case "leave":
                return CommandLeave.handle((Player) sender, args);
            case "info":
                return CommandInfo.handle((Player) sender, args);
            case "chat":
                return CommandChat.handle((Player) sender, args);
            case "help":
                return CommandHelp.handle((Player) sender, args);
        }

        return true;
    }

}
