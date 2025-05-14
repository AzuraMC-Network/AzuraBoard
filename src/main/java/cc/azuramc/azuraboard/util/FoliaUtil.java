package cc.azuramc.azuraboard.util;

import org.bukkit.plugin.Plugin;

/**
 * Utility class for providing appropriate scheduling methods
 * This class is a facade for the actual implementation
 *
 * @author an5w1r@163.com
 */
public class FoliaUtil {

    private static final SchedulerCompat scheduler = SchedulerFactory.getScheduler();
    
    public static final Boolean isFolia = scheduler.isFoliaServer();

    /**
     * Check if the server is running Folia
     *
     * @return true if the server is using Folia
     */
    public static boolean foliaCheck() {
        return scheduler.isFoliaServer();
    }

    /**
     * Run a task on the main thread (Folia compatible)
     *
     * @param plugin The plugin instance
     * @param task The task to run
     */
    public static void runTask(Plugin plugin, Runnable task) {
        scheduler.runTask(plugin, task);
    }

    /**
     * Run a delayed task on the main thread (Folia compatible)
     *
     * @param plugin The plugin instance
     * @param task The task to run
     * @param delay The delay in ticks
     */
    public static void runTaskLater(Plugin plugin, Runnable task, long delay) {
        scheduler.runTaskLater(plugin, task, delay);
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
        return scheduler.runTaskTimer(plugin, task, delay, period);
    }

    /**
     * Cancel a task (Folia compatible)
     *
     * @param task The task object
     */
    public static void cancelTask(Object task) {
        scheduler.cancelTask(task);
    }
} 