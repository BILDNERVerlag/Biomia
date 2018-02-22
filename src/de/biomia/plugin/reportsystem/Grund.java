package de.biomia.plugin.reportsystem;

public enum Grund {
    FLYHACK, NOSLOWDOWN, KILLAURA, SPEEDHACK, SONSTIGER_HACK, SPAMMING, TROLLING, GRIEFING, BELEIDIGUNG, ANDERER_GRUND;

    public static String toText(Grund g) {
        String s = null;
        switch (g) {
            case FLYHACK:
                s = "FlyHack";
                break;
            case NOSLOWDOWN:
                s = "NoSlowdown";
                break;
            case KILLAURA:
                s = "KillAura";
                break;
            case SPEEDHACK:
                s = "Speedhack";
                break;
            case SONSTIGER_HACK:
                s = "einem nicht gelisteten Hack/Cheat";
                break;
            case SPAMMING:
                s = "Spamming";
                break;
            case TROLLING:
                s = "Trolling";
                break;
            case GRIEFING:
                s = "Griefing";
                break;
            case BELEIDIGUNG:
                s = "Beleidigung";
                break;
            case ANDERER_GRUND:
                s = "einem nicht gelisteten Grund";
                break;
        }
        return s;
    }
}
