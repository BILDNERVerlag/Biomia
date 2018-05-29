package de.biomia.universal;

public class Messages {
    public static final String COLOR_MAIN = "§c";
    public static final String COLOR_SUB = "§b";
    public static final String COLOR_AUX = "§7";
    private static final String NOFORMAT = "§r";

    public static final String PREFIX = String.format("%s[%sBIO%sMIA%s] ", COLOR_AUX, COLOR_MAIN, COLOR_SUB, COLOR_AUX);
    public static final String BIOMIA = String.format("%sBIO%sMIA%s", COLOR_MAIN, COLOR_SUB, NOFORMAT);
    public static final String NO_PERM = PREFIX + String.format("%sDu hast keine Rechte um diesen Befehl auszuführen!", COLOR_MAIN);
    public static final String NOT_ONLINE = PREFIX + String.format("%sDer Spieler ist nicht Online!", COLOR_MAIN);
    public static final String NO_PLAYER = PREFIX + String.format("%sDu musst ein Spieler sein!", COLOR_MAIN);

    /**
     * @param message placeholder with %s
     */
    public static String format(String message, Object... placeholder) {
        message = COLOR_MAIN + message.replaceAll("[^§\\w\\s]|_", COLOR_AUX + "$1" + COLOR_MAIN);
        for (Object o : placeholder) {
            message = message.replaceFirst("%s", COLOR_SUB + o + COLOR_MAIN);
        }
        return message;
    }
}
