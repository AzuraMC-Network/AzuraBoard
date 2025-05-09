package cc.azuramc.azuraboard.manager;

import cc.azuramc.azuraboard.AzuraBoard;
import cc.azuramc.azuraboard.util.ChatColorUtil;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Language Manager for AzuraBoard
 * Handles loading and accessing language messages
 * 
 * @author an5w1r@163.com
 */
public class LanguageManager {

    private final AzuraBoard plugin;
    private final Map<String, FileConfiguration> languages = new HashMap<>();
    @Getter private String defaultLanguage = "en_US";

    /**
     * Constructor for LanguageManager
     *
     * @param plugin The plugin instance
     */
    public LanguageManager(AzuraBoard plugin) {
        this.plugin = plugin;
    }

    /**
     * Load all language files
     */
    public void loadLanguages() {
        // Get default language from config
        this.defaultLanguage = plugin.getConfigManager().getDefaultLanguage();
        
        // Clear any existing languages
        languages.clear();
        
        // Create language files directory if it doesn't exist
        File dataFolder = plugin.getDataFolder();
        
        // Load default language files
        loadDefaultLanguageFile("messages_en_US.yml");
        loadDefaultLanguageFile("messages_zh_CN.yml");
        
        // Load all language files from the plugin folder
        File[] files = dataFolder.listFiles((dir, name) -> name.startsWith("messages_") && name.endsWith(".yml"));
        
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String langCode = fileName.substring(9, fileName.length() - 4);
                
                FileConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
                languages.put(langCode, langConfig);
                
                plugin.getLogger().info("Loaded language file: " + fileName);
            }
        }
        
        // Ensure default language exists
        if (!languages.containsKey(defaultLanguage)) {
            plugin.getLogger().warning("Default language '" + defaultLanguage + "' not found! Falling back to en_US.");
            defaultLanguage = "en_US";
        }
    }
    
    /**
     * Load a default language file from the jar if it doesn't exist
     * 
     * @param fileName The name of the language file
     */
    private void loadDefaultLanguageFile(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        
        // Load from file
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        // Also load from jar for fallback
        InputStream defaultStream = plugin.getResource(fileName);
        if (defaultStream != null) {
            // Extract language code from filename (e.g., messages_en_US.yml -> en_US)
            String langCode = fileName.substring(9, fileName.length() - 4);
            FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
            
            // Add missing keys from default
            for (String key : defaultConfig.getKeys(true)) {
                if (!config.contains(key)) {
                    config.set(key, defaultConfig.get(key));
                }
            }
            
            // Save any missing keys
            try {
                config.save(file);
            } catch (IOException e) {
                plugin.getLogger().warning("Could not save updated language file: " + fileName);
            }
            
            languages.put(langCode, config);
        }
    }
    
    /**
     * Get a message from the language file with the default language
     * 
     * @param path The path to the message
     * @return The message
     */
    public String getMessage(String path) {
        return getMessage(path, defaultLanguage);
    }
    
    /**
     * Get a message from the language file with a specific language
     * 
     * @param path The path to the message
     * @param langCode The language code
     * @return The message
     */
    public String getMessage(String path, String langCode) {
        // Get the specified language or default to English
        FileConfiguration lang = languages.getOrDefault(langCode, languages.get(defaultLanguage));
        
        // If the message doesn't exist in the specified language, try default language
        if (lang == null || !lang.contains("messages." + path)) {
            lang = languages.get(defaultLanguage);
        }
        
        // If still null, return the path
        if (lang == null) {
            return path;
        }
        
        String message = lang.getString("messages." + path, path);
        return ChatColorUtil.color(message);
    }
    
    /**
     * Get a formatted message with placeholders
     * 
     * @param path The path to the message
     * @param replacements The placeholders and their values
     * @return The formatted message
     */
    public String getFormattedMessage(String path, Object... replacements) {
        return getFormattedMessage(path, defaultLanguage, replacements);
    }
    
    /**
     * Get a formatted message with placeholders in a specific language
     * 
     * @param path The path to the message
     * @param langCode The language code
     * @param replacements The placeholders and their values
     * @return The formatted message
     */
    public String getFormattedMessage(String path, String langCode, Object... replacements) {
        String message = getMessage(path, langCode);
        
        // Replace placeholders
        if (replacements.length >= 2) {
            for (int i = 0; i < replacements.length; i += 2) {
                if (i + 1 < replacements.length) {
                    String placeholder = String.valueOf(replacements[i]);
                    String value = String.valueOf(replacements[i + 1]);
                    message = message.replace("{" + placeholder + "}", value);
                }
            }
        }
        
        return message;
    }
    
    /**
     * Get the language code for a player
     * 
     * @param player The player
     * @return The language code
     */
    public String getPlayerLanguage(Player player) {
        // Use client locale if available (1.12+)
        try {
            String locale = player.getLocale();
            if (!locale.isEmpty() && languages.containsKey(locale)) {
                return locale;
            }
            
            // Convert client locale format (e.g. en_us) to our format (en_US)
            if (!locale.isEmpty()) {
                String[] parts = locale.split("_");
                if (parts.length == 2) {
                    String converted = parts[0].toLowerCase() + "_" + parts[1].toUpperCase();
                    if (languages.containsKey(converted)) {
                        return converted;
                    }
                }
            }
        } catch (Exception ignored) {
            // Method might not exist in older versions
        }
        
        // Use default language if client locale is not available or not supported
        return defaultLanguage;
    }
} 