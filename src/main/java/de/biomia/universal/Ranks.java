package de.biomia.universal;

public enum Ranks {

    UnregSpieler(23), RegSpieler(22), TestAccount(21),
    PremiumEins(20), PremiumZwei(19), PremiumDrei(18), PremiumVier(17), PremiumFuenf(16), PremiumSechs(15), PremiumSieben(14), PremiumAcht(13), PremiumNeun(12), PremiumZehn(11),
    YouTube(10),
    JrBuilder(9), Builder(8), PixelBiest(7), SrBuilder(6),
    Supporter(5), Moderator(4), SrModerator(3),
    Developer(2),
    Admin(1), Owner(0);

    private int level;

    Ranks(int id) {
        this.level = id;
    }

    public String getPrefix() {
        switch (this) {
            case Admin:
                return "\u00A75Admin | ";
            case Owner:
                return "\u00A7cOwner | ";
            case Developer:
                return "\u00A7dDev | ";
            case Builder:
                return "\u00A72Builder | ";
            case YouTube:
                return "\u00A74[\u00A70Y\u00A7fT\u00A74] | ";
            case JrBuilder:
                return "\u00A7aJrBuilder | ";
            case Moderator:
                return "\u00A73Mod | ";
            case SrBuilder:
                return "\u00A72SrBuilder | ";
            case Supporter:
                return "\u00A7bSup | ";
            case RegSpieler:
                return "\u00A77";
            case PremiumAcht:
                return "\u00A7eVIII | ";
            case PremiumDrei:
                return "\u00A7eIII | ";
            case PremiumEins:
                return "\u00A7eI | ";
            case PremiumFuenf:
                return "\u00A7eV | ";
            case PremiumNeun:
                return "\u00A76IX | ";
            case PremiumVier:
                return "\u00A7eIV | ";
            case PremiumZehn:
                return "\u00A76X | ";
            case PremiumZwei:
                return "\u00A7eII | ";
            case SrModerator:
                return "\u00A79SrMod | ";
            case PremiumSechs:
                return "\u00A7eVI | ";
            case UnregSpieler:
                return "\u00A78";
            case PremiumSieben:
                return "\u00A7eVII | ";
            case PixelBiest:
                return "\u00A76PB | ";
            case TestAccount:
                return "T\u00A7kX\u00A7rst | ";
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
