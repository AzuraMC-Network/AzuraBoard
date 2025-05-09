package cc.azuramc.azuraboard;

import cc.azuramc.azuraboard.command.AzuraBoardCommand;
import cc.azuramc.azuraboard.listener.PlayerListener;
import cc.azuramc.azuraboard.manager.BoardManager;
import cc.azuramc.azuraboard.manager.ConfigManager;
import lombok.Getter;
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
    private boolean placeholderApiAvailable;
    @Getter
    private boolean viaBackwardsAvailable;

    @Override
    public void onEnable() {
        instance = this;

        loadPluginSupport();
        loadConfig();
        intiBoardManager();
        registerListeners();
        registerCommands();

        getLogger().info("AzuraBoard plugin has been successfully enabled!");
    }

    @Override
    public void onDisable() {
        // Clean up all scoreboards
        if (boardManager != null) {
            boardManager.unregisterAll();
        }
        
        getLogger().info("AzuraBoard plugin has been disabled!");
    }

    private void loadPluginSupport() {

        this.placeholderApiAvailable = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (placeholderApiAvailable) {
            getLogger().info("PlaceholderAPI detected, placeholders will be processed automatically!");
        } else {
            getLogger().info("PlaceholderAPI not found, placeholders will be ignored!");
        }

        this.viaBackwardsAvailable = Bukkit.getPluginManager().getPlugin("ViaBackwards") != null;
        if (viaBackwardsAvailable) {
            getLogger().info("ViaBackwards detected, support lower 1.13 client receive incomplete lines!");
        } else {
            getLogger().info("ViaBackwards not found, support lower 1.13 client receive incomplete lines will be ignored!");
        }
    }

    private void loadConfig() {
        this.configManager = new ConfigManager(this);
        configManager.loadConfig();
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
}
