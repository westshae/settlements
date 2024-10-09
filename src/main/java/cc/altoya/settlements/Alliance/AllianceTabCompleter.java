package cc.altoya.settlements.Alliance;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllianceTabCompleter implements TabCompleter {

    private final List<String> subCommands = Arrays.asList("create","delete","invite","kick","join", "leave","info","chat","help");

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
        }else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "create":
                    completions.add("{allianceName}");
                    break;
                case "invite":
                    completions.add("{username}");
                    break;
                case "join":
                    completions.add("{allianceName}");
                    break;
                default:
                    break;
            }
            return completions;
        }

        return null;
    }
}
