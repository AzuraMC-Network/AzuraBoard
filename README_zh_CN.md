# AzuraBoard 计分板插件

<div align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.13%2B-brightgreen.svg" alt="支持Minecraft 1.13+">
  <img src="https://img.shields.io/badge/Spigot-已支持-yellow.svg" alt="支持Spigot">
  <img src="https://img.shields.io/badge/Paper-已支持-yellow.svg" alt="支持Paper">
  <img src="https://img.shields.io/badge/Folia-已支持-green.svg" alt="支持Folia">
  <img src="https://img.shields.io/badge/依赖-PlaceholderAPI(可选)-blue.svg" alt="依赖">
</div>

> 一个轻量级的计分板插件，支持多语言和变量替换。

[English](README.md) | [中文（当前）](README_zh_CN.md)

## 🌟 特性

- **高性能**：基于 [FastBoard](https://github.com/MrMicky-FR/FastBoard) 库构建，提供卓越性能
- **多语言支持**：自带英语和中文语言，轻松添加更多语言
- **变量支持**：支持 PlaceholderAPI 变量，实现动态内容
- **RGB 颜色支持**：支持 RGB 颜色代码（&#RRGGBB 格式），适用于 Minecraft 1.16+
- **自动语言切换**：根据玩家客户端语言自动更改界面语言
- **简单配置**：简单直观的配置文件，便于定制
- **ViaBackwards 兼容**：支持 1.13 以下客户端接收完整计分板信息
- **Folia 兼容**：完全支持 Folia 的区域化线程调度系统

## 📖 命令

- `/ab reload` - 重载插件配置和语言文件
- `/ab toggle` - 切换计分板显示

## 🛠️ 权限

- `azuraboard.command` - 允许使用管理命令 (默认：OP)
- `azuraboard.toggle` - 允许切换计分板显示 (默认：所有人)

## ⚙️ 配置

配置文件提供了自定义计分板标题、内容、更新间隔和语言设置等选项。

示例配置:
```yaml
settings:
  update-interval: 20 # 更新间隔(单位：tick)
  language: "zh_CN" # 默认语言

scoreboard:
  title: '&b&lAzura&f&lBoard' # 计分板标题
  lines: # 计分板内容
    - '&7&m----------------'
    - '&f玩家: &a%player_name%'
    - '&f世界: &a%player_world%'
    - ' '
    - '&#FF5555RGB颜色示例' # 1.16+ RGB颜色代码
    - '&7&m----------------'
```

## 📋 语言文件

插件支持多语言，你可以通过编辑或创建语言文件来添加新的语言支持。

## 🙏 致谢

- [FastBoard](https://github.com/MrMicky-FR/FastBoard) - 提供高性能计分板API

## 构建

AzuraBoard 支持两种构建模式：

1. **标准构建 (Java 8)**
   - 不包含Folia相关代码
   - 适用于标准的Bukkit/Spigot/Paper服务器
   - 构建命令：
     ```bash
     # Windows
     gradlew.bat buildBukkit
     
     # Linux/macOS
     ./gradlew buildBukkit
     ```
   - 输出文件: `build/libs/AzuraBoard-版本号-bukkit.jar`

2. **Folia支持构建 (Java 17)**
   - 包含Folia API支持
   - 适用于Folia服务器和标准服务器
   - 构建命令：
     ```bash
     # Windows
     gradlew.bat buildFolia
     
     # Linux/macOS
     ./gradlew buildFolia
     ```
   - 输出文件: `build/libs/AzuraBoard-版本号-folia.jar`

### 其他构建命令

```bash
# 显示构建系统帮助
./gradlew buildHelp

# 显示所有可用的构建选项
./gradlew buildAll
```

> **注意**：当直接使用 shadowJar 任务并附加 `-PuseFolia=true` 参数时，请确保之前的构建没有修改配置。建议使用专门的 `buildBukkit` 和 `buildFolia` 任务来确保正确构建。