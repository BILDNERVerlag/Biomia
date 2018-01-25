package de.biomiaAPI.achievements;

import java.util.ArrayList;

@SuppressWarnings("UnusedAssignment")
public class BiomiaAchievement {

    public static void init(){
        new BiomiaAchievement(AchievementType.VerdieneFuenftausendCoins, 5000, Stats.BiomiaStat.CoinsAccumulated, "GeldAchievement")
        .setDescription("Verdiene insgesamt mindestens 5000 BC.");
    }

    public enum AchievementType {
        LogDichFuenfmalAufDemQuestServerEin, VerdieneFuenftausendCoins;
    }

    private AchievementType achievement;
    private final int value;
    private final Stats.BiomiaStat stat;
    private String description = "Leider ist keine Beschreibung vorhanden. Beschwer dich am besten bei den Admins.";
    private final String displayName;

    BiomiaAchievement(AchievementType achievment, int mindestWert, Stats.BiomiaStat stat, String displayName) {
        this.achievement = achievment;
        this.value = mindestWert;
        this.stat = stat;
        this.displayName = displayName;

        if (!Stats.stats.containsKey(stat)) {
            Stats.stats.put(stat, new ArrayList<>());
        }
        Stats.stats.get(stat).add(this);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AchievementType getAchievement() {
        return achievement;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMindestWert() {
        return value;
    }
}