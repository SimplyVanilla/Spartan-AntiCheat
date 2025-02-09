package com.vagdedes.spartan.compatibility.manual.abilities.crackshot;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import com.shampaggon.crackshot.events.WeaponPreShootEvent;
import com.shampaggon.crackshot.events.WeaponScopeEvent;
import com.shampaggon.crackshot.events.WeaponShootEvent;
import com.vagdedes.spartan.configuration.Compatibility;
import com.vagdedes.spartan.handlers.identifiers.simple.CheckProtection;
import com.vagdedes.spartan.objects.data.Buffer;
import com.vagdedes.spartan.objects.replicates.SpartanPlayer;
import com.vagdedes.spartan.system.SpartanBukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CrackShot implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void WeaponScope(WeaponScopeEvent e) {
        if (Compatibility.CompatibilityType.CrackShot.isFunctional()) {
            SpartanPlayer p = SpartanBukkit.getPlayer(e.getPlayer());

            if (p == null) {
                return;
            }
            if (!e.isCancelled()) {
                Buffer buffer = p.getBuffer();
                CheckProtection.evadeStandardCombatFPs(p, Compatibility.CompatibilityType.CrackShot, 20);

                if (e.isZoomIn()) {
                    buffer.set("crackshot=compatibility=scope", 1);
                } else {
                    buffer.remove("crackshot=compatibility=scope");
                }
            } else {
                p.getBuffer().remove("crackshot=compatibility=scope");
            }
        }
    }

    @EventHandler
    private void WeaponPreShoot(WeaponPreShootEvent e) {
        if (Compatibility.CompatibilityType.CrackShot.isFunctional()) {
            CheckProtection.evadeStandardCombatFPs(e.getPlayer(), Compatibility.CompatibilityType.CrackShot, 40);
        }
    }

    @EventHandler
    private void WeaponShoot(WeaponShootEvent e) {
        if (Compatibility.CompatibilityType.CrackShot.isFunctional()) {
            CheckProtection.evadeStandardCombatFPs(e.getPlayer(), Compatibility.CompatibilityType.CrackShot, 40);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void WeaponDamageEntity(WeaponDamageEntityEvent e) {
        if (Compatibility.CompatibilityType.CrackShot.isFunctional()) {
            SpartanPlayer p = SpartanBukkit.getPlayer(e.getVictim().getUniqueId());

            if (p != null) {
                CheckProtection.evadeStandardCombatFPs(p, Compatibility.CompatibilityType.CrackShot, 60);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void EntityDamage(EntityDamageEvent e) {
        if (Compatibility.CompatibilityType.CrackShot.isFunctional()) {
            SpartanPlayer p = SpartanBukkit.getPlayer(e.getEntity().getUniqueId());

            if (p != null && isUsingScope(p)) {
                CheckProtection.evadeStandardCombatFPs(p, Compatibility.CompatibilityType.CrackShot, 60);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void EntityByEntityDamage(EntityDamageByEntityEvent e) {
        if (Compatibility.CompatibilityType.CrackShot.isFunctional()) {
            SpartanPlayer p = SpartanBukkit.getPlayer(e.getDamager().getUniqueId());

            if (p != null && isUsingScope(p)) {
                CheckProtection.evadeStandardCombatFPs(p, Compatibility.CompatibilityType.CrackShot, 60);
            }
        }
    }

    public static boolean isUsingScope(SpartanPlayer p) {
        return Compatibility.CompatibilityType.CrackShot.isFunctional() && p.getBuffer().get("crackshot=compatibility=scope") != 0
                || CrackShotPlus.isUsingScope(p);
    }
}
