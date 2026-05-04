package nadiendev.ultimategrowthtweak.client;

import nadiendev.ultimategrowthtweak.network.ToggleTwerkPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "ultimategrowthtweak", value = Dist.CLIENT)
public class KeyInputHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        if (KeyBindings.TOGGLE_TWERK.consumeClick()) {
            PacketDistributor.sendToServer(new ToggleTwerkPacket());
        }
    }
}