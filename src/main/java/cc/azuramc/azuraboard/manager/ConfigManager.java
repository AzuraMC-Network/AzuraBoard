package cc.azuramc.azuraboard.manager;

import cc.azuramc.azuraboard.AzuraBoard;
import cc.azuramc.azuraboard.util.ChatColorUtil;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * Configuration Manager for AzuraBoard
 * Handles loading and accessing configuration values
 * 
 * @author an5w1r@163.com
 */
public class ConfigManager {

    private final AzuraBoard plugin;
    private FileConfiguration config;

    @Getter private String title;
    @Getter private List<String> lines;
    @Getter private int updateInterval;
    @Getter private boolean usePlaceholders;
    @Getter private String defaultLanguage;
    @Getter private boolean enableRgbColors;

    /**
     * Constructor for ConfigManager
     *
     * @param plugin The plugin instance
     */
    public ConfigManager(AzuraBoard plugin) {
        this.plugin = plugin;
    }

    /**
     * Load configuration from file
     */
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        
        // Load title
        this.title = ChatColorUtil.color(config.getString("scoreboard.title", "&b&lAzura&f&lBoard"));
        
        // Load lines
        this.lines = ChatColorUtil.color(config.getStringList("scoreboard.lines"));
        
        // Load update interval
        this.updateInterval = config.getInt("settings.update-interval", 20);
        
        // Load placeholder setting
        this.usePlaceholders = config.getBoolean("settings.use-placeholders", true);
        
        // Load language setting
        this.defaultLanguage = config.getString("settings.language", "en_US");
        
        // Load RGB color support setting
        this.enableRgbColors = config.getBoolean("settings.enable-rgb-colors", true);
    }
    
    /**
     * Reload configuration from file
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }
}