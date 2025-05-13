package cc.azuramc.azuraboard.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

/**
 * Utility class for detecting Folia and providing appropriate scheduling methods
 *
 * @author an5w1r@163.com
 */
public class FoliaUtil {

    public static final Boolean isFolia = foliaCheck();

    /**
     * Check if the server is running Folia
     *
     * @return true if the server is using Folia
     */
    public static boolean foliaCheck() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Run a task on the main thread (Folia compatible)
     *
     * @param plugin The plugin instance
     * @param task The task to run
     */
    public static void runTask(Plugin plugin, Runnable task) {
        if (isFolia) {
            try {
                // Use the global region scheduler in Folia
                Bukkit.getGlobalRegionScheduler().execute(plugin, task);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to execute task in Folia", e);
            }
        } else {
            // Use the synchronous scheduler in regular Bukkit/Spigot
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    /**
     * Run a delayed task on the main thread (Folia compatible)
     *
     * @param plugin The plugin instance
     * @param task The task to run
     * @param delay The delay in ticks
     */
    public static void runTaskLater(Plugin plugin, Runnable task, long delay) {
        if (isFolia) {
            try {
                // Use the global region scheduler in Folia
                Bukkit.getGlobalRegionScheduler().runDelayed(plugin, (s) -> task.run(), delay);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to execute delayed task in Folia", e);
            }
        } else {
            // Use the synchronous scheduler in regular Bukkit/Spigot
            Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        }
    }

    /**
     * Run a repeating task on the main thread (Folia compatible)
     *
     * @param plugin The plugin instance
     * @param task The task to run
     * @param delay The initial delay in ticks
     * @param period The period in ticks
     * @return The created task
     */
    public static Object runTaskTimer(Plugin plugin, Runnable task, long delay, long period) {
        if (isFolia) {
            try {
                // Use the global region scheduler in Folia
                return Bukkit.getGlobalRegionScheduler().runAtFixedRate(
                    plugin,
                    (s) -> task.run(),
                        // Convert to milliseconds
                        delay * 50,
                        period * 50
                );
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to execute repeating task in Folia", e);
                return null;
            }
        } else {
            // Use the synchronous scheduler in regular Bukkit/Spigot
            return Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
        }
    }

    /**
     * Cancel a task (Folia compatible)
     *
     * @param task The task object
     */
    public static void cancelTask(Object task) {
        if (task == null) {
            return;
        }

        if (isFolia) {
            if (task instanceof io.papermc.paper.threadedregions.scheduler.ScheduledTask) {
                ((io.papermc.paper.threadedregions.scheduler.ScheduledTask) task).cancel();
            }
        } else {
            if (task instanceof org.bukkit.scheduler.BukkitTask) {
                ((org.bukkit.scheduler.BukkitTask) task).cancel();
            }
        }
    }
} 