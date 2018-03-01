package de.biomia.achievements;

import java.util.ArrayList;

public class Achievements {

    //ATTRIBUTES

    private final BiomiaAchievement achievement;
    private final int value;
    private final String displayName;
    private String description = "Leider ist keine Beschreibung vorhanden. Beschwer dich am besten bei den Admins.";

    //CONSTRUCTOR

    private Achievements(BiomiaAchievement achievement, Stats.BiomiaStat stat, int value, String displayName) {
        this.achievement = achievement;
        this.value = value;
        this.displayName = displayName;

        if (!Stats.stats.containsKey(stat)) {
            Stats.stats.put(stat, new ArrayList<>());
        }
        Stats.stats.get(Stats.BiomiaStat.CoinsAccumulated).add(this);
    }

    //METHODS

    public static void init() {
        new Achievements(BiomiaAchievement.VerdieneFuenftausendCoins, Stats.BiomiaStat.CoinsAccumulated, 5000, "Sparfuchs")
                .setDescription("Verdiene insgesamt mindestens 5000 BC.");
        new Achievements(BiomiaAchievement.OeffneZehnTruhen, Stats.BiomiaStat.ChestsOpened, 10, "Truhenöffner")
                .setDescription("Öffne zehn Truhen.");
    }

    //SETTER AND GETTER

    private void setDescription(String description) {
        this.description = description;
    }

    public BiomiaAchievement getAchievement() {
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

