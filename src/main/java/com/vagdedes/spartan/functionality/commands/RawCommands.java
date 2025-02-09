package com.vagdedes.spartan.functionality.commands;

import com.vagdedes.spartan.configuration.Config;
import com.vagdedes.spartan.functionality.important.Permissions;
import com.vagdedes.spartan.functionality.moderation.Spectate;
import com.vagdedes.spartan.functionality.notifications.AwarenessNotifications;
import com.vagdedes.spartan.gui.SpartanMenu;
import com.vagdedes.spartan.handlers.stability.Moderation;
import com.vagdedes.spartan.objects.replicates.SpartanPlayer;
import com.vagdedes.spartan.system.SpartanBukkit;
import com.vagdedes.spartan.utils.server.ConfigUtils;
import com.vagdedes.spartan.utils.server.PluginUtils;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RawCommands {

    private static final Map<String[], String[]> commands = new LinkedHashMap<>(4); // Attention
    private static final String[] reportCommand = new String[]{"Punishments.enable_raw_report_command", "report"};
    private static final String spectateCommand = "spectate";

    static {
        // 3 key length if functionality should be custom
        commands.put(new String[]{"Punishments.enable_raw_ban_commands", "false"}, new String[]{"ban", "tempban", "unban"});
        commands.put(new String[]{"Punishments.enable_raw_kick_command", "false"}, new String[]{"kick"});
        commands.put(new String[]{reportCommand[0], "false", ""}, new String[]{reportCommand[1]});
        commands.put(new String[]{"Punishments.enable_raw_spectate_command", "true"}, new String[]{spectateCommand});
    }

    static boolean isCommand(String cmd, String command) {
        return cmd.equalsIgnoreCase(command) || cmd.toLowerCase().startsWith(command + " ");
    }

    public static void create() {
        boolean notification = false;
        File file = Config.settings.getFile();

        for (Map.Entry<String[], String[]> entry : commands.entrySet()) {
            String[] option = entry.getKey();
            String optionKey = option[0];
            ConfigUtils.add(file, optionKey, option[1].equals("true"));

            if (!notification && Config.settings.getBoolean(optionKey)) {
                for (String command : entry.getValue()) {
                    if (!PluginUtils.isCommandRegistered(command)) {
                        notification = true;
                        break;
                    }
                }
            }
        }

        if (notification) {
            String notificationMessage = AwarenessNotifications.getOptionalNotification("Some raw commands have no tab completion.");

            if (notificationMessage != null) {
                List<SpartanPlayer> players = Permissions.getStaff();

                if (!players.isEmpty()) {
                    for (SpartanPlayer player : players) {
                        if (AwarenessNotifications.canSend(player.getUniqueId(), "raw-commands")) {
                            player.sendMessage(notificationMessage);
                        }
                    }
                }
            }
        }
    }

    public static boolean run(SpartanPlayer p, String cmd) {
        for (Map.Entry<String[], String[]> entry : commands.entrySet()) {
            String[] option = entry.getKey();

            if (option.length == 2 && Config.settings.getBoolean(option[0])) {
                for (String command : entry.getValue()) {
                    if (isCommand(cmd, "/" + command)) {
                        if (command.equals(spectateCommand)) {
                            Spectate.remove(p, false);
                        } else {
                            Bukkit.dispatchCommand(p.getPlayer(), "/spartan " + command);
                        }
                        return true;
                    }
                }
            }
        }
        return runReport(p, cmd);
    }

    static boolean runReport(SpartanPlayer p, String cmd) {
        if (RawCommands.isCommand(cmd, "/" + reportCommand[1])
                && Config.settings.getBoolean(reportCommand[0])) {
            if (!Permissions.has(p, Enums.Permission.REPORT)) {
                p.sendMessage(Config.messages.getColorfulString("no_permission"));
            } else {
                String[] args = cmd.split(" ");

                if (args.length == 2) {
                    SpartanPlayer t = SpartanBukkit.getPlayer(args[1]);

                    if (t == null) {
                        p.sendMessage(Config.messages.getColorfulString("player_not_found_message"));
                    } else {
                        SpartanMenu.playerReports.open(p, t);
                    }
                } else if (args.length >= 3) {
                    SpartanPlayer t = SpartanBukkit.getPlayer(args[1]);

                    if (t == null) {
                        p.sendMessage(Config.messages.getColorfulString("player_not_found_message"));
                    } else {
                        StringBuilder s = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            s.append(args[i]).append(" ");
                        }
                        Moderation.report(p, t, s.substring(0, s.length() - 1));
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Usage: /report <player> [reason]");
                }
            }
            return true;
        }
        return false;
    }
}
