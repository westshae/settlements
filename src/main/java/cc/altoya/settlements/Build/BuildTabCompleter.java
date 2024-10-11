package cc.altoya.settlements.Build;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildTabCompleter implements TabCompleter {

    private final List<String> subCommands = Arrays.asList("new", "delete", "refresh", "collect", "plot", "upgrade",
            "workers", "giveall", "transform", "collectall", "supply", "help");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
            return completions;
        } else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "new":
                    completions.add("{blueprintName}");
                    break;
                case "workers":
                    completions.add("{amount}");
                    break;
                case "giveall":
                    completions.add("{amount}");
                    break;
                case "supply":
                    completions.add("{amount}");
                    break;
                default:
                    break;
            }
            return completions;
        }

        return null;
    }
}