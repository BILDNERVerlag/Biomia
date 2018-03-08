package de.biomia.spigot.minigames;

public enum TeamColor {

    BLUE(1), RED(2), YELLOW(3), GREEN(4), ORANGE(5), PURPLE(6), WHITE(7), BLACK(8);

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
