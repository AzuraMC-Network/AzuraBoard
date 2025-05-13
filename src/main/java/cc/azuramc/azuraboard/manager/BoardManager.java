package cc.azuramc.azuraboard.manager;

import cc.azuramc.azuraboard.AzuraBoard;
import cc.azuramc.azuraboard.util.FoliaUtil;
import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Board Manager for AzuraBoard
 * Handles scoreboard creation, removal and updates
 * 
 * @author an5w1r@163.com
 */
public class BoardManager {

    private final AzuraBoard plugin;
    
    /** Map of player UUID to FastBoard */
    private final Map<UUID, FastBoard> boards;
    
    /** Set of player UUIDs with disabled scoreboards */
    private final Set<UUID> toggledOff;
    
    /** Task for updating scoreboards (BukkitTask or Folia ScheduledTask) */
    private Object updateTask;

    /**
     * Constructor for BoardManager
     * 
     * @param plugin The plugin instance
     */
    public BoardManager(AzuraBoard plugin) {
        this.plugin = plugin;
        this.boards = new ConcurrentHashMap<>();
        this.toggledOff = new HashSet<>();
        
        // Start scoreboard update task
        startTask();
    }

    /**
     * Create a scoreboard for a player
     * 
     * @param player The player to create a scoreboard for
     */
    public void createBoard(Player player) {
        if (toggledOff.contains(player.getUniqueId())) {
            return;
        }

        final boolean useRgbSupport = plugin.getConfigManager().isEnableRgbColors();
        
        FastBoard board = new FastBoard(player) {
            @Override
            public boolean hasLinesMaxLength() {
                // Return false if RGB support is enabled and ViaBackwards compatibility is needed
                // This allows sending longer lines to support RGB colors
                if (plugin.isViaBackwardsAvailable()) {
                    return false;
                }
                
                // Return false if RGB support is enabled (1.16+)
                // Return true if RGB support is disabled (force limit line length)
                return !useRgbSupport;
            }
        };

        board.updateTitle(plugin.getConfigManager().getTitle());
        
        boards.put(player.getUniqueId(), board);
        updateBoard(player);
    }

    /**
     * Remove a player's scoreboard
     * 
     * @param player The player to remove the scoreboard from
     */
    public void removeBoard(Player player) {
        UUID uuid = player.getUniqueId();
        FastBoard board = boards.remove(uuid);
        
        if (board != null) {
            board.delete();
        }
    }
    
    /**
     * Toggle a player's scoreboard visibility
     * 
     * @param player The player to toggle the scoreboard for
     * @return true if the scoreboard is now toggled off, false if it is now visible
     */
    public boolean toggleBoard(Player player) {
        UUID uuid = player.getUniqueId();
        
        if (toggledOff.contains(uuid)) {
            toggledOff.remove(uuid);
            createBoard(player);
            // Scoreboard is now visible (toggled on)
            return false;
        } else {
            toggledOff.add(uuid);
            removeBoard(player);
            // Scoreboard is now hidden (toggled off)
            return true;
        }
    }
    
    /**
     * Update a player's scoreboard
     * 
     * @param player The player to update the scoreboard for
     */
    public void updateBoard(Player player) {
        UUID uuid = player.getUniqueId();
        FastBoard board = boards.get(uuid);
        
        if (board == null) {
            return;
        }
        
        List<String> lines = new ArrayList<>();
        for (String line : plugin.getConfigManager().getLines()) {
            if (plugin.isPlaceholderApiAvailable()) {
                try {
                    line = PlaceholderAPI.setPlaceholders(player, line);
                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to process placeholders: " + e.getMessage());
                }
            }
            lines.add(line);
        }
        
        board.updateLines(lines.toArray(new String[0]));
    }
    
    /**
     * Update all scoreboards for online players
     */
    public void updateAllBoards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.isFoliaServer()) {
                if (player.isOnline()) {
                    FoliaUtil.runTask(plugin, () -> updateBoard(player));
                }
            } else {
                updateBoard(player);
            }
        }
    }
    
    /**
     * Unregister all scoreboards
     * Called when plugin disables
     */
    public void unregisterAll() {
        for (FastBoard board : boards.values()) {
            board.delete();
        }
        
        boards.clear();
        
        if (updateTask != null) {
            FoliaUtil.cancelTask(updateTask);
            updateTask = null;
        }
    }
    
    /**
     * Start the scoreboard update task
     */
    private void startTask() {
        int interval = plugin.getConfigManager().getUpdateInterval();

        updateTask = FoliaUtil.runTaskTimer(plugin, this::updateAllBoards, 20L, interval);
    }
    
    /**
     * Reload the scoreboard update task
     * Called when configuration is reloaded
     */
    public void reloadTask() {
        if (updateTask != null) {
            FoliaUtil.cancelTask(updateTask);
        }
        
        startTask();
    }
    
    /**
     * Check if a player has toggled their scoreboard off
     * 
     * @param player The player to check
     * @return true if the player has toggled off their scoreboard
     */
    public boolean isToggled(Player player) {
        return toggledOff.contains(player.getUniqueId());
    }
} 