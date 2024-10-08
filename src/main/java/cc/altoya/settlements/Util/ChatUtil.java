package cc.altoya.settlements.Util;

import java.util.Map;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatUtil {
  private static ChatColor pluginPrimaryColour = ChatColor.DARK_AQUA;
  private static ChatColor pluginSecondaryColour = ChatColor.AQUA;
  private static ChatColor errorColour = ChatColor.DARK_RED;
  private static ChatColor successColour = ChatColor.DARK_GREEN;
  private static ChatColor allianceColour = ChatColor.BLUE;

  public static void sendErrorMessage(Player player, String message) {
    String formattedMessage = "" + errorColour + "[!] " + message;
    player.sendMessage(formattedMessage);
  }

  public static void sendSuccessMessage(Player player, String message) {
    String formattedMessage = "" + successColour + "[+] " + message;
    player.sendMessage(formattedMessage);
  }

  public static void sendAllianceMessage(Player player, String allianceName, String message) {
    String formattedMessage = "" + allianceColour + "[" + allianceName + "] " + message;
    player.sendMessage(formattedMessage);
  }

  public static void sendCommandHelpMessage(Player player, String command, Map<String, String> commands) {
    String header = "" + successColour + "[" + command + " Help]";
    player.sendMessage(header);

    for (Map.Entry<String, String> entry : commands.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();

      String commandMessage = successColour + key + ": " + value;
      player.sendMessage(commandMessage);
    }
  }

  public static void sendErrorBar(Player player, String message){
    String formattedMessage = "" + errorColour + "[!] " + message;
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(formattedMessage));
  }

  public static ChatColor getPrimaryColour() {
    return pluginPrimaryColour;
  }

  public static ChatColor getSecondaryColour() {
    return pluginSecondaryColour;
  }
}
