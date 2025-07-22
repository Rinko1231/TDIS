## 🔧 工具拆解  
## 🔧 Tool Disassemble

有时候你可能对你做出的匠魂工具不满意，本模组添加了一个“工具拆解器”，用于将你的匠魂工具拆回原材料。  
Sometimes you might not be satisfied with the Tinkers' tool you made. This mod adds a *Tool Disassembler* that lets you dismantle your Tinkers' tools back into parts and materials.

---

## 🛠️ 使用方式 | How to Use

- 主手持工具拆解器（本模组物品）  
- Hold the Tool Disassembler (item from this mod) in your **main hand**

- 将要拆解的工具放入快捷栏任意格（推荐左边位置）  
- Put the tool you want to dismantle in your **hotbar** (preferably leftmost slots)

- 对特定方块右键（默认是**平滑石头**，可配置）  
- Right-click on a specific block (**Smooth Stone** by default, configurable)

- 即可自动拆解**快捷栏第一个可拆工具**  
- It will automatically dismantle the **first disassemblable tool** in your hotbar

---

## ♻️ 拆解返还内容 | What You Get Back

- 拆解会返还工具的**全部部件**  
- You'll get **all parts** used to build the tool

- 会返还使用的**强化材料**（如红石、钻石等）  
- **Upgrade materials** (e.g. redstone, diamond) will be returned

- 如果有**黏液覆层（Overslime）**，会返还相应数量的**原版黏液球**  
- If the tool has **Overslime**, you'll get back equivalent **vanilla slimeballs**

---

## ⚠️ 注意事项 | ⚠️ Notes

- 有些强化有**多个配方**（如红石：红石 vs 红石块）  
- Some upgrades have **multiple recipes** (e.g. redstone dust vs redstone block)

- 由于工具不会记录你用了哪种配方，模组只能退回**代码层的第一个配方**  
- Because the tool doesn't store which recipe was used, the mod falls back to the **first recipe in code**

- 这意味着如果第一配方是“块”，你用的是“粉”，可能会有**损耗**  
- So if the first recipe uses blocks, and you used dust, you **might lose** some materials

---

## ⚙️ 配置项支持 | Configurable Features

- 🔒 `工具拆解黑名单`：阻止某些工具被拆解（默认添加了**镶板盾牌**，因为存在复制Bug）  
- 🔒 `Tool Disassemble Blacklist`: Prevents certain tools from being dismantled (e.g. **plate_shield** by default due to dupe bugs)

- ❌ `物品返还黑名单`：拆解时不返还其中物品（默认添加了**下界石英**）  
- ❌ `Material Return Blacklist`: Prevents certain items (like **Nether Quartz**) from being returned

- 🧱 `触发拆解的方块`：可以自定义哪些方块可右键触发拆解（默认是**平滑石头**）  
- 🧱 `Trigger Block`: You can configure which blocks trigger dismantling (default: **Smooth Stone**)

---

## 🧪 与 JEI 配合提示 | JEI Recipe Handling

由于强化配方顺序受注册顺序影响，JEI 显示的强化材料可能与实拆返还的不一致。  
Because upgrade recipe order depends on registration, the material shown in JEI may not match the one returned when dismantled.

---

希望这个模组能帮你更灵活地处理工具！  
Hope it helps you handle your tools more flexibly!
