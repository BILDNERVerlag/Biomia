package de.biomia.spigot;

public enum BiomiaServerType {

    TestLobby, TestQuest, TestBedWars, TestSkyWars, TestFreebuild, TestServer,
    Lobby, Quest, BedWars, SkyWars, Duell, Weltenlabor_1, FreebuildFarm, Freebuild, BauServer,

    Event_Schnitzeljagd;

    public boolean isMinigame() {
        switch (this) {
            case BedWars:
            case SkyWars:
            case Duell:
                return true;
            default:
                return false;
        }
    }
}
