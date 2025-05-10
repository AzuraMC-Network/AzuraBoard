package cc.azuramc.azuraboard.util;

import org.bukkit.Bukkit;

/**
 * Version Utility Class for checking server version and feature compatibility
 * 
 * @author an5w1r@163.com
 */
public final class VersionUtil {

    private static Boolean supportsRgb = null;
    private static Integer majorVersion = null;
    private static Integer minorVersion = null;

    /**
     * Check if the server supports RGB colors (Minecraft 1.16+)
     * 
     * @return true if the server version is 1.16 or higher, false otherwise
     */
    public static boolean supportsRgb() {
        if (supportsRgb == null) {
            supportsRgb = checkVersion(1, 16);
        }
        return supportsRgb;
    }

    /**
     * Check if the server version is greater than or equal to the specified version
     *
     * @param major The major version number
     * @param minor The minor version number
     * @return true if the server version is greater than or equal to the specified version
     */
    public static boolean checkVersion(int major, int minor) {
        extractVersion();
        
        if (majorVersion == null || minorVersion == null) {
            return false;
        }
        
        return majorVersion > major || (majorVersion == major && minorVersion >= minor);
    }

    /**
     * Get the server version string
     * 
     * @return The server version string, e.g. "1.16.5"
     */
    public static String getServerVersion() {
        return Bukkit.getBukkitVersion().split("-")[0];
    }

    /**
     * Extract the major and minor version numbers from the server version
     */
    private static void extractVersion() {
        if (majorVersion != null && minorVersion != null) {
            return;
        }
        
        try {
            String[] versionParts = getServerVersion().split("\\.");
            majorVersion = Integer.parseInt(versionParts[0]);
            minorVersion = Integer.parseInt(versionParts[1]);
        } catch (Exception e) {
            // Set default values if version parsing fails
            majorVersion = 1;
            minorVersion = 0;
        }
    }
} 