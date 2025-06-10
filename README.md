# no-hide-flags

Support Minecraft 1.21+ | [Release](https://github.com/ohto-ai/no-hide-flags/releases)

**显示物品中被隐藏的附魔和属性信息 / Reveal Hidden Enchantments and Attributes in Tooltips**

---

## 📝 简介 | Introduction

这个 Fabric Mod 可以在 Minecraft 中强制显示物品 `ItemStack` 中被隐藏的附魔（enchantments）和属性修饰（attribute modifiers）。
即使 `show_in_tooltip = false`，也会通过 tooltip 补充信息提示玩家。

This Fabric mod reveals hidden **enchantments** and **attribute modifiers** in item tooltips.
Even if `show_in_tooltip = false`, the mod adds a tooltip entry to notify the player.

Without mod

![image](https://media.forgecdn.net/attachments/1283440/changelog/changelog_175af99b-9127-495f-8f62-bf6f876544ee.png)

With mod

![image](https://media.forgecdn.net/attachments/1283440/changelog/changelog_6805f29d-2473-4725-9f30-46b514475e6b.png)

---

## ✨ 功能 | Features

- 🔍 显示被隐藏的附魔
- ⚔️ 显示被隐藏的属性修饰
- 💬 如果物品有隐藏附魔，会在 tooltip 增加一行 `Hide Enchantments` 提示
- 🧠 仅在启用 "F3 + H" 后生效，符合原版习惯

---

## 💡 使用方式 | How to Use

1. 安装 [Fabric Loader](https://fabricmc.net/) 和 [Fabric API](https://modrinth.com/mod/fabric-api)。
2. 将构建好的 `no-hide-flags-x.y.z.jar` 放入 `mods/` 目录。
3. 启动游戏，按 `F3 + H` 开启高级物品信息。
4. 将鼠标悬停在带有隐藏附魔或属性修饰的物品上，如果有隐藏附魔会出现额外的 `Hide Enchantments` 提示，属性修饰符则会直接显示其具体内容。

---

## 🧪 示例效果 | Example Output

- Tooltip 中增加：
  - 当存在隐藏附魔时：_`Hide Enchantments`_（灰色斜体）
  - 属性修饰符直接以原版样式显示，无额外提示行
- 附加真实的附魔或属性效果文字，使用与原版相同的渲染方式

---

## 🛠️ 构建 | Build

本项目使用 Gradle 构建：

```bash
./gradlew build
```
构建完成后可在 `build/libs/no-hide-flags-*.jar` 找到输出文件。

## 🔧 依赖 | Dependencies

- Minecraft 1.21+
- Fabric Loader
- Fabric API
- Mixin（通过 Loom 自动配置）

## 📄 许可证 | License

MIT License  
See [LICENSE](LICENSE) for details.
