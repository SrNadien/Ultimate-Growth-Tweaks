package nadiendev.ultimategrowthtweak.event;

import nadiendev.ultimategrowthtweak.UltimateGrowthTweak;
import nadiendev.ultimategrowthtweak.config.ModConfig;
import nadiendev.ultimategrowthtweak.util.CropUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles right-click harvesting of crops.
 */

public class CropHarvestHandler {

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!ModConfig.COMMON.enableRightClickHarvest.get()) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        BlockPos pos = event.getPos();
        ServerLevel level = (ServerLevel) player.level();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (!(block instanceof CropBlock crop)) return;
        if (!crop.isMaxAge(state)) return;
        if (CropUtil.isHarvestBlacklisted(block)) return;

        List<ItemStack> drops = new ArrayList<>(
                Block.getDrops(state, level, pos, level.getBlockEntity(pos), player, player.getMainHandItem())
        );

        ItemStack seedToReplant = findAndRemoveSeed(drops, crop);

        level.setBlock(pos, crop.defaultBlockState(), Block.UPDATE_ALL);

        if (seedToReplant.isEmpty()) {
            for (ItemStack drop : drops) {
                if (!drop.isEmpty()) Block.popResource(level, pos, drop);
            }
        } else {
            for (ItemStack drop : drops) {
                if (!drop.isEmpty()) Block.popResource(level, pos, drop);
            }
        }

        event.setCanceled(true);

        if (ModConfig.COMMON.debugMode.get()) {
            UltimateGrowthTweak.LOGGER.info("[Harvest] {} harvested {} at {}",
                    player.getName().getString(), block.getDescriptionId(), pos);
        }
    }

    private ItemStack findAndRemoveSeed(List<ItemStack> drops, CropBlock crop) {
        int seedIndex = -1;
        int smallestCount = Integer.MAX_VALUE;

        for (int i = 0; i < drops.size(); i++) {
            ItemStack stack = drops.get(i);
            if (stack.isEmpty()) continue;

            String itemName = stack.getItem().toString().toLowerCase();
            if (itemName.contains("seed")) {
                seedIndex = i;
                break;
            }
            // Fallback: smallest stack = likely the seed
            if (stack.getCount() < smallestCount) {
                smallestCount = stack.getCount();
                seedIndex = i;
            }
        }

        if (seedIndex == -1) return ItemStack.EMPTY;

        ItemStack seedStack = drops.get(seedIndex);
        ItemStack seed = seedStack.copyWithCount(1);
        seedStack.shrink(1);
        if (seedStack.isEmpty()) drops.remove(seedIndex);

        return seed;
    }
}