# 单 jar 支持 Minecraft 1.21 – 26.2 可行性探索

> 日期：2026-10-24
> 背景：`docs/26.2-upgrade-plan.md` 曾断言"单 jar 多版本不可行"。本文件对该结论做系统复核。
> 结论先行：**可行，但不是"一次编译"，而是"多份编译产物合并 + 运行时门控"。**
> 旧结论仅在"单一编译单元/单一映射体系"的前提下成立。

## 1. 三大障碍与对策

### 1.1 Java 版本（21 vs 25）——已实测破解

- 1.21.x 要求 Java 21，26.2 要求 Java 25。
- JVM 向后兼容：class major 65（Java 21）的字节码在 Java 25 JVM 上完全合法。
- **实测（2026-10-24）**：把 `build.gradle` 的 `options.release`/`sourceCompatibility` 从 25 改为 21，
  `./gradlew build` 成功，产物三个类均为 class major 65。26.2 代码没有用到任何 Java 22+ API。
- 结论：全 jar 统一 `--release 21`，`fabric.mod.json` 声明 `java >=21`，两端 JVM 都能加载。
- 附带要求：三份 mixin 配置的 `compatibilityLevel` 统一为 `JAVA_21`（避免旧 mixin 环境解析
  `JAVA_25` 级别配置失败；JAVA_21 的 mixin 类可以注入更新的目标类）。

### 1.2 运行时名称体系（intermediary vs Mojang）——核心障碍，用"休眠类"破解

- 1.21.x：运行时是 intermediary 名（`net.minecraft.class_1799` 等），mod 以 Yarn 开发、构建期重映射。
- 26.x：游戏未混淆，运行时就是 Mojang 名（`net.minecraft.world.item.ItemStack`）。
- 任何一份编译产物只可能引用其中一套名字；引用"错误体系"名字的类在对方运行时无法解析。
- **对策**：一个 jar 里放多套互不相扰的类，按运行时版本只激活一套，其余保持"休眠"
  （从未被加载就不会触发 `NoClassDefFoundError`）。
- **本项目已有先例**：v1.3.0 的 `NoHideFlagsMixinPlugin`（`IMixinConfigPlugin`）+
  `NoHideFlagsVersion`（读 `FabricLoader.getModContainer("minecraft")` 版本）在 1.21.5 上
  跳过引用了 1.21.6 API 的 mixin 类——同一机制，本项目已在生产验证过。
- 门控发生时机：`IMixinConfigPlugin.shouldApplyMixin` / `getMixins()` 在 mixin 类被加载**之前**
  调用，返回 false 即可让该类永不加载。

### 1.3 fabric.mod.json 版本声明——用 OR 数组

- `"minecraft": ["~26.2", ">=1.21.5 <1.21.9", ">=1.21 <1.21.5"]`（数组 = 或语义，Fabric 标准行为）。
- `"fabricloader": ">=0.16.10"` 同时满足两个时代；`"fabric-api": "*"`；`"java": ">=21"`。
- CurseForge 单个文件可标记多个游戏版本（v1.3.0 就是一个文件标了 1.21.5–1.21.8 四个版本）。

## 2. 为什么需要三套代码路径

| MC 版本 | 隐藏机制 | 代表 API（Yarn/Mojang） | 现成实现 |
| --- | --- | --- | --- |
| 1.21 – 1.21.4 | 组件内 `showInTooltip` 布尔 | `AttributeModifiersComponent.showInTooltip`、`ItemEnchantmentsComponent` | v1.1.0（tag v1.1.0） |
| 1.21.5 – 1.21.8 | `TooltipDisplay` 组件级隐藏 + 逐条 `Display.Hidden` | `TooltipDisplayComponent.shouldDisplay` | v1.3.0（tag v1.3.0） |
| 26.2 | 同上（包名/方法名变化） | `TooltipDisplay.shows`（Mojang） | 当前 master（v2.0.0） |

intermediary 名贯穿整个 1.21.x 不变，所以前两套可以在**同一 intermediary 运行时**下共存，
但仍需分别针对 1.21.4 和 1.21.8 的 Yarn 各编译一次（开发期 API 不同）。

## 3. 目标 jar 结构

```
no-hide-flags-x.y.z.jar
├── fabric.mod.json                 # minecraft 为 OR 数组；entrypoint 指向 Dispatcher
├── indi/ohtoai/tool/no_hide_flags/
│   ├── NoHideFlagsDispatcher.class # 唯一入口；不 import 任何 MC 类
│   ├── legacy121/…                 # 变体 A：编译于 1.21.4+Yarn（intermediary 产物）
│   ├── legacy125/…                 # 变体 B：编译于 1.21.8+Yarn（intermediary 产物）
│   └── modern26/…                  # 变体 C：编译于 26.2（Mojang 名产物）
├── no-hide-flags.legacy121.mixins.json + refmap
├── no-hide-flags.legacy125.mixins.json + refmap
└── no-hide-flags.modern26.mixins.json（无 refmap）
```

- **Dispatcher**（唯一的 `client` entrypoint）：仅依赖 fabric-loader API，通过
  `Class.forName("net.minecraft.class_310")`（1.21.x 存在）/ `Class.forName("net.minecraft.client.Minecraft")`
  （26.2 存在）探测时代，再反射实例化对应变体的 ClientModInitializer。
- **每份 mixin 配置各挂一个 Plugin**：读 MC 版本号，只对本时代的配置返回 true。
- **包名按变体隔离**（`legacy121` / `legacy125` / `modern26`），避免类名冲突，
  也让"休眠类"永远不会被误引用。
- refmap 只属于 intermediary 变体；26.2 变体无映射，天然不需要。

## 4. 构建方案

