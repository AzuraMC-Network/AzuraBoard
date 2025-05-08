package cc.azuramc.azuraboard.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Chat Color Utility Class for AzuraBoard
 * Provides methods to convert color codes in strings and collections
 * 
 * @author an5w1r@163.com
 */
public final class ChatColorUtil {

    /**
     * Convert color codes in a string
     * Converts '&' color codes to the Minecraft color code format
     * 
     * @param string The string to colorize
     * @return The colorized string, or empty string if input is null
     */
    public static String color(String string) {
        if (string == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Convert color codes in a list of strings
     * Converts '&' color codes to the Minecraft color code format for each string in the list
     * 
     * @param lines The list of strings to colorize
     * @return The list of colorized strings, or empty list if input is null
     */
    public static List<String> color(List<String> lines) {
        List<String> toReturn = new ArrayList<>();
        
        if (lines == null) {
            return toReturn;
        }
        
        for (String line : lines) {
            if (line != null) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
            } else {
                toReturn.add("");
            }
        }

        return toReturn;
    }

    /**
     * Convert color codes in an array of strings
     * Converts '&' color codes to the Minecraft color code format for each string in the array
     * 
     * @param lines The array of strings to colorize
     * @return The array of colorized strings, or empty array if input is null
     */
    public static String[] color(String[] lines) {
        if (lines == null) {
            return new String[0];
        }
        
        String[] colored = new String[lines.length];
        for (int i = 0; i < lines.length; i++) {
            if (lines[i] != null) {
                colored[i] = ChatColor.translateAlternateColorCodes('&', lines[i]);
            } else {
                colored[i] = "";
            }
        }
        return colored;
    }
}
