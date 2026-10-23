# No Hide Flags

[![Development Builds](https://github.com/ohto-ai/no-hide-flags/actions/workflows/build.yml/badge.svg)](https://github.com/ohto-ai/no-hide-flags/actions/workflows/build.yml)
[![Publish Release](https://github.com/ohto-ai/no-hide-flags/actions/workflows/release.yml/badge.svg?event=release)](https://github.com/ohto-ai/no-hide-flags/actions/workflows/release.yml)

Support Minecraft 1.21 – 26.2 (single jar) | [Release](https://github.com/ohto-ai/no-hide-flags/releases)

**显示物品中被隐藏的附魔和属性信息 / Reveal Hidden Enchantments and Attributes in Tooltips**

---

## 📝 简介 | Introduction

`No Hide Flags` is a Minecraft mod for the Fabric platform. It reveals all hidden enchantments and attribute modifiers in item tooltips, allowing players to view complete item details. Even if `show_in_tooltip = false`, the mod supplements tooltip information for the player. A single jar supports Minecraft 1.21 – 1.21.8 and 26.2.

---

## ✨ 功能 | Features

- Reveals hidden enchantments in item tooltips
- Reveals hidden attribute modifiers in item tooltips
- If an item has hidden enchantments, an extra `Hide Enchantments` line is added to the tooltip
- Only takes effect when <kbd>F3 + H</kbd> advanced tooltip is enabled, matching vanilla Minecraft behavior

---

## 💡 使用方式 | How to Use

1. Install [Fabric Loader](https://fabricmc.net/) and [Fabric API](https://modrinth.com/mod/fabric-api).
2. Place the built `no-hide-flags-x.y.z.jar` into your `mods/` directory.
3. Launch the game and press <kbd>F3 + H</kbd> to enable advanced item tooltips.
4. Hover over items with hidden enchantments or attribute modifiers. When hidden enchantments exist, an extra `Hide Enchantments` entry appears; attribute modifiers are shown directly.

---

## 🧪 示例效果 | Example Output

- Tooltip shows _`Hide Enchantments`_ (gray italic) when hidden enchantments are present
- Attribute modifiers appear in vanilla style without extra lines
- Real enchantment or attribute effect text is rendered just like in vanilla Minecraft

---

## 📸 效果截图 | Screenshots

Without mod

![image](https://media.forgecdn.net/attachments/1283440/changelog/changelog_175af99b-9127-495f-8f62-bf6f876544ee.png)

With mod

![image](https://media.forgecdn.net/attachments/1283440/changelog/changelog_6805f29d-2473-4725-9f30-46b514475e6b.png)

---

## 🛠️ 构建 | Build

The release artifact is a single fat jar covering Minecraft 1.21 – 1.21.8 and 26.2. It is assembled from three variant builds:

- `variants/legacy121` — 1.21–1.21.4, Yarn/intermediary pipeline, **JDK 21** (Loom 1.10)
- `variants/legacy125` — 1.21.5–1.21.8, Yarn/intermediary pipeline, **JDK 21** (Loom 1.10)
- root — 26.2, unobfuscated Mojang names, **JDK 25** (Loom 1.18), merges the variants into the fat jar

Run everything with one command (override install paths via `NHF_JDK21` / `NHF_JDK25`):

```bash
./build-all.sh        # Windows: build-all.cmd
```

The output JAR can be found in `build/libs/no-hide-flags-*.jar` after building.

---

## 🔧 依赖 | Dependencies

- Minecraft 1.21 – 1.21.8, 26.2
- Java 21+ (Minecraft 26.2 itself requires a Java 25 runtime)
- Fabric Loader
- Fabric API
- Mixin (configured via Loom)

---

## 📄 许可证 | License

MIT License  
See [LICENSE](https://github.com/ohto-ai/no-hide-flags/blob/master/LICENSE) for details.
