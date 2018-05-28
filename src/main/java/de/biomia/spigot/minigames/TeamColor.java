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

    public String getColorcode() {

        switch (this) {
            case BLACK:
                return "§0";
            case BLUE:
                return "§9";
            case ORANGE:
                return "§6";
            case GREEN:
                return "§2";
            case PURPLE:
                return "§b";
            case RED:
                return "§c";
            default:
            case WHITE:
                return "§f";
            case YELLOW:
                return "§e";
        }
    }

    public short getData() {
        switch (this) {
            case BLACK:
                return 15;
            case BLUE:
                return 11;
            case ORANGE:
                return 1;
            case GREEN:
                return 13;
            case PURPLE:
                return 10;
            case RED:
                return 14;
            default:
            case WHITE:
                return 0;
            case YELLOW:
                return 4;
        }
    }

    public String translate() {
        switch (this) {
            case BLACK:
                return "Schwarz";
            case BLUE:
                return "Blau";
            case ORANGE:
                return "Orange";
            case GREEN:
                return "Grün";
            case PURPLE:
                return "Lila";
            case RED:
                return "Rot";
            default:
            case WHITE:
                return "Weiß";
            case YELLOW:
                return "Gelb";
        }
    }

    public static TeamColor getColorFromData(short data) {
        for (TeamColor color : values()) {
            if (color.getData() == data) {
                return color;
            }
        }
        return BLACK;
    }
}
