package de.biomia.bungee.events;

public enum Grund {
    FLYHACK, NOSLOWDOWN, KILLAURA, SPEEDHACK, SONSTIGER, SPAMMING, TROLLING, GRIEFING, BELEIDIGUNG, ANDERER;

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
        case SONSTIGER:
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
        case ANDERER:
            s = "einem nicht gelisteten Grund";
            break;
        }
        return s;
    }
}
