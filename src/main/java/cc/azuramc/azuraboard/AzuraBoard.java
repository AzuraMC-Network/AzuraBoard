package cc.azuramc.azuraboard;

import cc.azuramc.azuraboard.command.AzuraBoardCommand;
import cc.azuramc.azuraboard.listener.PlayerListener;
import cc.azuramc.azuraboard.manager.BoardManager;
import cc.azuramc.azuraboard.manager.ConfigManager;
import cc.azuramc.azuraboard.manager.LanguageManager;
import cc.azuramc.azuraboard.util.ChatColorUtil;
import cc.azuramc.azuraboard.util.SchedulerUtil;
import cc.azuramc.azuraboard.util.Metrics;
import cc.azuramc.azuraboard.util.VersionUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * AzuraBoard Plugin Main Class
 * A scoreboard plugin based on FastBoard library with PlaceholderAPI support
 * 
 * @author an5w1r@163.com
 */
public final class AzuraBoard extends JavaPlugin {

    @Getter
    private static AzuraBoard instance;
    @Getter
    private ConfigManager configManager;
    @Getter
    private BoardManager boardManager;
    @Getter
    private LanguageManager languageManager;
    @Getter
    private boolean placeholderApiAvailable;
    @Getter
    private boolean viaBackwardsAvailable;
    @Getter
    private boolean foliaServer;
    @Getter @Setter
    private ChatColorUtil chatColorUtil;

    @Override
    public void onEnable() {
        instance = this;

        // Load configuration first
        loadConfig();
        
        // Load language files
        loadLanguage();
        
        // Check server version
        checkServerVersion();
        
        // Load other components
        loadPluginSupport();
        intiBoardManager();
        registerListeners();
        registerCommands();

        // load bStats
        loadMetrics();

        getLogger().info(languageManager.getMessage("console.enabled"));
    }

    @Override
    public void onDisable() {
        // Clean up all scoreboards
        if (boardManager != null) {
            boardManager.unregisterAll();
        }
        
        if (languageManager != null) {
            getLogger().info(languageManager.getMessage("console.disabled"));
        } else {
            getLogger().info("AzuraBoard plugin has been disabled!");
        }
    }
    
    /**
     * Check server version and output related information
     */
    private void checkServerVersion() {
        // Check if server is running Folia
        this.foliaServer = SchedulerUtil.isFolia;
        if (foliaServer) {
            getLogger().info(languageManager.getMessage("console.folia-detected"));
        }

        String version = VersionUtil.getServerVersion();
        boolean supportsRgb = VersionUtil.isSupportsRgb;
        
        // Output server version
        getLogger().info(languageManager.getFormattedMessage("console.server-version", "%version%", version));
        
        // Output RGB support information from language files
        if (supportsRgb) {
            getLogger().info(languageManager.getMessage("console.rgb-supported"));
        } else {
            getLogger().info(languageManager.getMessage("console.rgb-unsupported"));
        }
    }

    private void loadPluginSupport() {
        this.placeholderApiAvailable = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (placeholderApiAvailable) {
            getLogger().info(languageManager.getMessage("console.placeholder-found"));
        } else {
            getLogger().info(languageManager.getMessage("console.placeholder-not-found"));
        }

        this.viaBackwardsAvailable = Bukkit.getPluginManager().getPlugin("ViaBackwards") != null;
        if (viaBackwardsAvailable) {
            getLogger().info(languageManager.getMessage("console.viabackwards-found"));
        } else {
            getLogger().info(languageManager.getMessage("console.viabackwards-not-found"));
        }
    }

    private void loadConfig() {
        this.configManager = new ConfigManager(this);
        configManager.loadConfig();
    }
    
    private void loadLanguage() {
        this.languageManager = new LanguageManager(this);
        languageManager.loadLanguages();
    }

    private void intiBoardManager() {
        this.boardManager = new BoardManager(this);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("azuraboard")).setExecutor(new AzuraBoardCommand(this));
    }

    private void loadMetrics() {
        new Metrics(this, 25845);
    }
}
