package com.vagdedes.spartan.objects.profiling;

import com.vagdedes.spartan.configuration.Config;
import com.vagdedes.spartan.functionality.configuration.AntiCheatLogs;
import com.vagdedes.spartan.handlers.stability.ResearchEngine;
import com.vagdedes.spartan.objects.replicates.SpartanPlayer;
import com.vagdedes.spartan.system.SpartanBukkit;
import me.vagdedes.spartan.system.Enums;

public class PlayerFight {

    // Properties
    public static final long maxFightTime = 20_000L;
    public static final int requiredHits = 4;

    // Keys
    public static final String[] outcome = new String[]{"judged", "kill"};
    public static final String
            finderKey = " won the fight against ",
            outcomeKey = "outcome",
            durationKey = "duration",
            hitsKey = "hits",
            hitTimeAverageKey = "hit-time-average",
            reachAverageKey = "reach-average",
            maxCpsKey = "max-cps",
            cpsKey = "cps",
            yawRateKey = "yaw-rate",
            pitchRateKey = "pitch-rate",
            maxHitComboKey = "max-hit-combo";

    // Backup Keys
    public static final String oldVerticalVelocityKey = "velocity";

    // Separators
    public static final String
            introductorySeparator = ":",
            majorSeparator = ", ";
    public static final String[] intermediateSeparator = new String[]{"|", "\\|"}; // One for logs, the other for the code

    // Separator

    private final PlayerOpponent opponent1, opponent2;
    private PlayerOpponent winner, loser;
    private boolean judged;

    public PlayerFight(PlayerOpponent winner, PlayerOpponent loser, boolean judged) { // Used for logs
        this.opponent1 = winner;
        this.opponent2 = loser;
        this.winner = winner;
        this.loser = loser;
        this.judged = judged;

        if (!winner.getName().equals(loser.getName())) { // Log manipulation protection
            ResearchEngine.getPlayerProfile(winner.getName()).getCombat().storeFight(this, false);
            ResearchEngine.getPlayerProfile(loser.getName()).getCombat().storeFight(this, false);
        }
    }

    PlayerFight(PlayerOpponent opponent1, PlayerOpponent opponent2) { // Used for memory
        this.opponent1 = opponent1;
        this.opponent2 = opponent2;
        this.winner = null;
        this.loser = null;
        this.judged = false;
        ResearchEngine.getPlayerProfile(opponent1.getName()).getCombat().createFight(this);
        ResearchEngine.getPlayerProfile(opponent2.getName()).getCombat().createFight(this);
    }

    // Separator

    public PlayerOpponent[] getOpponent(PlayerOpponent opponent) { // Use only when player is in said fight
        PlayerOpponent opponent1 = getOpponent1();
        return opponent1 == opponent ?
                new PlayerOpponent[]{opponent1, getOpponent2()} :
                new PlayerOpponent[]{getOpponent2(), opponent1};
    }

    public PlayerOpponent[] getOpponent(String name) { // Use only when player is in said fight
        PlayerOpponent opponent1 = getOpponent1();
        return opponent1.getName().equals(name) ?
                new PlayerOpponent[]{opponent1, getOpponent2()} :
                new PlayerOpponent[]{getOpponent2(), opponent1};
    }

    public PlayerOpponent[] getOpponent(SpartanPlayer player) { // Use only when player is in said fight
        return getOpponent(player.getName());
    }

    // Separator

    public PlayerOpponent[] getOpponents() {
        return new PlayerOpponent[]{opponent1, opponent2};
    }

    public PlayerOpponent getOpponent1() {
        return opponent1;
    }

    public PlayerOpponent getOpponent2() {
        return opponent2;
    }

    public PlayerOpponent getWinner() {
        return winner;
    }

    public PlayerOpponent getLoser() {
        return loser;
    }

    // Separator

    public double getHitRatio(PlayerOpponent[] opponents) {
        int targetHits = opponents[1].getHits();
        return targetHits == 0 ? opponents[0].getHits() : (opponents[0].getHits() / ((double) targetHits));
    }

    public double getHitRatio(PlayerOpponent opponent) {
        return getHitRatio(getOpponent(opponent));
    }

    public int getHits() {
        return opponent1.getHits() + opponent2.getHits();
    }

    public boolean isJudged() {
        return judged;
    }

    public boolean isKill() {
        return !judged;
    }

