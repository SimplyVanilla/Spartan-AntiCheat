package com.vagdedes.spartan.api;

import com.vagdedes.spartan.system.Enums.HackType;
import com.vagdedes.spartan.system.Enums.Permission;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

// Props to the 16-year-old me for thinking of this creative class name
public class API {

    public static String licenseID() {
        return BackgroundAPI.licenseID();
    }

    public static String getVersion() {
        return BackgroundAPI.getVersion();
    }

    public static String getMessage(String path) {
        return BackgroundAPI.getMessage(path);
    }

    public static boolean getSetting(String path) {
        return BackgroundAPI.getSetting(path);
    }

    public static String getCategory(Player p, HackType hackType) {
        return BackgroundAPI.getCategory(p, hackType);
    }

    @Deprecated
    public static boolean hasVerboseEnabled(Player p) {
        return BackgroundAPI.hasVerboseEnabled(p);
    }

    public static boolean hasNotificationsEnabled(Player p) {
        return BackgroundAPI.hasNotificationsEnabled(p);
    }

    public static int getViolationResetTime() {
        return BackgroundAPI.getViolationResetTime();
    }

    @Deprecated
    public static void setVerbose(Player p, boolean value) {
        BackgroundAPI.setVerbose(p, value);
    }

    public static void setNotifications(Player p, boolean value) {
        BackgroundAPI.setNotifications(p, value);
    }

    @Deprecated
    public static void setVerbose(Player p, boolean value, int frequency) {
        BackgroundAPI.setVerbose(p, value, frequency);
    }

    public static void setNotifications(Player p, int frequency) {
        BackgroundAPI.setNotifications(p, frequency);
    }

    public static int getPing(Player p) {
        return BackgroundAPI.getPing(p);
    }

    public static double getTPS() {
        return BackgroundAPI.getTPS();
    }

    public static boolean hasPermission(Player p, Permission Permission) {
        return BackgroundAPI.hasPermission(p, Permission);
    }

    public static boolean isEnabled(HackType HackType) {
        return BackgroundAPI.isEnabled(HackType);
    }

    public static boolean isSilent(HackType HackType) {
        return BackgroundAPI.isSilent(HackType);
    }

    public static int getVL(Player p, HackType HackType) {
        return BackgroundAPI.getVL(p, HackType);
    }

    @Deprecated
    public static double getDecimalVL(Player p, HackType HackType) {
        return BackgroundAPI.getDecimalVL(p, HackType);
    }

    public static int getVL(Player p) {
        return BackgroundAPI.getVL(p);
    }

    @Deprecated
    public static void setVL(Player p, HackType HackType, int amount) {
        BackgroundAPI.setVL(p, HackType, amount);
    }

    @Deprecated
    public static int getCancelViolation(HackType hackType, String worldName) {
        return BackgroundAPI.getCancelViolation(hackType, worldName);
    }

    public static int getCancelViolation(HackType hackType) {
        return BackgroundAPI.getCancelViolation(hackType);
    }

    public static int getViolationDivisor(Player p, HackType hackType) {
        return BackgroundAPI.getViolationDivisor(p, hackType);
    }

    public static void reloadConfig() {
        BackgroundAPI.reloadConfig();
    }

    public static void reloadPermissions() {
        BackgroundAPI.reloadPermissions();
    }

    public static void reloadPermissions(Player p) {
        BackgroundAPI.reloadPermissions(p);
    }

    public static void enableCheck(HackType HackType) {
        BackgroundAPI.enableCheck(HackType);
    }

    public static void disableCheck(HackType HackType) {
        BackgroundAPI.disableCheck(HackType);
    }

    public static void enableSilentChecking(Player p, HackType HackType) {
        BackgroundAPI.enableSilentChecking(p, HackType);
    }

    public static void disableSilentChecking(Player p, HackType HackType) {
        BackgroundAPI.disableSilentChecking(p, HackType);
    }

    public static void enableSilentChecking(HackType HackType) {
        BackgroundAPI.enableSilentChecking(HackType);
    }

    public static void disableSilentChecking(HackType HackType) {
        BackgroundAPI.disableSilentChecking(HackType);
    }

    public static void cancelCheck(Player p, HackType HackType, int ticks) {
        BackgroundAPI.cancelCheck(p, HackType, ticks);
    }

    public static void cancelCheckPerVerbose(Player p, String string, int ticks) {
        BackgroundAPI.cancelCheckPerVerbose(p, string, ticks);
    }

    public static void startCheck(Player p, HackType HackType) {
        BackgroundAPI.startCheck(p, HackType);
    }

    public static void stopCheck(Player p, HackType HackType) {
        BackgroundAPI.stopCheck(p, HackType);
    }

