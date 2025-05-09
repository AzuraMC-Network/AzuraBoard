package cc.azuramc.azuraboard.command;

import cc.azuramc.azuraboard.AzuraBoard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command Handler for AzuraBoard
 * Handles plugin commands and tab completion
 * 
 * @author an5w1r@163.com
 */
public class AzuraBoardCommand implements CommandExecutor, TabCompleter {

    /**
     * Plugin instance
     */
    private final AzuraBoard plugin;
    
    /**
     * Available subcommands
     */
    private final List<String> subCommands = Arrays.asList("reload", "toggle");

    /**
     * Constructor for AzuraBoardCommand
     * 
     * @param plugin The plugin instance
     */
    public AzuraBoardCommand(AzuraBoard plugin) {
        this.plugin = plugin;
    }

    /**
     * Handle plugin commands
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
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                handleReloadCommand(sender);
                break;
                
            case "toggle":
                handleToggleCommand(sender);
                break;
                
            default:
                sendHelp(sender);
                break;
        }
        
        return true;
    }

    /**
     * Handle the reload command
     * 
     * @param sender The command sender
     */
    private void handleReloadCommand(CommandSender sender) {
        if (!sender.hasPermission("azuraboard.command")) {
            String langCode = (sender instanceof Player) 
                ? plugin.getLanguageManager().getPlayerLanguage((Player) sender) 
                : plugin.getLanguageManager().getDefaultLanguage();
            
            sender.sendMessage(plugin.getLanguageManager().getMessage("no-permission", langCode));
            return;
        }
        
        plugin.getConfigManager().reloadConfig();
        plugin.getLanguageManager().loadLanguages();
        plugin.getBoardManager().reloadTask();
        
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!plugin.getBoardManager().isToggled(player)) {
                plugin.getBoardManager().removeBoard(player);
                plugin.getBoardManager().createBoard(player);
            }
        }
        
        String langCode = (sender instanceof Player) 
            ? plugin.getLanguageManager().getPlayerLanguage((Player) sender) 
            : plugin.getLanguageManager().getDefaultLanguage();
        
        sender.sendMessage(plugin.getLanguageManager().getMessage("plugin-reload", langCode));
    }

    /**
     * Handle the toggle command
     * 
     * @param sender The command sender
     */
    private void handleToggleCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getLanguageManager().getMessage("player-only-command"));
            return;
        }
        
        Player player = (Player) sender;
        String langCode = plugin.getLanguageManager().getPlayerLanguage(player);
        
        if (!player.hasPermission("azuraboard.toggle")) {
            player.sendMessage(plugin.getLanguageManager().getMessage("no-permission", langCode));
            return;
        }
        
        boolean isToggled = plugin.getBoardManager().toggleBoard(player);
        if (isToggled) {
            player.sendMessage(plugin.getLanguageManager().getMessage("board-disabled", langCode));
        } else {
            player.sendMessage(plugin.getLanguageManager().getMessage("board-enabled", langCode));
        }
    }

    /**
     * Send help message to a sender
     * 
     * @param sender The command sender
     */
    private void sendHelp(CommandSender sender) {
        String langCode = (sender instanceof Player) 
            ? plugin.getLanguageManager().getPlayerLanguage((Player) sender) 
            : plugin.getLanguageManager().getDefaultLanguage();
        
        String header = plugin.getLanguageManager().getFormattedMessage("help.header", langCode, 
                "version", plugin.getDescription().getVersion());
        
        sender.sendMessage(header);
        sender.sendMessage("");
        sender.sendMessage(plugin.getLanguageManager().getMessage("help.reload", langCode));
        sender.sendMessage(plugin.getLanguageManager().getMessage("help.toggle", langCode));
        sender.sendMessage("");
    }

    /**
     * Handle tab completion for plugin commands
     *
     * @param sender The command sender
     * @param command The command
     * @param alias The command alias
     * @param args The command arguments
     * @return List of tab completions
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, 
                                     @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return subCommands.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
} 