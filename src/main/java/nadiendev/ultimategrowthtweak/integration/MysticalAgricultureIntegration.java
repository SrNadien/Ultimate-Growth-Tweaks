package nadiendev.ultimategrowthtweak.integration;

import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;

/**
 * Mystical Agriculture optional integration.
 */
public class MysticalAgricultureIntegration {

    private static final String MA_CROP_CLASS = "com.blakebr0.mysticalagriculture.block.CropBlock";

    /**
     * Returns true if the given block is a Mystical Agriculture crop.
     */
    public static boolean isMysticalCrop(Block block) {
        // Runtime check by class name so this compiles without MA as a dependency
        return block.getClass().getName().startsWith("com.blakebr0.mysticalagriculture");
    }
}
