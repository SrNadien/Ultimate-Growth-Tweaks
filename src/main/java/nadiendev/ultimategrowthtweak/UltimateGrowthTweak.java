package nadiendev.ultimategrowthtweak;

import nadiendev.ultimategrowthtweak.config.ModConfig;
import nadiendev.ultimategrowthtweak.event.CropHarvestHandler;
import nadiendev.ultimategrowthtweak.event.CropTrampleHandler;
import nadiendev.ultimategrowthtweak.event.TwerkHandler;
import nadiendev.ultimategrowthtweak.network.ToggleTwerkPacket;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main mod class. Handles mod initialization and common setup.
 * Unos Mateicos UWU
 */

@Mod(UltimateGrowthTweak.MOD_ID)
public class UltimateGrowthTweak {

    public static final String MOD_ID = "ultimategrowthtweak";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public UltimateGrowthTweak(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(Type.COMMON, ModConfig.COMMON_SPEC, "ultimategrowthtweak-common.toml");

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerPayloads);

        NeoForge.EVENT_BUS.register(new CropHarvestHandler());
        NeoForge.EVENT_BUS.register(new CropTrampleHandler());
        NeoForge.EVENT_BUS.register(new TwerkHandler());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("[UltimateGrowthTweak] Mod loaded successfully.");
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                ToggleTwerkPacket.TYPE,
                ToggleTwerkPacket.CODEC,
                ToggleTwerkPacket::handle
        );
    }
}