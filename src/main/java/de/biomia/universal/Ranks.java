package de.biomia.universal;

public enum Ranks {

    UnregSpieler(0), RegSpieler(1),
    PremiumEins(2), PremiumZwei(3), PremiumDrei(4), PremiumVier(5), PremiumFünf(6), PremiumSechs(7), PremiumSieben(8), PremiumAcht(9), PremiumNeun(10), PremiumZehn(0),
    YouTube(11),
    JrBuilder(12), Builder(13), PixelBiest(14), SrBuilder(15),
    Supporter(16), Moderator(17), SrModerator(18),
    Developer(19),
    Admin(20), Owner(21);

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
            case PremiumFünf:
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
