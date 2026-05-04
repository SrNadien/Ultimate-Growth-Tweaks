package nadiendev.ultimategrowthtweak.integration;

import nadiendev.ultimategrowthtweak.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

/**
 * Applied Energistics 2 optional integration.
 */
public class AE2Integration {

  
    public static int applyMultiplier(ServerLevel level, BlockPos pos, int baseTicks) {
        return baseTicks * ModConfig.COMMON.ae2Multiplier.get();
    }
}