    public boolean hasEnded() {
        return opponent1.hasEnded() && opponent2.hasEnded();
    }

    public long getDuration() {
        return Math.min(opponent1.getDuration(), opponent2.getDuration());
    }

    // Separator

    public void setWinner(String name) { // Use only when player is in said fight
        if (this.winner == null && this.loser == null) {
            PlayerOpponent[] opponents = getOpponent(name);
            PlayerOpponent winnerOpponent = opponents[0]; // First is the player is in the method
            PlayerOpponent loserOpponent = opponents[1];
            int winnerHits = winnerOpponent.getHits();
            int loserHits = loserOpponent.getHits();

            if (winnerHits > 0 && loserHits > 0 && (winnerHits + loserHits) >= requiredHits) {
                long time = System.currentTimeMillis();
                winnerOpponent.end(time);
                loserOpponent.end(time);
                this.winner = winnerOpponent;
                this.loser = loserOpponent;
                store();
            } else {
                delete();
            }
        }
    }

    public void judge() {
        if (this.winner == null && this.loser == null) {
            PlayerOpponent opponent1 = getOpponent1();
            PlayerOpponent opponent2 = getOpponent2();
            int opponentHits1 = opponent1.getHits();
            int opponentHits2 = opponent2.getHits();

            if (opponentHits1 > 0 && opponentHits2 > 0 && (opponentHits1 + opponentHits2) >= requiredHits) {
                long time = System.currentTimeMillis();
                opponent1.end(time);
                opponent2.end(time);
                this.judged = true;

                if (opponentHits1 > opponentHits2) {
                    this.winner = opponent1;
                    this.loser = opponent2;
                    store();
                } else if (opponentHits1 != opponentHits2) {
                    this.winner = opponent2;
                    this.loser = opponent1;
                    store();
                } else { // No draw outcome
                    delete();
                }
            } else {
                delete();
            }
        }
    }

    // Separator

    private void delete() {
        for (PlayerOpponent opponent : new PlayerOpponent[]{opponent1, opponent2}) {
            ResearchEngine.getPlayerProfile(opponent.getName()).getCombat().removeFight(this, false);
        }
    }

    private void store() {
        // Memory
        for (PlayerOpponent opponent : new PlayerOpponent[]{opponent1, opponent2}) {
            ResearchEngine.getPlayerProfile(opponent.getName()).getCombat().removeFight(this, true);
        }

        // Logs (DO NOT FORGET THE MAJOR KEYS)
        String intermediateSeparator = PlayerFight.intermediateSeparator[0],
                basic = Config.getConstruct() + winner.getName() + finderKey + loser.getName(),
                advanced = introductorySeparator + " "
                        + outcomeKey + intermediateSeparator + (judged ? outcome[0] : outcome[1]) + majorSeparator
                        + durationKey + intermediateSeparator + getDuration() + majorSeparator
                        + hitsKey + intermediateSeparator + winner.getHits() + intermediateSeparator + loser.getHits() + majorSeparator
                        + hitTimeAverageKey + intermediateSeparator + winner.getHitTimeAverage() + intermediateSeparator + loser.getHitTimeAverage() + majorSeparator
                        + reachAverageKey + intermediateSeparator + winner.getReachAverage() + intermediateSeparator + loser.getReachAverage() + majorSeparator
                        + maxCpsKey + intermediateSeparator + winner.getMaxCPS() + intermediateSeparator + loser.getMaxCPS() + majorSeparator
                        + cpsKey + intermediateSeparator + winner.getCPSAverage() + intermediateSeparator + loser.getCPSAverage() + majorSeparator
                        + yawRateKey + intermediateSeparator + winner.getYawRateAverage() + intermediateSeparator + loser.getYawRateAverage() + majorSeparator
                        + pitchRateKey + intermediateSeparator + winner.getPitchRateAverage() + intermediateSeparator + loser.getPitchRateAverage() + majorSeparator
                        + maxHitComboKey + intermediateSeparator + winner.getMaxHitCombo() + intermediateSeparator + loser.getMaxHitCombo();

        // Storage
        AntiCheatLogs.silentLogInfo(basic + advanced);

        // Detections
        SpartanPlayer player = SpartanBukkit.getPlayer(winner.getName());

        if (player != null) {
            player.getExecutor(Enums.HackType.KillAura).handle(winner);
        }
    }
}
