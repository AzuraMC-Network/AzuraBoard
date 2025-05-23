package cc.azuramc.azuraboard.scheduler;

import org.bukkit.plugin.Plugin;

/**
 * Scheduler compatibility interface
 * Provides common methods for scheduling tasks regardless of Folia support
 * 
 * @author an5w1r@163.com
 */
public interface SchedulerCompat {
    
    /**
     * Check if the server is running Folia
     *
     * @return true if the server is using Folia
     */
    boolean isFoliaServer();
    
    /**
     * Run a task on the main thread 
     *
     * @param plugin The plugin instance
     * @param task The task to run
     */
    void runTask(Plugin plugin, Runnable task);
    
    /**
     * Run a delayed task on the main thread
     *
     * @param plugin The plugin instance
     * @param task The task to run
     * @param delay The delay in ticks
     */
    void runTaskLater(Plugin plugin, Runnable task, long delay);
    
    /**
     * Run a repeating task on the main thread
     *
     * @param plugin The plugin instance
     * @param task The task to run
     * @param delay The initial delay in ticks
     * @param period The period in ticks
     * @return The created task
     */
    Object runTaskTimer(Plugin plugin, Runnable task, long delay, long period);
    
    /**
     * Cancel a task
     *
     * @param task The task object
     */
    void cancelTask(Object task);
} 