Loom 1.10（intermediary 管线）与 Loom 1.18（未混淆管线）无法共存于同一个 Gradle build，
因此用 **included builds（复合构建）或 CI 多阶段构建**：

1. 子构建 A：MC 1.21.4 + Yarn + Loom 1.10 → `remapJar` 产物（intermediary）；
2. 子构建 B：MC 1.21.8 + Yarn + Loom 1.10 → `remapJar` 产物（intermediary）；
3. 子构建 C：MC 26.2 + Loom 1.18 → jar 产物（Mojang 名）；
4. 汇总构建：解包 A/B/C，按包名合并类 + 三份 mixin 配置 + 共享 fabric.mod.json + Dispatcher，
   统一 `--release 21`，产出最终 fat jar。

注意：A/B/C 的产物在合并时已是最终字节码，汇总构建不再做任何重映射。

## 5. 已验证 vs 待验证

**已验证：**
- `--release 21` 编译 26.2 代码通过，产物 class major 65（本文 §1.1，2026-10-24 实测）。
- 休眠类门控机制在 1.21.5/1.21.6 场景生产可用（v1.3.0 先例）。
- CurseForge 单文件多版本标记可行（v1.3.0 先例）。

**待 PoC 验证（高置信但未实证）：**
- loader 0.19.5（26.2 侧）对 jar 内休眠 intermediary 类完全惰性：不会被主动链接/扫描报错。
  依据：Fabric loader 按需加载 mod 类，mixin 类仅在配置应用时加载（Plugin 门控在前）。
- 两份带 refmap 的 mixin 配置与一份无 refmap 配置在同一 jar 内共存无冲突。
- `minecraft` OR 数组在 0.16.x 与 0.19.x loader 上均按"或"解析（标准语义，建议 PoC 首项验证）。

## 6. 成本与取舍

| 方案 | 工作量 | 维护成本 | 覆盖 |
| --- | --- | --- | --- |
| 现状：两条发布线两个 jar | 0 | 每次 MC 更新各自适配 | 已覆盖 1.21.5–1.21.8 + 26.2 |
| 单 jar：1.21.5 – 26.2（两个变体） | 约 0.5–1 天 PoC | 每次 MC 更新改一个变体 + 全矩阵回归 | 放弃 1.21–1.21.4 |
| 单 jar：1.21 – 26.2（三个变体） | 约 1–2 天 | 同上 + 多一个老变体 | 全覆盖 |

- 1.21–1.21.4 是 2024 年的版本，存量用户少（v1.1.0 下载 1 次 vs v1.3.0 的 1.21.5+ 线 8+ 次），
  建议先不做，视需求追加（变体 A 代码在 tag v1.1.0 里现成）。
- 版本空档说明：1.21.9+（如存在）、26.0、26.1 不在声明范围内，用户装不上，属预期行为。

## 7. 建议

1. 若追求单 jar：先做 **1.21.5–26.2 两变体 PoC**（变体 B 的产物 = v1.3.0 jar 现成可拆，
   变体 C = 当前 master），验收标准 = 同一 jar 在 1.21.5、1.21.8、26.2 三个客户端里
   F3+H 行为与各自的单版本 jar 一致。
2. PoC 第一项就验证 §5 的三个"待验证"点，任何一点失败即止损回退双 jar 方案。
3. 发布命名建议 `v3.0.0` 起，CurseForge 单文件标记全部覆盖版本。

## 8. 实施记录（2026-10-24，已实施）

按第 3 节架构完整落地，版本号 `3.0.0`：

- `variants/legacy121`：v1.1.0 逻辑（组件级 `showInTooltip`），编译于 MC 1.21 + Yarn 1.21+build.9，
  Gradle 8.12.1 / Loom 1.10.5 / JDK 21，包名 `…no_hide_flags.legacy121`。
- `variants/legacy125`：v1.3.0 逻辑（TooltipDisplay + 逐条 Display），编译于 MC 1.21.8 + Yarn，
  同上工具链，包名 `…legacy125`，保留 1.21.5/1.21.6 内部门控。
- 根项目：26.2 代码移入 `…modern26` 子包；新增 `NoHideFlagsDispatcher`（唯一 entrypoint，
  按 MC 版本反射加载对应变体）；三份 mixin 配置各挂 `IMixinConfigPlugin` 时代门控；
  dev 与 fat 双 `fabric.mod.json`（fat 版经 `fabric.mod.json.template` 在 jar 任务内展开合并）；
  根构建统一 `--release 21`，`jar` 任务解包合并两个变体产物（类 + 配置 + refmap）。
- 构建入口 `build-all.cmd` / `build-all.sh`；CI（build.yml / release.yml）改为双 JDK 21+25 流水线。

**验证结果：**

- 静态：fat jar 46 条目，三变体类树齐全，全部 class major 65；legacy 类引用 intermediary 名
  （`class_9285` 等）、modern26 类引用 Mojang 名、dispatcher 零 MC 引用；两份 refmap 正确注入。
- 运行（26.2 开发客户端 + 变体产物以纯类库形式挂载 = 生产同构环境）：
  - `No Hide Flags active (modern26)`——dispatcher 正确分发；
  - 三份配置 `Selecting` 正常，legacy121/legacy125 `Preparing … (0)`——休眠门控生效；
  - `Mixing … modern26.mixins.json into ItemStack / TooltipDisplay`——动态注册路径实际应用成功；
  - 正常进入主菜单，无 mixin 错误。

**待用户验收（无法本机自动化）：** fat jar 在 1.21、1.21.4、1.21.5、1.21.8 生产客户端的
F3+H 行为矩阵（变体代码系 v1.1.0/v1.3.0 生产版直接移植，风险低但未实测）。
发布前注意：CurseForge 后台删除 2026-10-24 误传的重复 v2.0.0 文件（见 26.2-upgrade-plan.md §10）。
