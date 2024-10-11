package cc.altoya.settlements.Build;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BuildTabCompleter implements TabCompleter {

        @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        List<String> commandsUnformatted = List.copyOf(BuildUtil.getBuildCommands().keySet());

        List<String[]> commands = new ArrayList<String[]>();

        for(String commandUnformatted : commandsUnformatted){
            commands.add(commandUnformatted.split(" "));
        }

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (String[] commandSplit : commands) {
                if (commandSplit[1].toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(commandSplit[1].toLowerCase());
                }
            }
            return completions;
        } else{
            int commandIndex = -1;
            int currentIndex = 0;
            for (String[] commandSplit : commands) {
                if(commandSplit[1].toLowerCase().equals(args[0].toLowerCase())){
                    commandIndex = currentIndex;
                    break;
                }
                currentIndex++;
            }

            if(args.length < commands.get(commandIndex).length){
                completions.add(commands.get(commandIndex)[args.length]);
                return completions;
            }
        }
        return null;
    }

}