package de.biomia.spigot.minigames;

public enum GameType {
    BED_WARS_VS, SKY_WARS_VS, KIT_PVP_VS;

    public String getDisplayName() {
        switch (this) {
            case BED_WARS_VS:
                return "BedWars";
            case SKY_WARS_VS:
                return "SkyWars";
            case KIT_PVP_VS:
                return "KitPVP";
            default:
                return "";
        }
    }
    }
