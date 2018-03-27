package de.biomia.bungee.var;

public class Bans {

    private int biomiaID;
    private String grund;
    private boolean perm;
    private int bis;
    private final int von;
    private final int timestamp;

    // constructor
    public Bans(boolean perm, int bis, String grund, int biomiaID, int von, int timestamp) {
        this.biomiaID = biomiaID;
        this.grund = grund;
        this.bis = bis;
        this.perm = perm;
        this.von = von;
        this.timestamp = timestamp;
    }

    public int getBiomiaID() {
        return biomiaID;
    }

    public String getGrund() {
        return grund;
    }

    public boolean isPerm() {
        return perm;
    }

    public int getBis() {
        return bis;
    }

    public int getVon() {
        return von;
    }

    public int getTimestamp() {
        return timestamp;
    }
}
