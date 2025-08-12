package cc.azuramc.azuraboard.manager;

import cc.azuramc.azuraboard.AzuraBoard;
import cc.azuramc.azuraboard.util.ChatColorUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Configuration Manager for AzuraBoard
 * Handles loading and accessing configuration values
 *
 * @author an5w1r@163.com
 */
public class ConfigManager {

    private final AzuraBoard plugin;
    private FileConfiguration config;

    // Basic settings
    @Getter private int updateInterval;
    @Getter private boolean usePlaceholders;
    @Getter private String defaultLanguage;

    // Feature switches
    @Getter private boolean enableWorldSpecific;
    @Getter private boolean enablePermissionBased;

    // Priority settings
    @Getter private int permissionBasedPriority;
    @Getter private int worldSpecificPriority;
    @Getter private int defaultPriority;

    // Scoreboard configurations
    @Getter private ScoreboardConfig defaultScoreboard;
    @Getter private Map<String, ScoreboardConfig> worldScoreboards;
    @Getter private Map<String, PermissionScoreboardConfig> permissionScoreboards;

    /**
     * Load configuration from file
     */
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        // Load basic settings
        this.updateInterval = config.getInt("settings.update-interval", 20);
        this.usePlaceholders = config.getBoolean("settings.use-placeholders", true);
        this.defaultLanguage = config.getString("settings.language", "en_US");

        // Load feature switches
        this.enableWorldSpecific = config.getBoolean("settings.enable-world-specific", true);
        this.enablePermissionBased = config.getBoolean("settings.enable-permission-based", true);

        // Load priority settings
        this.permissionBasedPriority = config.getInt("settings.priority-order.permission-based", 100);
        this.worldSpecificPriority = config.getInt("settings.priority-order.world-specific", 50);
        this.defaultPriority = config.getInt("settings.priority-order.default", 0);

        // Load default scoreboard
        loadDefaultScoreboard();

        // Load world-specific scoreboards
        loadWorldScoreboards();

