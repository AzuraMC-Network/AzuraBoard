package cc.azuramc.azuraboard.command;

import cc.azuramc.azuraboard.AzuraBoard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Simple command handler for /sb command
 * Provides a quick way to toggle scoreboard display
 * No permissions required - available to all players
 * 
 * @author an5w1r@163.com
 */
public class ScoreboardCommand implements CommandExecutor {

    /**
     * Plugin instance
     */
    private final AzuraBoard plugin;

    /**
     * Constructor for ScoreboardCommand
     * 
     * @param plugin The plugin instance
     */
    public ScoreboardCommand(AzuraBoard plugin) {
        this.plugin = plugin;
    }

    /**
     * Handle the /sb command
     *
     * @param sender The command sender
     * @param command The command
     * @param label The command label
     * @param args The command arguments
     * @return true if the command was handled
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, 
                            @NotNull String label, String[] args) {
        // Only players can use this command
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getLanguageManager().getMessage("player-only-command"));
            return true;
        }
        
        Player player = (Player) sender;
        String langCode = plugin.getLanguageManager().getPlayerLanguage(player);
        
        // Toggle the scoreboard
        boolean isToggled = plugin.getBoardManager().toggleBoard(player);
        if (isToggled) {
            player.sendMessage(plugin.getLanguageManager().getMessage("board-disabled", langCode));
        } else {
            player.sendMessage(plugin.getLanguageManager().getMessage("board-enabled", langCode));
        }
        
        return true;
    }
}
