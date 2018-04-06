package de.biomia.universal;

import de.biomia.spigot.messages.SpecialChars;

public class Messages {
    public static final String COLOR_MAIN = "§c";
    public static final String COLOR_SUB = "§b";
    public static final String COLOR_AUX = "§7";

    public static final String PREFIX = String.format("%s[%sBIO%sMIA%s] ", COLOR_AUX, COLOR_MAIN, COLOR_SUB, COLOR_AUX);
    public static final String NO_PERM = PREFIX + String.format("%sDu hast keine Rechte um diesen Befehl auszuf%shren!", COLOR_MAIN, SpecialChars.UE);
    public static final String NOT_ONLINE = PREFIX + String.format("%sDer Spieler ist nicht Online!",COLOR_MAIN);
    public static final String NO_PLAYER = PREFIX + String.format("%sDu musst ein Spieler sein!", COLOR_MAIN);
}
