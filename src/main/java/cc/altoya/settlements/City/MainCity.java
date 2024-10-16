package cc.altoya.settlements.City;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;

public class MainCity implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("city")) {
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }

        if (args.length == 0) {
            ChatUtil.sendErrorMessage((Player) sender, "Requires more arguments. Use /city help");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                return CommandList.handle((Player) sender, args);
            case "expand":
                return CommandExpand.handle((Player) sender, args);
            case "info":
                return CommandInfo.handle((Player) sender, args);
            case "hire":
                return CommandHire.handle((Player) sender, args);
            case "resources":
                return CommandResources.handle((Player) sender, args);
            case "help":
                return CommandHelp.handle((Player) sender, args);
        }

        return true;
    }

}
