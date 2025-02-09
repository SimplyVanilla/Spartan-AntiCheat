package com.vagdedes.spartan.system;

import com.vagdedes.spartan.Register;
import com.vagdedes.spartan.compatibility.manual.essential.protocollib.ProtocolLib;
import com.vagdedes.spartan.configuration.Config;
import com.vagdedes.spartan.functionality.important.MultiVersion;
import com.vagdedes.spartan.handlers.connection.IDs;
import com.vagdedes.spartan.handlers.connection.Piracy;
import com.vagdedes.spartan.handlers.stability.TPS;
import com.vagdedes.spartan.handlers.stability.TestServer;
import com.vagdedes.spartan.objects.data.Cooldowns;
import com.vagdedes.spartan.objects.replicates.SpartanPlayer;
import com.vagdedes.spartan.objects.system.Threads;
import com.vagdedes.spartan.utils.server.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpartanBukkit {

    public static final boolean
            supportedFork = MultiVersion.fork().equals("Spigot") || MultiVersion.fork().equals("Paper"),
            canAdvertise = !Piracy.enabled || IDs.isBuiltByBit() || IDs.isSongoda() || IDs.isPolymart(),
            hasResourcePack;
    public static Cooldowns cooldowns = new Cooldowns(false);

    static {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_18)) {
            boolean result = false;

            for (String option : new String[]{
                    Bukkit.getResourcePack(), Bukkit.getResourcePackHash(), Bukkit.getResourcePackPrompt()
            }) {
                if (!option.isEmpty()) {
                    result = true;
                    break;
                }
            }
            hasResourcePack = result;
        } else {
            hasResourcePack = false;
        }
    }

    public static final Threads.ThreadPool
            analysisThread = new Threads.ThreadPool(TPS.tickTime),
            connectionThread = new Threads.ThreadPool(TPS.tickTime),
            storageThread = new Threads.ThreadPool(5L),
            playerThread = MultiVersion.folia ? null : new Threads.ThreadPool(2L),
            chunkThread = MultiVersion.folia ? null : new Threads.ThreadPool(1L),
            detectionThread = MultiVersion.folia ? null : new Threads.ThreadPool(1L);

    public static final int hashCodeMultiplier = 31;
    private static final ConcurrentHashMap<UUID, SpartanPlayer> players = new ConcurrentHashMap<>(Config.getMaxPlayers());
    public static final UUID uuid = UUID.randomUUID();
    public static final Class<?> craftPlayer = ReflectionUtils.getClass(
            ReflectionUtils.class.getPackage().getName().substring(0, 19) // Package
                    + "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().substring(23) + ".entity.CraftPlayer" // Version
    );

    public static void clear() {
        players.clear();
    }

    public static boolean isPlayer(UUID uuid) {
        return players.containsKey(uuid);
    }

    public static SpartanPlayer getPlayer(String name) {
        if (!players.isEmpty()) {
            name = name.toLowerCase();

            for (SpartanPlayer p : players.values()) {
                if (p.getName().toLowerCase().equals(name)) {
                    return p;
                }
            }
        }
        return null;
    }

    public static SpartanPlayer getPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player == null ? null : getPlayer(player);
    }

    public static SpartanPlayer getPlayer(Player real) {
        if (!ProtocolLib.isTemporaryPLayer(real)) { // Temporary players have no UUIDs
            UUID uuid = real.getUniqueId();
            SpartanPlayer player = players.get(uuid);

            if (player == null && real.getAddress() != null) {
                players.put(uuid, player = new SpartanPlayer(real, uuid));
            }
            return player;
        }
        return null;
    }

    public static SpartanPlayer removePlayer(Player real) {
        return players.remove(real.getUniqueId());
    }

    public static boolean isProductionServer() {
        return !TestServer.isIdentified()
                && players.size() >= 7;
    }

    public static int getPlayerCount() {
        return players.size();
    }

    public static Set<UUID> getUUIDs() {
        return new HashSet<>(players.keySet());
    }

    public static List<SpartanPlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }

    // Separator

    public static boolean isSynchronised() {
        return Bukkit.isPrimaryThread()
                || !Register.isPluginEnabled();
    }

    // Separator

    public static Object getCraftPlayerMethod(Player p, String path) {
        if (craftPlayer != null) {
            try {
                Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
                return handle.getClass().getDeclaredField(path).get(handle);
            } catch (Exception ignored) {
            }
        }
        return -1;
    }

    // Separator

    public static Object runDelayedTask(SpartanPlayer player, Runnable runnable, long start) {
        return SpartanScheduler.schedule(player, runnable, start, -1L);
    }

    public static Object runRepeatingTask(SpartanPlayer player, Runnable runnable, long start, long repetition) {
        return SpartanScheduler.schedule(player, runnable, start, repetition);
    }

    public static Object runDelayedTask(Runnable runnable, long start) {
        return SpartanScheduler.schedule(null, runnable, start, -1L);
    }

    public static Object runRepeatingTask(Runnable runnable, long start, long repetition) {
        return SpartanScheduler.schedule(null, runnable, start, repetition);
    }

    public static void runTask(SpartanPlayer player, Runnable runnable) {
        SpartanScheduler.run(player, runnable, false);
    }

    public static void runTask(World world, int x, int z, Runnable runnable) {
        SpartanScheduler.run(world, x, z, runnable, false);
    }

    public static void transferTask(SpartanPlayer player, Runnable runnable) {
        SpartanScheduler.run(player, runnable, true);
    }

    public static void transferTask(World world, int x, int z, Runnable runnable) {
        SpartanScheduler.run(world, x, z, runnable, true);
    }

    public static void transferTask(Runnable runnable) {
        SpartanScheduler.transfer(runnable);
    }

    public static void cancelTask(Object task) {
        SpartanScheduler.cancel(task);
    }
}
