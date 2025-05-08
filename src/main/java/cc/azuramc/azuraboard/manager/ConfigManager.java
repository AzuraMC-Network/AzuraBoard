package cc.azuramc.azuraboard.manager;

import cc.azuramc.azuraboard.AzuraBoard;
import cc.azuramc.azuraboard.util.ChatColorUtil;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
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
        List<String> configLines = config.getStringList("scoreboard.lines");
        this.lines = new ArrayList<>();
        
        for (String line : configLines) {
            this.lines.add(ChatColorUtil.color(line));
        }
        
        // Load update interval
        this.updateInterval = config.getInt("settings.update-interval", 20);
        
        // Load placeholder setting
        this.usePlaceholders = config.getBoolean("settings.use-placeholders", true);
    }
    
    /**
     * Reload configuration from file
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }

}