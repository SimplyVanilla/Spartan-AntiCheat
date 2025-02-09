package com.vagdedes.spartan.handlers.identifiers.complex.unpredictable;

import com.vagdedes.spartan.functionality.important.MultiVersion;
import com.vagdedes.spartan.objects.data.Handlers;
import com.vagdedes.spartan.objects.replicates.SpartanPlayer;
import com.vagdedes.spartan.utils.gameplay.PlayerData;

public class ExtremeCollision {

    public static void run(SpartanPlayer player) {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
            Handlers handlers = player.getHandlers();

            if (!handlers.isDisabled(Handlers.HandlerType.ExtremeCollision)) {
                handlers.disable(Handlers.HandlerType.ExtremeCollision, 2); // Disable for the next tick

                if (PlayerData.getNearbyCollisions(player) >= 0.9) {
                    handlers.add(Handlers.HandlerType.ExtremeCollision, 40);
                }
            }
        }
    }
}
