package com.vagdedes.spartan.compatibility.manual.entity;

import com.vagdedes.spartan.configuration.Compatibility;
import com.vagdedes.spartan.objects.data.Buffer;
import com.vagdedes.spartan.objects.replicates.SpartanPlayer;
import com.vagdedes.spartan.system.SpartanBukkit;
import es.pollitoyeye.vehicles.enums.VehicleType;
import es.pollitoyeye.vehicles.events.VehicleEnterEvent;
import es.pollitoyeye.vehicles.events.VehicleExitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class Vehicles implements Listener {

    private static final String key = Compatibility.CompatibilityType.Vehicles + "=compatibility=";

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Enter(VehicleEnterEvent e) {
        if (Compatibility.CompatibilityType.Vehicles.isFunctional()) {
            SpartanPlayer p = SpartanBukkit.getPlayer(e.getPlayer().getUniqueId());
            VehicleType vehicleType = e.getVehicleType();

            if (vehicleType == VehicleType.DRILL) {
                add(p, "drill");
            } else if (vehicleType == VehicleType.TRACTOR) {
                add(p, "tractor");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Exit(VehicleExitEvent e) {
        if (Compatibility.CompatibilityType.Vehicles.isFunctional()) {
            SpartanPlayer p = SpartanBukkit.getPlayer(e.getPlayer().getUniqueId());
            p.getBuffer().clear(key);
        }
    }

    private static void add(SpartanPlayer p, String type) {
        Buffer buffer = p.getBuffer();
        buffer.set(key + type, 1);
        buffer.setRemainingTicks(key + type, 20);
    }

    private static boolean has(Buffer buffer, String type) {
        return buffer.get(key + type) == 1 || buffer.getRemainingTicks(key + type) > 0;
    }

    public static boolean has(SpartanPlayer p, String type) {
        return has(p.getBuffer(), type);
    }

    public static boolean has(SpartanPlayer p, String[] types) {
        Buffer buffer = p.getBuffer();

        for (String type : types) {
            if (has(buffer, type)) {
                return true;
            }
        }
        return false;
    }
}
