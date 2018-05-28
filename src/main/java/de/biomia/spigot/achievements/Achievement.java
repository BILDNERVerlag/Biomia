package de.biomia.spigot.achievements;

import java.util.ArrayList;

@SuppressWarnings("SameParameterValue")
public class Achievement {

    //ATTRIBUTES

    private final BiomiaAchievement achievement;
    private final int targetValue;
    private final String displayName;
    private String description = "Leider ist keine Beschreibung vorhanden. Beschwer dich am besten bei den Developer.";
    private String comment = null;

    //CONSTRUCTORS

    private Achievement(BiomiaAchievement achievement, BiomiaStat stat, int targetValue, String displayName) {
        this(achievement, stat, targetValue, displayName, null);
    }

    private Achievement(BiomiaAchievement achievement, BiomiaStat stat, int targetValue, String displayName, String comment) {
        this.achievement = achievement;
        this.targetValue = targetValue;
        this.displayName = displayName;
        this.comment = comment;
        BiomiaAchievement.stats.computeIfAbsent(stat, biomiaStat -> new ArrayList<>()).add(this);
    }

    //METHODS

    public static void init() {
        new Achievement(BiomiaAchievement.VerdieneFuenftausendCoins, BiomiaStat.CoinsAccumulated, 5000, "Sparfuchs")
                .setDescription("Verdiene insgesamt mindestens 5000 BC.");
        new Achievement(BiomiaAchievement.OeffneZehnTruhen, BiomiaStat.ChestsOpened, 10, "Truhenöffner")
                .setDescription("Öffne zehn Truhen.");
        new Achievement(BiomiaAchievement.Nimm20Fallschaden, BiomiaStat.HealthLost, 20, "Aua Aua", "FALL")
                .setDescription("Nimm 20 Fallschaden.");
        //Idee: Neben das Inventar geklickt-Achievement
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

