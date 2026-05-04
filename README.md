## 🌱 Ultimate Growth Tweak

A lightweight, server-friendly farming quality-of-life mod for **NeoForge 1.21.1** with three fully independent and configurable features.

---

### ✂️ Right-Click Harvest

Right-click any fully-grown crop to harvest it and **automatically replant** — no breaking required.

- Works with all vanilla crops (wheat, carrot, potato, beetroot, nether wart) and any modded crop extending `CropBlock`
- Drops all produce normally, keeps one seed for replanting
- Per-block **blacklist** to exclude specific crops

---

### 🕺 Squat to Grow *(Twerk)*

Crouch repeatedly near your farm to send growth ticks to surrounding crops and plants.

- Press **N** to toggle the feature on/off per player (shown in the action bar)
- Requires a configurable number of **squats** before triggering (default: **3**)
- Costs **hunger** per activation (default: 1 shank — or disable it entirely)
- Configurable **area**: from **3×3** up to **21×21** blocks
- Only grows actual crops and plant shoots — **never spawns flowers or grass decorations**
- Whitelist or blacklist mode for fine-grained block control
- Optional held item requirement with durability damage support
- Adventure mode support (configurable)

#### What grows:
Wheat · Carrot · Potato · Beetroot · Nether Wart · Sugar Cane · Cactus · Melon & Pumpkin Stems · Tree Saplings · Bamboo · Kelp · Twisting & Weeping Vines

#### What never grows:
Short Grass · Tall Grass · Ferns · Flowers · Decorative Plants

---

### 🌾 Crop Trample Protection

Prevents farmland from being destroyed and crops from breaking when jumped on.

- **Enabled by default** — your farms are safe out of the box
- Option to protect only from **players**, letting mobs still trample
- Option to protect from **all entities**

---

### 🔌 Mod Integration

| Mod | How |
|-----|-----|
| **Applied Energistics 2** | Blocks tagged `ae2:growth_acceleratable` receive a configurable extra tick multiplier |
| **Mystical Agriculture** | Crops tagged `mysticalagriculture:crops` grow via the proper MA growth method |

Both are **soft dependencies** — the mod works without either installed.

---

### ⚙️ Configuration

All features are controlled via `config/ultimategrowthtweak-common.toml`.

| Feature | Key option | Default |
|---------|-----------|---------|
| Right-click harvest | `enableRightClickHarvest` | `true` |
| Harvest blacklist | `harvestBlacklist` | `[sweet_berry_bush]` |
| Squat to grow | `enableTwerk` | `true` |
| Squats required | `twerksRequired` | `3` (1–10) |
| Food cost | `consumeFood` / `foodCost` | `true` / `2` pts |
| Area radius | `range` | `1` = 3×3 (max 10 = 21×21) |
| Trample protection | `preventCropTrample` | `true` |
| AE2 multiplier | `ae2Multiplier` | `4` |
| Mystical Agriculture | `enableMysticalCrops` | `true` |