package cc.altoya.settlements.Build;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;

public class MainBuild implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("build")) {
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }

        if (args.length == 0) {
            ChatUtil.sendErrorMessage((Player) sender, "Usage: /build <generate|delete|refresh|collect|help>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "generate":
                return CommandGenerate.handle((Player) sender, args);
            case "delete":
                return CommandDelete.handle((Player) sender, args);
            case "refresh":
                return CommandRefresh.handle((Player) sender, args);
            case "collect":
                return CommandCollect.handle((Player) sender, args);
            case "help":
                return CommandHelp.handle((Player) sender, args);
        }

        return true;
    }

}
