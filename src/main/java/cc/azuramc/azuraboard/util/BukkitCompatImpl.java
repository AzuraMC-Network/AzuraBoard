package cc.azuramc.azuraboard.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Standard Bukkit implementation for Java 8
 * This implementation does not use any Folia-specific code
 *
 * @author an5w1r@163.com
 */
public class BukkitCompatImpl implements SchedulerCompat {

    @Override
    public boolean isFoliaServer() {
        return false; // Always return false as this implementation is for non-Folia servers
    }

    @Override
    public void runTask(Plugin plugin, Runnable task) {
        // Use the synchronous scheduler in regular Bukkit/Spigot
        Bukkit.getScheduler().runTask(plugin, task);
    }

    @Override
    public void runTaskLater(Plugin plugin, Runnable task, long delay) {
        // Use the synchronous scheduler in regular Bukkit/Spigot
        Bukkit.getScheduler().runTaskLater(plugin, task, delay);
    }

    @Override
    public Object runTaskTimer(Plugin plugin, Runnable task, long delay, long period) {
        // Use the synchronous scheduler in regular Bukkit/Spigot
        return Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
    }

    @Override
    public void cancelTask(Object task) {
        if (task == null) {
            return;
        }

        if (task instanceof org.bukkit.scheduler.BukkitTask) {
            ((org.bukkit.scheduler.BukkitTask) task).cancel();
        }
    }
} 