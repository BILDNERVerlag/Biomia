package de.biomiaAPI.achievements;

import java.util.ArrayList;

@SuppressWarnings("UnusedAssignment")
public class BiomiaAchievement {

    private final AchievementType achievement;

    private BiomiaAchievement() {
        this.achievement = AchievementType.VerdieneFuenftausendCoins;
        this.value = 5000;
        Stats.BiomiaStat stat1 = Stats.BiomiaStat.CoinsAccumulated;
        this.displayName = "Sparfuchs";

        if (!Stats.stats.containsKey(Stats.BiomiaStat.CoinsAccumulated)) {
            Stats.stats.put(Stats.BiomiaStat.CoinsAccumulated, new ArrayList<>());
        }
        Stats.stats.get(Stats.BiomiaStat.CoinsAccumulated).add(this);
    }

    public static void init() {
        new BiomiaAchievement()
                .setDescription("Verdiene insgesamt mindestens 5000 BC.");
    }
    private final int value;
    private String description = "Leider ist keine Beschreibung vorhanden. Beschwer dich am besten bei den Admins.";
    private final String displayName;

    private void setDescription(String description) {
        this.description = description;
    }

    public enum AchievementType {
        LogDichFuenfmalAufDemQuestServerEin, VerdieneFuenftausendCoins
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