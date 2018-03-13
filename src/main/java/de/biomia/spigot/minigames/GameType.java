package de.biomia.spigot.minigames;

public enum GameType {
    BED_WARS_VS, SKY_WARS_VS, KIT_PVP_VS, BED_WARS, SKY_WARS;

    public String getDisplayName() {
        switch (this) {
        case BED_WARS:
        case BED_WARS_VS:
            return "BedWars";
        case SKY_WARS:
        case SKY_WARS_VS:
            return "SkyWars";
        case KIT_PVP_VS:
            return "KitPVP";
        default:
            return "";
        }
    }

    public boolean isVersus() {
        switch (this) {
        case BED_WARS_VS:
        case SKY_WARS_VS:
            return true;
        default:
            return false;
        }
    }
}
