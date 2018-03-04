package de.biomia.spigot.server.minigames.versus.vs.game;

public enum TeamColor {

    BLUE(1), RED(2);

    private final int i;

    TeamColor(int id) {
        this.i = id;
    }

    public int getID() {
        return i;
    }

    public String getGermanName() {
        switch (this) {
            case RED:
                return "Rot";
            case BLUE:
                return "Blau";
            default:
                return null;
        }
    }

    public String getColorcode() {
        switch (this) {
            case RED:
                return "\u00A7c";
            case BLUE:
                return "\u00A79";
            default:
                return null;
        }
    }

    public short getData() {
        switch (this) {
            case RED:
                return 14;
            case BLUE:
                return 11;
            default:
                return 0;
        }
    }
}
