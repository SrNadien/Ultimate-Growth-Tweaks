package nadiendev.ultimategrowthtweak.event;

import nadiendev.ultimategrowthtweak.config.ModConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

/**
 * Prevents crops from being trampled when jumped on.
 */
public class CropTrampleHandler {

    @SubscribeEvent
    public void onFarmlandTrample(net.neoforged.neoforge.event.level.BlockEvent.FarmlandTrampleEvent event) {
        if (!ModConfig.COMMON.preventCropTrample.get()) return;

        Entity entity = event.getEntity();

        // If trampleOnlyPlayers is true, only cancel for players.
        // If false, cancel for every entity (mobs, armor stands, etc.).
        boolean isPlayer = entity instanceof Player;
        if (ModConfig.COMMON.trampleOnlyPlayers.get() && !isPlayer) return;

        event.setCanceled(true);
    }
}
