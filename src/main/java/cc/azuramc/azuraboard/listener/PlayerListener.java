package cc.azuramc.azuraboard.listener;

import cc.azuramc.azuraboard.AzuraBoard;
import cc.azuramc.azuraboard.util.SchedulerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Player Event Listener for AzuraBoard
 * Handles scoreboard creation and removal on player join/quit
 * 
 * @author an5w1r@163.com
 */
public class PlayerListener implements Listener {

    /**
     * Plugin instance
     */
    private final AzuraBoard plugin;

    /**
     * Constructor for PlayerListener
     * 
     * @param plugin The plugin instance
     */
    public PlayerListener(AzuraBoard plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles player join event
     * Creates a scoreboard for the player
     * 
     * @param event The player join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        SchedulerUtil.runTaskLater(plugin, () -> plugin.getBoardManager().createBoard(player), 5L);
    }

    /**
     * Handles player quit event
     * Removes the player's scoreboard
     * 
     * @param event The player quit event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Remove the player's scoreboard
        plugin.getBoardManager().removeBoard(player);
    }
    
    /**
     * Handles player world change event
     * Updates the player's scoreboard if world-specific configuration is enabled
     * 
     * @param event The player world change event
     */
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        
        // Only update if world-specific configuration is enabled
        if (plugin.getConfigManager().isEnableWorldSpecific()) {
            // Update the player's scoreboard with new world configuration
            SchedulerUtil.runTaskLater(plugin, () -> {
                plugin.getBoardManager().refreshBoard(player);
            }, 1L);
        }
    }
}