    public static void resetVL() {
        BackgroundAPI.resetVL();
    }

    public static void resetVL(Player p) {
        BackgroundAPI.resetVL(p);
    }

    public static boolean isBypassing(Player p) {
        return BackgroundAPI.isBypassing(p);
    }

    public static boolean isBypassing(Player p, HackType HackType) {
        return BackgroundAPI.isBypassing(p, HackType);
    }

    public static void banPlayer(UUID uuid, String reason) {
        BackgroundAPI.banPlayer(uuid, reason);
    }

    public static boolean isBanned(UUID uuid) {
        return BackgroundAPI.isBanned(uuid);
    }

    public static void unbanPlayer(UUID uuid) {
        BackgroundAPI.unbanPlayer(uuid);
    }

    public static String getBanReason(UUID uuid) {
        return BackgroundAPI.getBanReason(uuid);
    }

    public static String getBanPunisher(UUID uuid) {
        return BackgroundAPI.getBanPunisher(uuid);
    }

    public static boolean isHacker(Player p) {
        return BackgroundAPI.isHacker(p);
    }

    public static boolean isLegitimate(Player p) {
        return BackgroundAPI.isLegitimate(p);
    }

    @Deprecated
    public static boolean hasMiningNotificationsEnabled(Player p) {
        return BackgroundAPI.hasMiningNotificationsEnabled(p);
    }

    @Deprecated
    public static void setMiningNotifications(Player p, boolean value) {
        BackgroundAPI.setMiningNotifications(p, value);
    }

    public static int getCPS(Player p) {
        return BackgroundAPI.getCPS(p);
    }

    public static UUID[] getBanList() {
        return BackgroundAPI.getBanList();
    }

    public static boolean addToWave(UUID uuid, String command) {
        return BackgroundAPI.addToWave(uuid, command);
    }

    public static void removeFromWave(UUID uuid) {
        BackgroundAPI.removeFromWave(uuid);
    }

    public static void clearWave() {
        BackgroundAPI.clearWave();
    }

    public static void runWave() {
        BackgroundAPI.runWave();
    }

    public static UUID[] getWaveList() {
        return BackgroundAPI.getWaveList();
    }

    public static int getWaveSize() {
        return BackgroundAPI.getWaveSize();
    }

    public static boolean isAddedToTheWave(UUID uuid) {
        return BackgroundAPI.isAddedToTheWave(uuid);
    }

    @Deprecated
    public static void warnPlayer(Player p, String reason) {
        BackgroundAPI.warnPlayer(p, reason);
    }

    public static void addPermission(Player p, Permission permission) {
        BackgroundAPI.addPermission(p, permission);
    }

    @Deprecated
    public static void sendClientSidedBlock(Player p, Location loc, Material m, byte b) {
        BackgroundAPI.sendClientSidedBlock(p, loc, m, b);
    }

    @Deprecated
    public static void destroyClientSidedBlock(Player p, Location loc) {
        BackgroundAPI.destroyClientSidedBlock(p, loc);
    }

    @Deprecated
    public static void removeClientSidedBlocks(Player p) {
        BackgroundAPI.removeClientSidedBlocks(p);
    }

    @Deprecated
    public static boolean containsClientSidedBlock(Player p, Location loc) {
        return BackgroundAPI.containsClientSidedBlock(p, loc);
    }

    @Deprecated
    public static Material getClientSidedBlockMaterial(Player p, Location loc) {
        return BackgroundAPI.getClientSidedBlockMaterial(p, loc);
    }

    @Deprecated
    public static byte getClientSidedBlockData(Player p, Location loc) {
        return BackgroundAPI.getClientSidedBlockData(p, loc);
    }

    public static void disableVelocityProtection(Player p, int ticks) {
        BackgroundAPI.disableVelocityProtection(p, ticks);
    }

    public static String getConfiguredCheckName(HackType hackType) {
        return BackgroundAPI.getConfiguredCheckName(hackType);
    }

    public static void setConfiguredCheckName(HackType hackType, String name) {
        BackgroundAPI.setConfiguredCheckName(hackType, name);
    }

    public static void setOnGround(Player p, int ticks) {
        BackgroundAPI.setOnGround(p, ticks);
    }

    @Deprecated
    public static int getMaxPunishmentViolation(HackType hackType) {
        return BackgroundAPI.getMaxPunishmentViolation(hackType);
    }

    @Deprecated
    public static int getMinPunishmentViolation(HackType hackType) {
        return BackgroundAPI.getMinPunishmentViolation(hackType);
    }

    @Deprecated
    public static boolean mayPunishPlayer(Player p, HackType hackType) {
        return BackgroundAPI.mayPunishPlayer(p, hackType);
    }
}
