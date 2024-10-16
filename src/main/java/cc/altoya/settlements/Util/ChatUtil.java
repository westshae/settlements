package cc.altoya.settlements.Util;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;

public class ChatUtil {
  private static String getBoxedString(String middleText, ChatColor color) {
    return ChatColor.GRAY + "[" + color + middleText + ChatColor.GRAY + "] ";
  }

  private static String formatErrorMessage(String message) {
    return getBoxedString("!!!", ChatColor.RED) + ChatColor.RED + message;
  }

  private static String formatSuccessMessage(String message) {
    return getBoxedString("+", ChatColor.DARK_GREEN) + ChatColor.GREEN + message;
  }

  public static void sendErrorMessage(Player player, String message) {
    player.sendMessage(Component.text(formatErrorMessage(message)));
  }

  public static void sendErrorBar(Player player, String message) {
    player.sendActionBar(Component.text(formatErrorMessage(message)));
  }

  public static void sendSuccessMessage(Player player, String message) {
    player.sendMessage(Component.text(formatSuccessMessage(message)));
  }

  public static void sendSuccessBar(Player player, String message) {
    player.sendActionBar(Component.text(formatSuccessMessage(message)));
  }

  public static void sendAllianceMessage(Player player, String allianceName, String message) {
    String formattedMessage = getBoxedString(allianceName, ChatColor.AQUA) + ChatColor.AQUA + message;
    player.sendMessage(Component.text(formattedMessage));
  }

  public static void sendCommandHelpMessage(Player player, String command, Map<String, String> commands) {
    String header = getBoxedString(command + " help", ChatColor.GREEN);
    player.sendMessage(header);

    for (Map.Entry<String, String> entry : commands.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();

      String commandMessage = ChatColor.GREEN + key + ": " + ChatColor.WHITE + value;
      player.sendMessage(commandMessage);
    }
  }

  public static void sendPlayerListedMessage(Player player, String header, List<String> messages){
    String headerFormatted = getBoxedString(header, ChatColor.GREEN);
    player.sendMessage(headerFormatted);
    for(String message: messages){
      player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + message);
    }
  }

  public static void sendAllPlayersAllianceJoin(Player player, String allianceName) {
    ChatColor color = null;
    switch (allianceName.toLowerCase()) {
      case "red":
        color = ChatColor.RED;
        break;
      case "blue":
        color = ChatColor.BLUE;
        break;
      case "green":
        color = ChatColor.GREEN;
        break;
      case "yellow":
        color = ChatColor.YELLOW;
        break;

      default:
        return;
    }
    String displayName = PlainTextComponentSerializer.plainText().serialize(player.displayName());
    String message = "" + color + ChatColor.BOLD + displayName + " has joined the " + allianceName + " alliance!";
    for (Player serverPlayer : Bukkit.getServer().getOnlinePlayers()) {
      serverPlayer.sendMessage(message);
    }
  }
}