        // Load permission-based scoreboards
        loadPermissionScoreboards();
    }

    /**
     * Load default scoreboard configuration
     */
    private void loadDefaultScoreboard() {
        String title = config.getString("default-scoreboard.title", "&b&lAzura&f&lBoard");
        List<String> lines = config.getStringList("default-scoreboard.lines");
        this.defaultScoreboard = new ScoreboardConfig(title, lines);
    }

    /**
     * Constructor for ConfigManager
     *
     * @param plugin The plugin instance
     */
    public ConfigManager(AzuraBoard plugin) {
        this.plugin = plugin;
    }

    /**
     * Load world-specific scoreboard configurations
     */
    private void loadWorldScoreboards() {
        this.worldScoreboards = new HashMap<>();

        if (!enableWorldSpecific) {
            return;
        }

        ConfigurationSection worldSection = config.getConfigurationSection("world-scoreboards");
        if (worldSection == null) {
            return;
        }

        for (String worldName : worldSection.getKeys(false)) {
            ConfigurationSection worldConfig = worldSection.getConfigurationSection(worldName);
            if (worldConfig != null) {
                String title = worldConfig.getString("title", defaultScoreboard.getTitle());
                List<String> lines = worldConfig.getStringList("lines");
                if (lines.isEmpty()) {
                    lines = defaultScoreboard.getLines();
                }
                worldScoreboards.put(worldName, new ScoreboardConfig(title, lines));
            }
        }
    }

    /**
     * Load permission-based scoreboard configurations
     */
    private void loadPermissionScoreboards() {
        this.permissionScoreboards = new HashMap<>();

        if (!enablePermissionBased) {
            return;
        }

        ConfigurationSection permissionSection = config.getConfigurationSection("permission-scoreboards");
        if (permissionSection == null) {
            return;
        }

        for (String configName : permissionSection.getKeys(false)) {
            ConfigurationSection permConfig = permissionSection.getConfigurationSection(configName);
            if (permConfig != null) {
                String permission = permConfig.getString("permission");
                if (permission != null && !permission.isEmpty()) {
                    String title = permConfig.getString("title", defaultScoreboard.getTitle());
                    List<String> lines = permConfig.getStringList("lines");
                    if (lines.isEmpty()) {
                        lines = defaultScoreboard.getLines();
                    }
                    int priority = permConfig.getInt("priority", 0); // Default priority if not specified
                    permissionScoreboards.put(configName, new PermissionScoreboardConfig(permission, title, lines, priority));
                }
            }
        }
    }

    /**
     * Get the appropriate scoreboard configuration for a player
     * Based on priority order: permission-based > world-specific > default
     *
     * @param player The player to get configuration for
     * @return The appropriate scoreboard configuration
     */
    public ScoreboardConfig getScoreboardConfig(Player player) {
        List<ScoreboardCandidate> candidates = new ArrayList<>();

        // Add default scoreboard
        candidates.add(new ScoreboardCandidate(defaultScoreboard, defaultPriority));

        // Add world-specific scoreboard if enabled and available
        if (enableWorldSpecific) {
            String worldName = player.getWorld().getName();
            ScoreboardConfig worldConfig = worldScoreboards.get(worldName);
            if (worldConfig != null) {
                candidates.add(new ScoreboardCandidate(worldConfig, worldSpecificPriority));
            }
        }

        // Add permission-based scoreboard if enabled and player has permission
        if (enablePermissionBased) {
            for (PermissionScoreboardConfig permConfig : permissionScoreboards.values()) {
                if (player.hasPermission(permConfig.getPermission())) {
                    // Use individual permission scoreboard priority instead of global priority
                    candidates.add(new ScoreboardCandidate(permConfig, permConfig.getPriority()));
                }
            }
        }

        // Sort by priority (highest first) and return the highest priority config
        return candidates.stream()
                .max(Comparator.comparingInt(ScoreboardCandidate::getPriority))
                .map(ScoreboardCandidate::getConfig)
                .orElse(defaultScoreboard);
    }

    /**
     * Get title for a specific player (for backward compatibility)
     *
     * @param player The player
     * @return The title for the player
     */
    public String getTitle(Player player) {
        return getScoreboardConfig(player).getTitle();
    }

    /**
     * Get lines for a specific player (for backward compatibility)
     *
     * @param player The player
     * @return The lines for the player
     */
    public List<String> getLines(Player player) {
        return getScoreboardConfig(player).getLines();
    }

    /**
     * Get default title (for backward compatibility)
     *
     * @return The default title
     */
    public String getTitle() {
        return defaultScoreboard.getTitle();
    }

    /**
     * Get default lines (for backward compatibility)
     *
     * @return The default lines
     */
    public List<String> getLines() {
        return defaultScoreboard.getLines();
    }

    /**
     * Scoreboard configuration class
     */
    public static class ScoreboardConfig {
        @Getter private final String title;
        @Getter private final List<String> lines;

        public ScoreboardConfig(String title, List<String> lines) {
            this.title = ChatColorUtil.color(title);
            this.lines = ChatColorUtil.color(lines);
        }
    }

    /**
     * Permission-based scoreboard configuration class
     */
    public static class PermissionScoreboardConfig extends ScoreboardConfig {
        @Getter private final String permission;
        @Getter private final int priority;

        public PermissionScoreboardConfig(String permission, String title, List<String> lines, int priority) {
            super(title, lines);
            this.permission = permission;
            this.priority = priority;
        }
    }

    /**
     * Helper class for scoreboard candidate with priority
     */
    private static class ScoreboardCandidate {
        @Getter private final ScoreboardConfig config;
        @Getter private final int priority;

        public ScoreboardCandidate(ScoreboardConfig config, int priority) {
            this.config = config;
            this.priority = priority;
        }
    }

    /**
     * Reload configuration from file
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }
}
