package nadiendev.ultimategrowthtweak.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static final KeyMapping TOGGLE_TWERK = new KeyMapping(
            "key.ultimategrowthtweak.toggle_twerk",   // translation key
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,                          // default: N
            "key.categories.ultimategrowthtweak"      // category
    );
}