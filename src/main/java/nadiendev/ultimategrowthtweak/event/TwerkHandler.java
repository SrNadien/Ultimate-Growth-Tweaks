package nadiendev.ultimategrowthtweak.event;

import nadiendev.ultimategrowthtweak.UltimateGrowthTweak;
import nadiendev.ultimategrowthtweak.config.ModConfig;
import nadiendev.ultimategrowthtweak.network.TwerkToggleManager;
import nadiendev.ultimategrowthtweak.util.CropUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.TwistingVinesBlock;
import net.minecraft.world.level.block.WeepingVinesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TwerkHandler {

    // Tag-based mod integrations (same approach as SquatGrow)
    private static final TagKey<Block> AE2_TAG = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("ae2", "growth_acceleratable")
    );
    private static final TagKey<Block> MYSTICAL_TAG = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("mysticalagriculture", "crops")
    );

    private final Map<UUID, Integer> twerkCounter = new HashMap<>();
    private final Map<UUID, Boolean> wasCrouching = new HashMap<>();

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID id = event.getEntity().getUUID();
        twerkCounter.remove(id);
        wasCrouching.remove(id);
        TwerkToggleManager.onPlayerLogout(id);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!ModConfig.COMMON.enableTwerk.get()) return;
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        // Per-player toggle via N key
        if (!TwerkToggleManager.isEnabled(player.getUUID())) return;

        ServerLevel level = (ServerLevel) player.level();

        if (!ModConfig.COMMON.allowAdventureTwerking.get()) {
            ServerPlayer sp = level.getServer().getPlayerList().getPlayer(player.getUUID());
            if (sp != null && sp.gameMode.getGameModeForPlayer() == GameType.ADVENTURE) return;
        }

        UUID id = player.getUUID();
        boolean nowCrouching = player.isCrouching() && player.onGround();
        boolean prevCrouching = wasCrouching.getOrDefault(id, false);

        if (nowCrouching && !prevCrouching) {
            int count = twerkCounter.getOrDefault(id, 0) + 1;
            int required = ModConfig.COMMON.twerksRequired.get();

            if (ModConfig.COMMON.debugMode.get())
                UltimateGrowthTweak.LOGGER.info("[Twerk] {} squat {}/{}", player.getName().getString(), count, required);

            if (count >= required) {
                if (tryConsumeFood(player, level)) {
                    performTwerk(player, level);
                }
                twerkCounter.put(id, 0);
            } else {
                twerkCounter.put(id, count);
            }
        }

        wasCrouching.put(id, nowCrouching);
    }

    private boolean tryConsumeFood(Player player, ServerLevel level) {
        if (!ModConfig.COMMON.consumeFood.get()) return true;

        if (player instanceof ServerPlayer sp) {
            GameType gm = sp.gameMode.getGameModeForPlayer();
            if (gm == GameType.CREATIVE || gm == GameType.SPECTATOR) return true;
        }

        FoodData food = player.getFoodData();
        int cost = ModConfig.COMMON.foodCost.get();
        if (food.getFoodLevel() < cost) {
            if (ModConfig.COMMON.debugMode.get())
                UltimateGrowthTweak.LOGGER.info("[Twerk] {} not enough food.", player.getName().getString());
            return false;
        }
        food.setFoodLevel(food.getFoodLevel() - cost);
        return true;
    }

    private void performTwerk(Player player, ServerLevel level) {
        int range     = ModConfig.COMMON.twerkRange.get();
        double chance = ModConfig.COMMON.twerkChance.get();
        int tickMult  = ModConfig.COMMON.randomTickMultiplier.get();
        int sugarMult = ModConfig.COMMON.sugarcaneMultiplier.get();
        boolean d2g   = ModConfig.COMMON.enableDirtToGrass.get();
        boolean debug = ModConfig.COMMON.debugMode.get();

        if (ModConfig.COMMON.requirementsEnabled.get()) {
            List<? extends String> required = ModConfig.COMMON.heldItemRequirement.get();
            if (!required.isEmpty()) {
                ItemStack held = player.getMainHandItem();
                ResourceLocation heldId = BuiltInRegistries.ITEM.getKey(held.getItem());
                if (heldId == null || !required.contains(heldId.toString())) {
                    if (debug) UltimateGrowthTweak.LOGGER.info("[Twerk] {} missing required item.", player.getName().getString());
                    return;
                }
                if (ModConfig.COMMON.requiredItemTakesDamage.get() && player instanceof ServerPlayer sp) {
                    held.hurtAndBreak(ModConfig.COMMON.durabilityDamage.get(), level, sp, item -> {});
                }
            }
        }

        BlockPos center = player.blockPosition();
        RandomSource rng = level.random;

        if (debug) UltimateGrowthTweak.LOGGER.info("[Twerk] Growing at {} range={}", center, range);

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (chance < rng.nextDouble()) continue;

                    BlockPos pos = center.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    Block block = state.getBlock();

                    if (state.isAir()) continue;

                    // Dirt → Grass
                    if (d2g && block == Blocks.DIRT) {
                        ItemStack offhand = player.getOffhandItem();
                        if (!offhand.isEmpty() && offhand.getItem() == Blocks.GRASS_BLOCK.asItem()) {
                            level.setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), Block.UPDATE_ALL);
                            continue;
                        }
                    }

                    if (!CropUtil.canTwerkGrow(level, pos)) continue;

                    // ── AE2: tag-based, randomTick like SquatGrow ──────────────
                    if (ModConfig.COMMON.enableAE2Accelerator.get()
                            && CropUtil.isAE2Loaded()
                            && state.is(AE2_TAG)) {
                        int ticks = ModConfig.COMMON.ae2Multiplier.get();
                        for (int i = 0; i < ticks; i++) {
                            level.getBlockState(pos).randomTick(level, pos, rng);
                        }
                        continue;
                    }

                    // ── Mystical Agriculture: tag + growCrops() like SquatGrow ─
                    if (ModConfig.COMMON.enableMysticalCrops.get()
                            && CropUtil.isMysticalAgricultureLoaded()
                            && state.is(MYSTICAL_TAG)
                            && block instanceof CropBlock crop) {
                        crop.growCrops(level, pos, state);
                        continue;
                    }

                    // ── Standard growth ────────────────────────────────────────
                    int ticks = (block instanceof SugarCaneBlock || block instanceof CactusBlock)
                            ? sugarMult : tickMult;

                    tryGrow(level, pos, state, block, ticks, rng);
                }
            }
        }
    }

    /**
     * Grows a block using the correct method per block type.
     *
     * RandomTick path (PUBLIC via BlockState):
     *   - SugarCane, Cactus: grow upward
     *   - StemBlock age==7: try to place fruit
     *   - BambooStalkBlock, KelpBlock: grow upward
     *   - TwistingVinesBlock, WeepingVinesBlock: grow tips
     *
     * BoneMeal path (PUBLIC via BoneMealItem):
     *   - CropBlock (wheat, carrot, potato, beetroot, etc.)
     *   - StemBlock age<7 (melon/pumpkin stem growing)
     *   - SaplingBlock (cherry, oak, etc.) — grows the tree
     */
    private void tryGrow(ServerLevel level, BlockPos pos, BlockState state, Block block, int ticks, RandomSource rng) {
        boolean useRandomTick =
                block instanceof SugarCaneBlock
                || block instanceof CactusBlock
                || block instanceof BambooStalkBlock
                || block instanceof KelpBlock
                || block instanceof TwistingVinesBlock
                || block instanceof WeepingVinesBlock
                || (block instanceof StemBlock && state.getValue(StemBlock.AGE) == 7);

        if (useRandomTick) {
            for (int i = 0; i < ticks; i++) {
                level.getBlockState(pos).randomTick(level, pos, rng);
            }
            return;
        }

        // Bonemeal path: crops, immature stems, saplings
        if (block instanceof BonemealableBlock bonemeal
                && bonemeal.isValidBonemealTarget(level, pos, state)) {
            for (int i = 0; i < ticks; i++) {
                BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), level, pos);
                BlockState updated = level.getBlockState(pos);
                if (!(updated.getBlock() instanceof BonemealableBlock bm)
                        || !bm.isValidBonemealTarget(level, pos, updated)) {
                    break;
                }
            }
        }
    }
}