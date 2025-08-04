package cc.azuramc.azuraboard.util;

import cc.azuramc.azuraboard.AzuraBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Chat Color Utility Class for AzuraBoard
 * Provides methods to convert color codes in strings and collections
 *
 * @author an5w1r@163.com
 */
public final class ChatColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    private static final Pattern HEX_PATTERN_2 = Pattern.compile("\\{#([A-Fa-f0-9]{6})}");


    /**
     * Convert RGB hex color codes to Minecraft supported format
     *
     * @param message The message containing hex color codes
     * @return The converted message
     */
    private static String translateHexColorCodes(String message) {
        if (message == null) {
            return "";
        }

        // Check if server supports RGB colors and if it's enabled in config
        if (!isRgbSupported()) {
            return message;
        }

        // &#RRGGBB
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hexCode = matcher.group(1);

            StringBuilder replacement = new StringBuilder("§x");
            for (char c : hexCode.toCharArray()) {
                replacement.append("§").append(c);
            }

            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);

        // {#RRGGBB}
        message = buffer.toString();
        matcher = HEX_PATTERN_2.matcher(message);
        buffer = new StringBuffer();

        while (matcher.find()) {
            String hexCode = matcher.group(1);

            StringBuilder replacement = new StringBuilder("§x");
            for (char c : hexCode.toCharArray()) {
                replacement.append("§").append(c);
            }

            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    /**
     * Check if RGB colors are supported and enabled
     *
     * @return true if RGB colors are supported by the server (1.16+) and enabled in config
     */
    private static boolean isRgbSupported() {
        // Check if RGB is supported by the server
        return VersionUtil.isSupportsRgb;
    }

    /**
     * Convert color codes in a string
     * Converts '&' color codes to the Minecraft color code format
     * Supports RGB colors in format &#RRGGBB or {#RRGGBB} for Minecraft 1.16+
     *
     * @param string The string to colorize
     * @return The colorized string, or empty string if input is null
     */
    public static String color(String string) {
        if (string == null) {
            return "";
        }

        String translatedHex = translateHexColorCodes(string);

        return ChatColor.translateAlternateColorCodes('&', translatedHex);
    }

    /**
     * Convert color codes in a list of strings
     * Converts '&' color codes to the Minecraft color code format for each string in the list
     * Supports RGB colors in format &#RRGGBB or {#RRGGBB} for Minecraft 1.16+
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
                toReturn.add(color(line));
            } else {
                toReturn.add("");
            }
        }

        return toReturn;
    }

    /**
     * Convert color codes in an array of strings
     * Converts '&' color codes to the Minecraft color code format for each string in the array
     * Supports RGB colors in format &#RRGGBB or {#RRGGBB} for Minecraft 1.16+
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
                colored[i] = color(lines[i]);
            } else {
                colored[i] = "";
            }
        }
        return colored;
    }

    public static String parse(Player player, String string) {
        if (AzuraBoard.getInstance().isPlaceholderApiAvailable()) {
            try {
                string = color(PlaceholderAPI.setPlaceholders(player, string));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            string = color(string);
        }

        return string;
    }

    public static List<String> parse(Player player, List<String> lines) {
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            toReturn.add(parse(player, color(line)));
        }

        return toReturn;
    }
}
