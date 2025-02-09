package com.vagdedes.spartan.functionality.configuration;

import com.vagdedes.spartan.Register;
import com.vagdedes.spartan.configuration.Config;
import com.vagdedes.spartan.functionality.notifications.DetectionNotifications;
import com.vagdedes.spartan.functionality.synchronicity.CrossServerInformation;
import com.vagdedes.spartan.objects.replicates.SpartanPlayer;
import com.vagdedes.spartan.objects.system.Check;
import com.vagdedes.spartan.system.SpartanBukkit;
import com.vagdedes.spartan.utils.java.TimeUtils;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AntiCheatLogs {

    public static final String
            dateFormatChanger = ".",
            usualDateFormat = "yyyy/MM/dd HH:mm:ss",
            dateFormat = "yyyy/MM/dd HH:mm:ss" + dateFormatChanger + "SSSSSSSSS";
    private static final String logsFolder = Register.plugin.getDataFolder() + "/logs";

    private static int id = 1;
    private static Timestamp time = new Timestamp(System.currentTimeMillis());
    private static File savedFile = null;
    private static YamlConfiguration fileConfiguration = null;

    public static void refresh() {
        id = 1;
        time = new Timestamp(System.currentTimeMillis());
        save();

        // Always Last
        savedFile = null;
        fileConfiguration = null;
    }

    public static String syntaxDate(String date, int id) {
        return "(" + id + ")[" + date + "]";
    }

    // Separator

    private static void save() {
        if (savedFile != null && fileConfiguration != null) {
            try {
                fileConfiguration.save(savedFile);
            } catch (Exception ignored) {
            }
        }
    }

    private static void storeInFile(boolean playerRelated, String information) {
        id++;
        fileConfiguration.set(
                syntaxDate(DateTimeFormatter.ofPattern(dateFormat).format(LocalDateTime.now()), id),
                information
        );

        if (!playerRelated || (id % Check.sufficientViolations) == 0) {
            if (SpartanBukkit.isSynchronised()) {
                SpartanBukkit.storageThread.executeIfFreeElseHere(AntiCheatLogs::save);
            } else {
                save();
            }
        }
    }

    // Separator

    public static void silentLogInfo(String info) {
        logInfo(null, info, null, null, null, false, true, -1, -1);
    }

    public static void logInfo(String info) {
        logInfo(null, info, info, null, null, false, true, -1, -1);
    }

    public static void logInfo(SpartanPlayer p, String information, String console,
                               Material material, Enums.HackType hackType,
                               boolean falsePositive, boolean cloudFeature,
                               int violations, int cancelViolation) {
        boolean playerRelated = p != null;

        if (console != null
                && Config.settings.getBoolean("Logs.log_console")
                && (!playerRelated
                || DetectionNotifications.getPlayers(true).isEmpty())) {
            Bukkit.getConsoleSender().sendMessage(console);
        }
        if (Config.settings.getBoolean("Logs.log_file")) {
            Timestamp now = new Timestamp(System.currentTimeMillis());

            if (savedFile == null || fileConfiguration == null || time.getDay() != now.getDay()) {
                save();
                id = 1;
                time = now;
                savedFile = new File(logsFolder);

                if (savedFile.exists() && savedFile.isDirectory() || savedFile.mkdirs()) {
                    savedFile = new File(logsFolder + "/log" + TimeUtils.getYearMonthDay(time).replace(TimeUtils.dateSeparator, "") + ".yml");

                    try {
                        if (savedFile.createNewFile()) {
                            fileConfiguration = YamlConfiguration.loadConfiguration(savedFile);
                            storeInFile(playerRelated, information);
                        } else {
                            fileConfiguration = null;
                            savedFile = null;
                        }
                    } catch (Exception ignored) {
                        fileConfiguration = null;
                        savedFile = null;
                    }
                } else {
                    fileConfiguration = null;
                    savedFile = null;
                }
            } else {
                storeInFile(playerRelated, information);
            }
        }
        if (cloudFeature) { // When the Cloud feature is not used, it's when incoming Cloud data is locally stored, so we don't send it to the Cloud or to the SQL database to avoid redundancy
            if (Config.sql.isEnabled()) {
                boolean miningNotification = playerRelated && material != null;
                Config.sql.logInfo(p, information, material, hackType, falsePositive, miningNotification, violations, cancelViolation);
            }
            CrossServerInformation.queueLog(information);
        }
    }
}
