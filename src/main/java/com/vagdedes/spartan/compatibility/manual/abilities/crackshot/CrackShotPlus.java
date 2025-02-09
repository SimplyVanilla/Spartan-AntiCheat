package com.vagdedes.spartan.compatibility.manual.abilities.crackshot;

import com.vagdedes.spartan.configuration.Compatibility;
import com.vagdedes.spartan.handlers.identifiers.simple.CheckProtection;
import com.vagdedes.spartan.objects.data.Buffer;
import com.vagdedes.spartan.objects.replicates.SpartanPlayer;
import com.vagdedes.spartan.system.SpartanBukkit;
import me.DeeCaaD.CrackShotPlus.Events.WeaponSecondScopeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CrackShotPlus implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void WeaponScope(WeaponSecondScopeEvent e) {
        if (Compatibility.CompatibilityType.CrackShotPlus.isFunctional()) {
            SpartanPlayer p = SpartanBukkit.getPlayer(e.getPlayer());

            if (p == null) {
                return;
            }
            if (!e.isCancelled()) {
                Buffer buffer = p.getBuffer();
                CheckProtection.evadeStandardCombatFPs(p, Compatibility.CompatibilityType.CrackShotPlus, 20);

                if (e.isZoomIn()) {
                    buffer.set("crackshotplus=compatibility=scope", 1);
                } else {
                    buffer.remove("crackshotplus=compatibility=scope");
                }
            } else {
                p.getBuffer().remove("crackshotplus=compatibility=scope");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void EntityDamage(EntityDamageEvent e) {
        if (Compatibility.CompatibilityType.CrackShotPlus.isFunctional()) {
            SpartanPlayer p = SpartanBukkit.getPlayer(e.getEntity().getUniqueId());

            if (p != null && isUsingScope(p)) {
                CheckProtection.evadeStandardCombatFPs(p, Compatibility.CompatibilityType.CrackShotPlus, 60);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void EntityByEntityDamage(EntityDamageByEntityEvent e) {
        if (Compatibility.CompatibilityType.CrackShotPlus.isFunctional()) {
            SpartanPlayer p = SpartanBukkit.getPlayer(e.getDamager().getUniqueId());

            if (p != null && isUsingScope(p)) {
                CheckProtection.evadeStandardCombatFPs(p, Compatibility.CompatibilityType.CrackShotPlus, 30);
            }
        }
    }

    static boolean isUsingScope(SpartanPlayer p) {
        return Compatibility.CompatibilityType.CrackShotPlus.isFunctional()
                && p.getBuffer().get("crackshotplus=compatibility=scope") != 0;
    }
}

