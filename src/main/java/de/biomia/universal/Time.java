package de.biomia.universal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public enum Time {

    Jahre(365 * 24 * 3600), Monate(30 * 24 * 3600), Tage(24 * 3600), Stunden(3600), Minuten(60), Sekunden(1);

    private final int sekunden;

    Time(int sekunden) {
        this.sekunden = sekunden;
    }

    public int getSekunden() {
        return sekunden;
    }

    private String getEinzahl() {
        return name().substring(0, name().length() - 1);
    }

    public static String toText(int i) {

        StringBuilder output = new StringBuilder();

        boolean firstRun = true;

        for (Time t : values()) {
            int vielfaches = i / t.getSekunden();
            if (vielfaches == 0)
                continue;
            if (!firstRun)
                output.append("§7, ");
            output.append(String.format("§c%s §b%s", vielfaches, vielfaches == 1 ? t.getEinzahl() : t.name()));
            i -= vielfaches * t.getSekunden();
            firstRun = false;
        }
        return output.toString();
    }

    public static String toFromatString(String format, int seconds) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(seconds * 1000));
    }

}
