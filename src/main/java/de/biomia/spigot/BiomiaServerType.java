package de.biomia.spigot;

public enum BiomiaServerType {

    TestLobby, TestQuest, TestBedWars, TestSkyWars, TestFreebuild, TestServer,
    Lobby, Quest, BedWars, SkyWars, Duell, Weltenlabor_1, FreebuildFarm, Freebuild, BauServer, Parrot,

    Event_Schnitzeljagd;

    public boolean isMinigame() {
        switch (this) {
            case BedWars:
            case SkyWars:
            case Duell:
            case Parrot:
                return true;
            default:
                return false;
        }
    }
}
