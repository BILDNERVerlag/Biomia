package de.biomia.universal;

public class Messages {
    public static final String COLOR_MAIN = "\u00A7c";
    public static final String COLOR_SUB = "\u00A7b";
    public static final String COLOR_AUX = "\u00A77";

    public static final String PREFIX = String.format("%s[%sBIO%sMIA%s] ", COLOR_AUX, COLOR_MAIN, COLOR_SUB, COLOR_AUX);
    public static final String NO_PERM = PREFIX + String.format("%sDu hast keine Rechte um diesen Befehl auszuführen!", COLOR_MAIN);
    public static final String NOT_ONLINE = PREFIX + String.format("%sDer Spieler ist nicht Online!", COLOR_MAIN);
    public static final String NO_PLAYER = PREFIX + String.format("%sDu musst ein Spieler sein!", COLOR_MAIN);
}
