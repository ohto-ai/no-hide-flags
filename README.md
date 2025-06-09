# 📦 no-hide-flags (Fabric Mod)

Support Minecraft 1.21+ | [Release](https://github.com/ohto-ai/no-hide-flags/releases)

**显示物品中被隐藏的附魔和属性信息 / Reveal Hidden Enchantments and Attributes in Tooltips**

---

## 📝 简介 | Introduction

这个 Fabric Mod 可以在 Minecraft 中强制显示物品 `ItemStack` 中被隐藏的附魔（enchantments）和属性修饰（attribute modifiers）。  
即使 `show_in_tooltip = false`，也会通过 tooltip 补充信息提示玩家。

This Fabric mod reveals hidden **enchantments** and **attribute modifiers** in item tooltips.  
Even if `show_in_tooltip = false`, the mod adds a tooltip entry to notify the player.

---

## ✨ 功能 | Features

- 🔍 显示被隐藏的附魔（通过 `ENCHANTMENTS` 组件）
- ⚔️ 显示被隐藏的属性修饰（通过 `ATTRIBUTE_MODIFIERS` 组件）
- 💬 在原版 tooltip 基础上增加一行 `Hide Enchantments` 或 `Hide Attributes` 提示
- 🧠 仅在启用 "F3 + H"（高级物品信息）后生效，符合原版习惯

---

## 💡 使用方式 | How to Use

1. 安装 [Fabric Loader](https://fabricmc.net/) 和 [Fabric API](https://modrinth.com/mod/fabric-api)。
2. 将构建好的 `no-hide-flags-x.y.z.jar` 放入 `mods/` 目录。
3. 启动游戏，按 `F3 + H` 开启高级物品信息。
4. 将鼠标悬停在附魔物品上，如果有隐藏信息会出现额外的提示。

---

## 🧪 示例效果 | Example Output

- Tooltip 中增加：
  - _*Hide Enchantments*_
  - _*Hide Attributes*_
- 附加真实的附魔或属性效果文字，使用与原版相同的渲染方式

---

## 🛠️ 构建 | Build

本项目使用 Gradle 构建：

```bash
./gradlew build
```
构建完成后可在 `build/libs/no-hide-flags-*.jar` 找到输出文件。

## 🔧 依赖 | Dependencies
Minecraft 1.21+

- Fabric Loader
- Fabric API
- Mixin (通过 Loom 自动配置)

## 📄 许可证 | License

MIT License
See [LICENSE](LICENSE) for details.
