package cc.altoya.settlements.Blueprint;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlueprintTabCompleter implements TabCompleter {

    private final List<String> subCommands = Arrays.asList("create", "first", "second", "save", "upgrade", "housing",
            "cost", "teleport", "help");

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
                case "create":
                    completions.add("{blueprintName}");
                    break;
                case "first":
                    completions.add("{blueprintName}");
                    break;
                case "second":
                    completions.add("{blueprintName}");
                    break;
                case "save":
                    completions.add("{blueprintName}");
                    break;
                case "dupe":
                    completions.add("{blueprintName}");
                    break;
                case "upgrade":
                    completions.add("{originalBlueprintName}");
                    break;
                case "delete":
                    completions.add("{blueprintName}");
                    break;
                case "housing":
                    completions.add("{blueprintName}");
                    break;
                case "cost":
                    completions.add("{blueprintName}");
                    break;
                case "teleport":
                    completions.add("{blueprintName}");
                    break;

                default:
                    break;
            }
            return completions;
        } else if (args.length == 3) {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "housing":
                    completions.add("{amount}");
                    break;
                case "cost":
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