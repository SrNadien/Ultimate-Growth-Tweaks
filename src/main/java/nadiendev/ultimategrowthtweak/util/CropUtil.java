package nadiendev.ultimategrowthtweak.util;

import nadiendev.ultimategrowthtweak.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.KelpPlantBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.TwistingVinesBlock;
import net.minecraft.world.level.block.TwistingVinesPlantBlock;
import net.minecraft.world.level.block.WeepingVinesBlock;
import net.minecraft.world.level.block.WeepingVinesPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;

import java.util.List;

/**
 * Utility methods related to crops and growth.
 */

public class CropUtil {

    private static final TagKey<Block> FLOWERS_TAG   = BlockTags.FLOWERS;
    private static final TagKey<Block> SAPLINGS_TAG  = BlockTags.SAPLINGS;

    public static boolean isFullyGrownCrop(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        return block instanceof CropBlock crop && crop.isMaxAge(state);
    }

    public static boolean isHarvestBlacklisted(Block block) {
        List<? extends String> blacklist = ModConfig.COMMON.harvestBlacklist.get();
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        return blacklist.contains(id.toString());
    }

    public static boolean isTwerkSkipped(Block block) {
        List<? extends String> list = ModConfig.COMMON.twerkIgnoreList.get();
        if (list.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        boolean inList = list.contains(id.toString());
        boolean useWhitelist = ModConfig.COMMON.useWhitelist.get();
        return useWhitelist != inList;
    }

    /**
     * Strict whitelist of blocks that can receive twerk growth.
     *
     * ALLOWED:
     *   CropBlock          - wheat, carrot, potato, beetroot, nether wart, etc.
     *   SugarCaneBlock     - sugar cane
     *   CactusBlock        - cactus
     *   NetherWartBlock    - nether wart (standalone block class)
     *   StemBlock          - melon / pumpkin stem
     *   SaplingBlock       - tree saplings (minecraft:saplings tag)
     *   BambooStalkBlock   - bamboo
     *   KelpBlock          - kelp tip (the growing part)
     *   TwistingVinesBlock / WeepingVinesBlock - nether vines (tips only)
     *
     * EXCLUDED (even though they are BonemealableBlock or GrowingPlantBlock):
     *   GRASS_BLOCK, MYCELIUM, MOSS_BLOCK  - spawn decorations
     *   short_grass, tall_grass            - are decorations themselves
     *   Flowers                            - minecraft:flowers tag
     *   Fern, large_fern, dead_bush, etc.  - decorative plants
     *   Vines (the plant block, not the tip) - already fully grown
     */
    public static boolean canTwerkGrow(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block == Blocks.AIR) return false;
        if (isTwerkSkipped(block)) return false;

        // Explicit exclusions — grass-type blocks that would spawn decorations
        if (block == Blocks.GRASS_BLOCK)   return false;
        if (block == Blocks.MYCELIUM)      return false;
        if (block == Blocks.MOSS_BLOCK)    return false;
        if (block == Blocks.SHORT_GRASS)   return false;
        if (block == Blocks.TALL_GRASS)    return false;
        if (block == Blocks.FERN)          return false;
        if (block == Blocks.LARGE_FERN)    return false;
        if (block == Blocks.DEAD_BUSH)     return false;
        if (block == Blocks.VINE)          return false;   // side vines, not a growing tip

        // Exclude all flowers
        if (state.is(FLOWERS_TAG)) return false;

        // Allowed: specific crop/plant types
        if (block instanceof CropBlock)           return true;
        if (block instanceof SugarCaneBlock)      return true;
        if (block instanceof CactusBlock)         return true;
        if (block instanceof NetherWartBlock)     return true;
        if (block instanceof StemBlock)           return true;
        if (block instanceof SaplingBlock)        return true;  // tree saplings
        if (block instanceof BambooStalkBlock)    return true;
        if (block instanceof KelpBlock)           return true;  // kelp tip
        if (block instanceof KelpPlantBlock)      return false; // kelp body, not growing
        if (block instanceof TwistingVinesBlock)  return true;  // tip
        if (block instanceof WeepingVinesBlock)   return true;  // tip
        // Plant blocks (body) are excluded
        if (block instanceof TwistingVinesPlantBlock) return false;
        if (block instanceof WeepingVinesPlantBlock)  return false;

        return false;
    }

    public static boolean isMysticalAgricultureLoaded() {
        return ModList.get().isLoaded("mysticalagriculture");
    }

    public static boolean isAE2Loaded() {
        return ModList.get().isLoaded("ae2");
    }
}