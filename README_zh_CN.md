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