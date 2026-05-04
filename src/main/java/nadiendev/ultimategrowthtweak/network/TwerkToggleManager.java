package nadiendev.ultimategrowthtweak.network;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks which players have the twerk feature DISABLED via the N key.
 */
public class TwerkToggleManager {

    // Players in this set have twerk DISABLED
    private static final Set<UUID> disabledPlayers =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    /** Toggle twerk for a player. Returns true if twerk is NOW enabled. */
    public static boolean toggle(UUID playerId) {
        if (disabledPlayers.contains(playerId)) {
            disabledPlayers.remove(playerId);
            return true;  // now enabled
        } else {
            disabledPlayers.add(playerId);
            return false; // now disabled
        }
    }

    /** Returns true if twerk is enabled for this player (default: true). */
    public static boolean isEnabled(UUID playerId) {
        return !disabledPlayers.contains(playerId);
    }

    /** Call on player logout to clean up. */
    public static void onPlayerLogout(UUID playerId) {
        disabledPlayers.remove(playerId);
    }
}