package de.biomia.bungee.events;

public enum Time {

    Jahre(365 * 24 * 3600), Monate(30 * 24 * 3600), Tage(24 * 3600), Stunden(3600), Minuten(60), Sekunden(1);

    private final int sekunden;

    Time(int sekunden) {
        this.sekunden = sekunden;
    }

    private int getSekunden() {
        return sekunden;
    }

    public static String toText(int i) {

        StringBuilder output = new StringBuilder();

        for (Time t : values()) {

            int vielfaches = i / t.getSekunden();

            if (vielfaches == 1) {
                output.append("§51 §2").append(t.name().substring(0, t.name().length() - 1)).append("§7, §r");

                i -= t.getSekunden();

                if (i == 0)
                    output = new StringBuilder(output.substring(0, output.length() - 4));

            } else if (vielfaches > 1) {
                output.append("§5").append(vielfaches).append(" §2").append(t.name()).append("§7, §r");

                i -= vielfaches * t.getSekunden();

                if (i == 0)
                    output = new StringBuilder(output.substring(0, output.length() - 6));
            }
        }
        return output.toString();
    }
}
