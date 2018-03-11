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
            return "\u00A70";
        case BLUE:
            return "\u00A79";
        case ORANGE:
            return "\u00A76";
        case GREEN:
            return "\u00A72";
        case PURPLE:
            return "\u00A7d";
        case RED:
            return "\u00A7c";
        default:
        case WHITE:
            return "\u00A7f";
        case YELLOW:
            return "\u00A7e";
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
            return "Gr\u00fcn";
        case PURPLE:
            return "Lila";
        case RED:
            return "Rot";
        default:
        case WHITE:
            return "Wei\u00df";
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
