package nadiendev.ultimategrowthtweak.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ModConfig {

    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }

    public static class Common {

        // ─── Right-click harvest ────────────────────────────────────────────
        public final ModConfigSpec.BooleanValue enableRightClickHarvest;
        public final ModConfigSpec.ConfigValue<List<? extends String>> harvestBlacklist;

        // ─── Twerk ─────────────────────────────────────────────────────────
        public final ModConfigSpec.BooleanValue enableTwerk;
        public final ModConfigSpec.DoubleValue twerkChance;
        public final ModConfigSpec.IntValue twerkRange;
        public final ModConfigSpec.IntValue randomTickMultiplier;
        public final ModConfigSpec.IntValue sugarcaneMultiplier;
        public final ModConfigSpec.BooleanValue allowAdventureTwerking;
        public final ModConfigSpec.BooleanValue enableDirtToGrass;
        public final ModConfigSpec.BooleanValue useWhitelist;
        public final ModConfigSpec.ConfigValue<List<? extends String>> twerkIgnoreList;
        public final ModConfigSpec.BooleanValue debugMode;

        // ─── Twerk counter (twerks needed) ─────────────────────────────────
        public final ModConfigSpec.IntValue twerksRequired;

        // ─── Food cost ─────────────────────────────────────────────────────
        public final ModConfigSpec.BooleanValue consumeFood;
        public final ModConfigSpec.IntValue foodCost;

        // ─── Twerk requirements ────────────────────────────────────────────
        public final ModConfigSpec.BooleanValue requirementsEnabled;
        public final ModConfigSpec.ConfigValue<List<? extends String>> heldItemRequirement;
        public final ModConfigSpec.BooleanValue requiredItemTakesDamage;
        public final ModConfigSpec.IntValue durabilityDamage;

        // ─── Mod integrations ──────────────────────────────────────────────
        public final ModConfigSpec.BooleanValue enableAE2Accelerator;
        public final ModConfigSpec.IntValue ae2Multiplier;
        public final ModConfigSpec.BooleanValue enableMysticalCrops;

        // ─── Crop trample protection ───────────────────────────────────────
        public final ModConfigSpec.BooleanValue preventCropTrample;
        public final ModConfigSpec.BooleanValue trampleOnlyPlayers;

        public Common(ModConfigSpec.Builder builder) {

            // ── Right-click harvest ─────────────────────────────────────────
            builder.comment("=== Right-Click Harvest ===").push("rightclick_harvest");

            enableRightClickHarvest = builder
                    .comment("Enable right-click crop harvesting (replants automatically).")
                    .define("enableRightClickHarvest", true);

            harvestBlacklist = builder
                    .comment(
                        "Blocks that should NOT be harvested on right-click.",
                        "Example: 'minecraft:wheat', 'mysticalagriculture:inferium_crop'"
                    )
                    .defineList("harvestBlacklist",
                            List.of("minecraft:sweet_berry_bush"),
                            e -> e instanceof String s && s.contains(":"));

            builder.pop();

            // ── Twerk ───────────────────────────────────────────────────────
            builder.comment(
                "=== Twerk / Squat to Grow ===",
                "Only grows actual crops and plant shoots.",
                "Does NOT trigger grass decorations (flowers, tall grass, etc.)"
            ).push("twerk");

            enableTwerk = builder
                    .comment("Enable the twerk (squat/shift) to grow plants feature.")
                    .define("enableTwerk", true);

            twerkChance = builder
                    .comment("Chance (0.0 – 1.0) that a growth tick fires per block per twerk.")
                    .defineInRange("chance", 1.0, 0.0, 1.0);

            twerkRange = builder
                    .comment(
                        "Radius of the growth area around the player.",
                        "1  = 3x3  (default)",
                        "2  = 5x5",
                        "3  = 7x7",
                        "5  = 11x11",
                        "10 = 21x21 (maximum)"
                    )
                    .defineInRange("range", 1, 1, 10);

            randomTickMultiplier = builder
                    .comment("How many growth ticks to send per block per twerk.")
                    .defineInRange("randomTickMultiplier", 4, 1, 64);

            sugarcaneMultiplier = builder
                    .comment("Growth tick multiplier for sugar cane and cactus specifically.")
                    .defineInRange("sugarcaneMultiplier", 4, 1, 64);

            allowAdventureTwerking = builder
                    .comment("Allow players in Adventure mode to trigger twerk growth.")
                    .define("allowAdventureTwerking", true);

            enableDirtToGrass = builder
                    .comment("Convert dirt to grass when holding a grass block in the offhand while twerking.")
                    .define("enableDirtToGrass", true);

            useWhitelist = builder
                    .comment("If true, ignoreList acts as a WHITELIST. If false, it acts as a BLACKLIST.")
                    .define("useWhitelist", false);

            twerkIgnoreList = builder
                    .comment(
                        "List of block registry names to blacklist (or whitelist if useWhitelist=true).",
                        "Example: 'minecraft:wheat', 'mysticalagriculture:inferium_crop'"
                    )
                    .defineList("ignoreList",
                            List.of(),
                            e -> e instanceof String s && s.contains(":"));

            debugMode = builder
                    .comment("Print debug info to the log for every twerk event.")
                    .define("debug", false);

            builder.pop();

            // ── Twerk Counter ───────────────────────────────────────────────
            builder.comment("=== Twerk Counter ===",
                            "How many consecutive twerks are needed before crops actually grow."
            ).push("twerk_counter");

            twerksRequired = builder
                    .comment(
                        "Number of twerks (squats) required to trigger growth.",
                        "Default: 3. Min: 1 (every twerk grows), Max: 10."
                    )
                    .defineInRange("twerksRequired", 3, 1, 10);

            builder.pop();

            // ── Food Cost ───────────────────────────────────────────────────
            builder.comment("=== Food Cost ===",
                            "Twerking can drain the player's hunger bar."
            ).push("food_cost");

            consumeFood = builder
                    .comment("If true, twerking costs hunger. Enabled by default.")
                    .define("consumeFood", true);

            foodCost = builder
                    .comment(
                        "How many hunger points are consumed per successful twerk cycle.",
                        "2 points = 1 shank. Default: 2 (1 shank). Range: 1–20."
                    )
                    .defineInRange("foodCost", 2, 1, 20);

            builder.pop();

            // ── Twerk Requirements ──────────────────────────────────────────
            builder.comment("=== Twerk Requirements ===").push("twerk_requirements");

            requirementsEnabled = builder
                    .comment("Enable the requirements system. If false, all options below are ignored.")
                    .define("enabled", false);

            heldItemRequirement = builder
                    .comment(
                        "Items that must be held in the main hand to twerk.",
                        "Leave empty to allow any item. Example: ['minecraft:bone_meal']"
                    )
                    .defineList("heldItemRequirement",
                            List.of(),
                            e -> e instanceof String s && s.contains(":"));

            requiredItemTakesDamage = builder
                    .comment("Whether the required held item loses durability when twerking.")
                    .define("requiredItemTakesDamage", false);

            durabilityDamage = builder
                    .comment("How much durability the required item loses per twerk event.")
                    .defineInRange("durabilityDamage", 1, 1, 100);

            builder.pop();

            // ── Mod Integrations ────────────────────────────────────────────
            builder.comment("=== Mod Integrations ===").push("integrations");

            enableAE2Accelerator = builder
                    .comment("Enable Applied Energistics 2 growth crystal acceleration support.")
                    .define("enableAE2Accelerator", true);

            ae2Multiplier = builder
                    .comment("Growth multiplier when an AE2 growth accelerator is adjacent.")
                    .defineInRange("ae2Multiplier", 4, 1, 64);

            enableMysticalCrops = builder
                    .comment("Enable Mystical Agriculture crop growth during twerking.")
                    .define("enableMysticalCrops", true);

            builder.pop();

            // ── Crop Trample Protection ─────────────────────────────────────
            builder.comment("=== Crop Trample Protection ===").push("trample_protection");

            preventCropTrample = builder
                    .comment("Prevent crops from being destroyed when jumped on. Enabled by default.")
                    .define("preventCropTrample", true);

            trampleOnlyPlayers = builder
                    .comment("If true, only player trampling is prevented. Mobs can still trample.")
                    .define("trampleOnlyPlayers", false);

            builder.pop();
        }
    }
}