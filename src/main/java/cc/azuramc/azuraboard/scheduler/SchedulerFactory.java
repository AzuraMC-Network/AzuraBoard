package cc.azuramc.azuraboard.scheduler;

/**
 * Factory class to get the appropriate scheduler implementation
 *
 * @author an5w1r@163.com
 */
public class SchedulerFactory {

    private static SchedulerCompat instance;

    /**
     * Get the appropriate scheduler implementation
     * 
     * @return a scheduler implementation based on the current environment
     */
    public static synchronized SchedulerCompat getScheduler() {
        if (instance == null) {
            // This will be replaced by conditional compilation in the build script
            instance = GeneratedSchedulerFactory.createSchedulerImpl();
        }
        return instance;
    }
} 