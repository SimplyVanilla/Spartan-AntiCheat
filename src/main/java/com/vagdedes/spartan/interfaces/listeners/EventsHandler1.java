package com.vagdedes.spartan.interfaces.listeners;

import com.vagdedes.spartan.checks.inventory.ImpossibleInventory;
import com.vagdedes.spartan.compatibility.manual.damage.NoHitDelay;
import com.vagdedes.spartan.configuration.Config;
import com.vagdedes.spartan.functionality.chat.ChatProtection;
import com.vagdedes.spartan.functionality.important.Permissions;
import com.vagdedes.spartan.functionality.notifications.SuspicionNotifications;
import com.vagdedes.spartan.functionality.performance.MaximumCheckedPlayers;
import com.vagdedes.spartan.functionality.protections.Explosion;
import com.vagdedes.spartan.functionality.protections.LagLeniencies;
import com.vagdedes.spartan.functionality.protections.PlayerLimitPerIP;
import com.vagdedes.spartan.functionality.protections.ReconnectCooldown;
import com.vagdedes.spartan.functionality.synchronicity.SpartanEdition;
import com.vagdedes.spartan.functionality.synchronicity.cloud.CloudFeature;
import com.vagdedes.spartan.gui.SpartanMenu;
import com.vagdedes.spartan.handlers.stability.Cache;
import com.vagdedes.spartan.handlers.stability.DetectionLocation;
import com.vagdedes.spartan.objects.data.Handlers;
import com.vagdedes.spartan.objects.profiling.PlayerCombat;
import com.vagdedes.spartan.objects.replicates.SpartanPlayer;
import com.vagdedes.spartan.system.SpartanBukkit;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class EventsHandler1 implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void Join(PlayerJoinEvent e) {
        Player n = e.getPlayer();

        // Utils
        if (PlayerLimitPerIP.add(n)) {
            e.setJoinMessage(null);
            return;
        }
        SpartanPlayer p = SpartanBukkit.getPlayer(n);

        if (p == null) {
            return;
        }
        // Object
        p.getProfile().setOnline(p);

        // Protections
        LagLeniencies.add(p);

        // System
        MaximumCheckedPlayers.add(p);
        SpartanEdition.attemptNotification(p);

        SpartanBukkit.runDelayedTask(p, () -> {
            if (p != null) {
                // Configuration
                Config.settings.runOnLogin(p);

                // Features
                if (!SpartanMenu.mainMenu.notify(p)
                        && !CloudFeature.announce(p)) {
                    SuspicionNotifications.run(p);
                }
            }
        }, 10);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void Leave(PlayerQuitEvent e) {
        Player n = e.getPlayer();
        SpartanPlayer p = SpartanBukkit.removePlayer(n);

        if (p == null) {
            return;
        }
        // Features
        MaximumCheckedPlayers.remove(p);

        // Utils
        Permissions.remove(p);
        PlayerLimitPerIP.remove(p);

        // Features
        SpartanMenu.manageConfiguration.save(p, true);
        ReconnectCooldown.remove(n);
        ChatProtection.remove(p);

        // System
        LagLeniencies.remove(p);
        Cache.clear(p, n, true, true, false, null);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Death(PlayerDeathEvent e) {
        Player n = e.getEntity();
        SpartanPlayer p = SpartanBukkit.getPlayer(n);

        if (p == null) {
            return;
        }
        // Detections
        Cache.clearCheckCache(p);
        p.getExecutor(Enums.HackType.KillAura).handle(null);
        p.getExecutor(Enums.HackType.ImpossibleActions).run();
        p.getExecutor(Enums.HackType.AutoRespawn).run();
        p.getExecutor(Enums.HackType.ImpossibleInventory).handle(ImpossibleInventory.DEATH);

        // Protections
        p.resetHandlers(); // Always First

        // Utils
        DetectionLocation.update(p, p.getLocation(), true);

        // Objects
        Player killer = n.getKiller();

        if (killer != null && killer.isOnline()) {
            PlayerCombat combat = p.getProfile().getCombat();
            combat.setWinnerAgainst(killer.getName());
        }
        p.resetLocationData();
        p.setDead(true);
        p.setSleeping(false);
        p.setHealth(n.getHealth());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Respawn(PlayerRespawnEvent e) {
        Player n = e.getPlayer();
        SpartanPlayer p = SpartanBukkit.getPlayer(n);

        if (p == null) {
            return;
        }
        // Detections
        Cache.clearCheckCache(p);
        p.getExecutor(Enums.HackType.KillAura).handle(null);
        p.getExecutor(Enums.HackType.IrregularMovements).handle(null);

        // Utils
        DetectionLocation.update(p, p.getLocation(), true);

        // Objects
        p.resetLocationData();
        p.setDead(false);
        p.setSleeping(false);
        p.setHealth(n.getMaxHealth());

        // Protections
        p.resetHandlers(); // Always First
    }

    @EventHandler(priority = EventPriority.HIGHEST)

    private void Velocity(PlayerVelocityEvent e) {
        SpartanPlayer p = SpartanBukkit.getPlayer(e.getPlayer());

        if (p == null) {
            return;
        }
        // Compatibility
        NoHitDelay.runVelocity(p);

        // Handlers
        if (!e.isCancelled()) {
            p.getHandlers().add(Handlers.HandlerType.Velocity, 80);
            Explosion.runVelocity(p);
        }

        // Detections
        if (p.getViolations(Enums.HackType.Velocity).process()) {
            e.setCancelled(true);
        }
    }
}
