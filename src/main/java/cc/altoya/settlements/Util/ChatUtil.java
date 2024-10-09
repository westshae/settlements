package cc.altoya.settlements.Util;

import java.util.Map;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatUtil {
  private static String getBoxedString(String middleText, ChatColor color){
    return ChatColor.GRAY + "[" +  color + middleText + ChatColor.GRAY + "] ";
  }

  private static String formatErrorMessage(String message){
    return getBoxedString("!!", ChatColor.RED) + ChatColor.RED + message;
  }

  private static String formatSuccessMessage(String message){
    return getBoxedString("+", ChatColor.DARK_GREEN) + ChatColor.GREEN + message;
  }


  public static void sendErrorMessage(Player player, String message) {
    player.sendMessage(formatErrorMessage(message));
  }

  public static void sendErrorBar(Player player, String message){
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(formatErrorMessage(message)));
  }

  public static void sendSuccessMessage(Player player, String message) {
    player.sendMessage(formatSuccessMessage(message));
  }

  public static void sendSuccessBar(Player player, String message){
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(formatSuccessMessage(message)));
  }

  public static void sendAllianceMessage(Player player, String allianceName, String message) {
    String formattedMessage =  getBoxedString(allianceName, ChatColor.AQUA) + ChatColor.AQUA + message;
    player.sendMessage(formattedMessage);
  }

  public static void sendCommandHelpMessage(Player player, String command, Map<String, String> commands) {
    String header = getBoxedString(command + "Help", ChatColor.AQUA);
    player.sendMessage(header);

    for (Map.Entry<String, String> entry : commands.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();

      String commandMessage = ChatColor.GREEN + key + ": " + ChatColor.WHITE + value;
      player.sendMessage(commandMessage);
    }
  }
}
