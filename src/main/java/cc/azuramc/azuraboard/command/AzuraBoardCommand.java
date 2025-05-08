package cc.azuramc.azuraboard.command;

import cc.azuramc.azuraboard.AzuraBoard;
import cc.azuramc.azuraboard.util.ChatColorUtil;
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
            sender.sendMessage(ChatColorUtil.color("&c你没有权限执行此命令！"));
            return;
        }
        
        plugin.getConfigManager().reloadConfig();
        plugin.getBoardManager().reloadTask();
        
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!plugin.getBoardManager().isToggled(player)) {
                plugin.getBoardManager().removeBoard(player);
                plugin.getBoardManager().createBoard(player);
            }
        }
        
        sender.sendMessage(ChatColorUtil.color("&aAzuraBoard 配置已重新加载！"));
    }

    /**
     * Handle the toggle command
     * 
     * @param sender The command sender
     */
    private void handleToggleCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColorUtil.color("&c此命令只能由玩家执行！"));
            return;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("azuraboard.toggle")) {
            player.sendMessage(ChatColorUtil.color("&c你没有权限执行此命令！"));
            return;
        }
        
        plugin.getBoardManager().toggleBoard(player);
    }

    /**
     * Send help message to a sender
     * 
     * @param sender The command sender
     */
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColorUtil.color("&b&lAzuraBoard &8- &7v" + plugin.getDescription().getVersion() + " &8- &b计分板 - 指令帮助"));
        sender.sendMessage("");
        sender.sendMessage(ChatColorUtil.color("&7 • &f/ab reload &7Reload plugin config."));
        sender.sendMessage(ChatColorUtil.color("&7 • &f/ab toggle &7Toggle board"));
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