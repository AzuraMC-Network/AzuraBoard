# AzuraBoard Scoreboard Plugin

<div align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.13%2B-brightgreen.svg" alt="Minecraft 1.13+">
  <img src="https://img.shields.io/badge/Spigot-Supported-yellow.svg" alt="Spigot Support">
  <img src="https://img.shields.io/badge/Paper-Supported-yellow.svg" alt="Paper Support">
  <img src="https://img.shields.io/badge/Folia-Supported-green.svg" alt="Folia Support">
  <img src="https://img.shields.io/badge/Dependencies-PlaceholderAPI(Optional)-blue.svg" alt="Dependencies">
</div>

> A lightweight scoreboard plugin with multi-language support and variable replacement.

[English (Current)](README.md) | [中文](README_zh_CN.md)

## 🌟 Features

- **High Performance**: Built on [FastBoard](https://github.com/MrMicky-FR/FastBoard) library for superior performance
- **Multi-language Support**: Comes with English and Chinese languages, easily add more
- **Variable Support**: Works with PlaceholderAPI variables for dynamic content
- **RGB Color Support**: Supports RGB color codes (&#RRGGBB format) for Minecraft 1.16+
- **Auto Language Switching**: Automatically changes UI language based on player's client language
- **Simple Configuration**: Straightforward config files for easy customization
- **ViaBackwards Compatible**: Supports clients below 1.13 receiving complete scoreboard information
- **Folia Compatible**: Full support for Folia's regionalized thread scheduling system

## 📖 Commands

- `/ab reload` - Reload plugin configuration and language files
- `/ab toggle` - Toggle scoreboard visibility

## 🛠️ Permissions

- `azuraboard.command` - Allows use of management commands (Default: OP)
- `azuraboard.toggle` - Allows toggling scoreboard display (Default: Everyone)

## ⚙️ Configuration

Configuration file provides options to customize scoreboard title, content, update interval, and language settings.

Example configuration:
```yaml
settings:
  update-interval: 20 # Update interval in ticks
  language: "en_US" # Default language

scoreboard:
  title: '&b&lAzura&f&lBoard' # Scoreboard title
  lines: # Scoreboard content
    - '&7&m----------------'
    - '&fPlayer: &a%player_name%'
    - '&fWorld: &a%player_world%'
    - ' '
    - '&#FF5555RGB Color Example' # 1.16+ RGB color code
    - '&7&m----------------'
```

## 📋 Language Files

The plugin supports multiple languages. You can add new language support by editing or creating language files.

## 🙏 Acknowledgements

- [FastBoard](https://github.com/MrMicky-FR/FastBoard) - High-performance scoreboard API

## Building

AzuraBoard supports two build modes:

1. **Standard Build (Java 8)**
   - Does not include Folia-specific code
   - Suitable for standard Bukkit/Spigot/Paper servers
   - Build command:
     ```bash
     ./gradlew shadowJar -PuseFolia=false
     ```
   - Output: `build/libs/AzuraBoard-version-bukkit.jar`

2. **Folia Support Build (Java 17)**
   - Includes Folia API support
   - Suitable for both Folia servers and standard servers
   - Build command:
     ```bash
     ./gradlew shadowJar -PuseFolia=true
     ```
   - Output: `build/libs/AzuraBoard-version-folia.jar`

### Additional Build Commands

```bash
# Show build help
./gradlew buildHelp

# Show all available build options
./gradlew buildAll
```

> **Note**: When using the direct shadowJar task with `-PuseFolia=true`, make sure no previous builds have modified the configuration. It's safer to use the dedicated `buildBukkit` and `buildFolia` tasks.