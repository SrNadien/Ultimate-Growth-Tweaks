# Ultimate Growth Tweak
**Minecraft 1.21.1 · NeoForge 21.1.215**

A highly configurable farming quality-of-life mod with three main features:

---

## Features

### 1. Right-Click Harvest
Right-click a fully-grown crop to harvest it and automatically replant.
- Works with all `CropBlock` subclasses (vanilla + mods)
- Per-block blacklist in config

### 2. Squat / Twerk to Grow *(Shift to grow plants)*
Press Shift while on the ground to send growth ticks to all growable blocks in a configurable radius.
- Configurable chance, range, and tick multipliers
- Sugar cane gets its own separate multiplier
- Dirt → Grass conversion when holding a grass block in your offhand
- Optional adventure mode support
- Whitelist **or** blacklist mode for blocks
- Requirements system: require a specific held item (with optional durability damage)
- AE2 and Mystical Agriculture integration (see below)

### 3. Crop Trample Protection
Prevents farmland from being trampled and crops from breaking when jumped on.
- Enabled by default
- Can be restricted to player-only trampling (letting mobs still trample)

---

## Configuration
Config file: `config/ultimategrowthtweak-common.toml`

### Right-Click Harvest
| Key | Default | Description |
|-----|---------|-------------|
| `enableRightClickHarvest` | `true` | Enable the feature |
| `harvestBlacklist` | `["minecraft:sweet_berry_bush"]` | Blocks that must NOT be right-click harvested |

### Twerk
| Key | Default | Description |
|-----|---------|-------------|
| `enableTwerk` | `true` | Enable the feature |
| `chance` | `1.0` | Per-block growth chance (0.0–1.0) |
| `range` | `16` | Radius in blocks |
| `randomTickMultiplier` | `4` | Extra random ticks per twerk |
| `sugarcaneMultiplier` | `4` | Extra ticks for sugar cane specifically |
| `allowAdventureTwerking` | `true` | Allow Adventure mode players |
| `enableDirtToGrass` | `true` | Convert dirt to grass when holding grass block |
| `useWhitelist` | `false` | If true, `ignoreList` becomes a whitelist |
| `ignoreList` | `[]` | Blacklist (or whitelist) of block IDs |
| `debug` | `false` | Verbose log output |

### Twerk Requirements
| Key | Default | Description |
|-----|---------|-------------|
| `enabled` | `false` | Enable the requirements system |
| `heldItemRequirement` | `[]` | Items that must be held (e.g. `["minecraft:bone_meal"]`) |
| `requiredItemTakesDamage` | `false` | Does the item lose durability? |
| `durabilityDamage` | `1` | How much durability per twerk |

### Integrations
| Key | Default | Description |
|-----|---------|-------------|
| `enableAE2Accelerator` | `true` | AE2 growth acceleration support |
| `ae2Multiplier` | `4` | AE2 growth multiplier |
| `enableMysticalCrops` | `true` | Mystical Agriculture crop support |

### Trample Protection
| Key | Default | Description |
|-----|---------|-------------|
| `preventCropTrample` | `true` | Prevent crop trampling |
| `trampleOnlyPlayers` | `false` | If true, only block player trampling (mobs can still trample) |

---

## Building

```bash
# Standard build
./gradlew build

# With AE2 integration compiled in
./gradlew build -Penable_ae2=true

# With Mystical Agriculture integration compiled in
./gradlew build -Penable_mystical_agriculture=true
```

Output JAR: `build/libs/ultimategrowthtweak-1.21.1-1.0.0.jar`

---

## Mod Integration Notes

### Applied Energistics 2
The AE2 integration is a **soft dependency** — the mod works without AE2 installed. The `AE2Integration.java` class is only called when AE2 is detected at runtime. If you compile with `enable_ae2=true`, you can replace the stub in `AE2Integration.applyMultiplier()` with real AE2 Growth Accelerator API calls.

### Mystical Agriculture
Same pattern — soft dependency, class-name based detection at runtime. Replace the stub in `MysticalAgricultureIntegration.isMysticalCrop()` with the real MA API when compiling with it.

---

## License
MIT
