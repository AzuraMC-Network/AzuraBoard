package cc.azuramc.azuraboard.listener;

import cc.azuramc.azuraboard.AzuraBoard;
import cc.azuramc.azuraboard.util.SchedulerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
} 