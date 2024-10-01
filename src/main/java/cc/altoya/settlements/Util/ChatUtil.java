package cc.altoya.settlements.Util;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ChatUtil {
  private static ChatColor pluginPrimaryColour = ChatColor.DARK_AQUA;
  private static ChatColor pluginSecondaryColour = ChatColor.AQUA;
  private static ChatColor errorColour = ChatColor.DARK_RED;
  private static ChatColor successColour = ChatColor.DARK_GREEN;

  public static void sendErrorMessage(Player player, String message){
    String formattedMessage = "" + errorColour + "[!] " + message;
    player.sendMessage(formattedMessage);
  }

  public static void sendSuccessMessage(Player player, String message){
    String formattedMessage = "" + successColour + "[+] " + message;
    player.sendMessage(formattedMessage);
  }

  public static ChatColor getPrimaryColour(){
    return pluginPrimaryColour;
  }

  public static ChatColor getSecondaryColour(){
    return pluginSecondaryColour;
  }
}
