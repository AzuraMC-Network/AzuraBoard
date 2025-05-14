package cc.azuramc.azuraboard.folia;

import cc.azuramc.azuraboard.util.SchedulerCompat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

/**
 * Folia compatibility implementation for Java 17
 * This class contains all Folia-specific code
 *
 * @author an5w1r@163.com
 */
public class FoliaCompatImpl implements SchedulerCompat {

    @Override
    public boolean isFoliaServer() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void runTask(Plugin plugin, Runnable task) {
        try {
            // Use the global region scheduler in Folia
            Bukkit.getGlobalRegionScheduler().execute(plugin, task);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to execute task in Folia", e);
        }
    }

    @Override
    public void runTaskLater(Plugin plugin, Runnable task, long delay) {
        try {
            // Use the global region scheduler in Folia
            Bukkit.getGlobalRegionScheduler().runDelayed(plugin, (s) -> task.run(), delay);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to execute delayed task in Folia", e);
        }
    }

    @Override
    public Object runTaskTimer(Plugin plugin, Runnable task, long delay, long period) {
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
    }

    @Override
    public void cancelTask(Object task) {
        if (task == null) {
            return;
        }

        if (task instanceof io.papermc.paper.threadedregions.scheduler.ScheduledTask) {
            ((io.papermc.paper.threadedregions.scheduler.ScheduledTask) task).cancel();
        }
    }
} 