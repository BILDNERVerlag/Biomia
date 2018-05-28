package de.biomia.universal;

public enum Ranks {

    UnregSpieler(23), RegSpieler(22), TestAccount(21),
    PremiumEins(20), PremiumZwei(19), PremiumDrei(18), PremiumVier(17), PremiumFuenf(16), PremiumSechs(15), PremiumSieben(14), PremiumAcht(13), PremiumNeun(12), PremiumZehn(11),
    YouTube(10),
    JrBuilder(9), Builder(8), PixelBiest(7), SrBuilder(6),
    Supporter(5), Moderator(4), SrModerator(3),
    Developer(2),
    Admin(1), Owner(0);

    private final int level;

    Ranks(int id) {
        this.level = id;
    }

    public String getPrefix() {
        switch (this) {
            case Admin:
                return "§5Admin | ";
            case Owner:
                return "§cOwner | ";
            case Developer:
                return "§dDev | ";
            case Builder:
                return "§2Builder | ";
            case YouTube:
                return "§4[§0Y§fT§4] | ";
            case JrBuilder:
                return "§aJrBuilder | ";
            case Moderator:
                return "§3Mod | ";
            case SrBuilder:
                return "§2SrBuilder | ";
            case Supporter:
                return "§bSup | ";
            case RegSpieler:
                return "§7";
            case PremiumAcht:
                return "§eVIII | ";
            case PremiumDrei:
                return "§eIII | ";
            case PremiumEins:
                return "§eI | ";
            case PremiumFuenf:
                return "§eV | ";
            case PremiumNeun:
                return "§6IX | ";
            case PremiumVier:
                return "§eIV | ";
            case PremiumZehn:
                return "§6X | ";
            case PremiumZwei:
                return "§eII | ";
            case SrModerator:
                return "§9SrMod | ";
            case PremiumSechs:
                return "§eVI | ";
            case UnregSpieler:
                return "§8";
            case PremiumSieben:
                return "§eVII | ";
            case PixelBiest:
                return "§6PB | ";
            case TestAccount:
                return "T§kX§rst | ";
        }
        return "NoPrefix";
    }

    public boolean isPremium() {
        return getName().contains("premium");
    }

    public String getName() {
        return this.name().toLowerCase();
    }

    public int getLevel() {
        return level;
    }
}
