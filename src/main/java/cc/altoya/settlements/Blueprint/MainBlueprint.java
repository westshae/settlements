package cc.altoya.settlements.Blueprint;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;

public class MainBlueprint implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("blueprint")) {
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }

        if (args.length == 0) {
            ChatUtil.sendErrorMessage((Player) sender, "Usage: /blueprint <create|first|second|interactive|save|help>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                return CommandCreate.handle((Player) sender, args);
            case "first":
                return CommandFirst.handle((Player) sender, args);
            case "second":
                return CommandSecond.handle((Player) sender, args);
            case "interactive":
                return CommandInteractive.handle((Player) sender, args);
            case "save":
                return CommandSave.handle((Player) sender, args);
            case "help":
                return CommandHelp.handle((Player) sender, args);
        }

        return true;
    }

}
