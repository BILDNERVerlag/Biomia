package de.biomia.spigot.achievements;

import java.util.ArrayList;

@SuppressWarnings("SameParameterValue")
public class Achievement {

    //TODO: Neben das Inventar geklickt D:

    //ATTRIBUTES

    private final BiomiaAchievement achievement;
    private final int targetValue;
    private final String displayName;
    private String description = "Leider ist keine Beschreibung vorhanden. Beschwer dich am besten bei den Admins.";
    private String comment = null;

    //CONSTRUCTORS

    private Achievement(BiomiaAchievement achievement, BiomiaStat stat, int targetValue, String displayName) {
        this.achievement = achievement;
        this.targetValue = targetValue;
        this.displayName = displayName;
        if (!BiomiaAchievement.stats.containsKey(stat)) BiomiaAchievement.stats.put(stat, new ArrayList<>());
        BiomiaAchievement.stats.get(stat).add(this);
    }

    private Achievement(BiomiaAchievement achievement, BiomiaStat stat, int targetValue, String displayName, String comment) {
        this.achievement = achievement;
        this.targetValue = targetValue;
        this.displayName = displayName;
        this.comment = comment;

        if (!BiomiaAchievement.stats.containsKey(stat)) {
            BiomiaAchievement.stats.put(stat, new ArrayList<>());
        }
        BiomiaAchievement.stats.get(stat).add(this);
    }

    //METHODS

    public static void init() {
        new Achievement(BiomiaAchievement.VerdieneFuenftausendCoins, BiomiaStat.CoinsAccumulated, 5000, "Sparfuchs")
                .setDescription("Verdiene insgesamt mindestens 5000 BC.");
        new Achievement(BiomiaAchievement.OeffneZehnTruhen, BiomiaStat.ChestsOpened, 10, "Truhen\u00f6ffner")
                .setDescription("\u00d6ffne zehn Truhen.");
        new Achievement(BiomiaAchievement.Nimm20Fallschaden, BiomiaStat.HealthLost, 20, "Aua Aua", "FALL")
                .setDescription("Nimm 20 Fallschaden.");
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

    public int getTargetValue() {
        return targetValue;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}

