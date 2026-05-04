package nadiendev.ultimategrowthtweak.network;

import nadiendev.ultimategrowthtweak.UltimateGrowthTweak;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * ToggleTwerkPacket is sent from the client to the server when the player presses the toggle twerk keybind.
 */
public record ToggleTwerkPacket() implements CustomPacketPayload {

    public static final Type<ToggleTwerkPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(UltimateGrowthTweak.MOD_ID, "toggle_twerk")
    );

    // No fields → empty codec
    public static final StreamCodec<FriendlyByteBuf, ToggleTwerkPacket> CODEC =
            StreamCodec.unit(new ToggleTwerkPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /** Called on the server thread when the packet arrives. */
    public static void handle(ToggleTwerkPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            boolean nowEnabled = TwerkToggleManager.toggle(player.getUUID());

            // Notify the player via action bar (doesn't interrupt chat)
            player.displayClientMessage(
                    Component.translatable(
                            nowEnabled
                                    ? "message.ultimategrowthtweak.twerk_enabled"
                                    : "message.ultimategrowthtweak.twerk_disabled"
                    ),
                    true  // action bar = true
            );
        });
    }
}