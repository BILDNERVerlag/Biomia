package de.biomia.spigot;

public enum BiomiaServerType {

    TestLobby, TestQuest, TestBedWars, TestSkyWars, TestDuell, TestFreebuild,
    Lobby, Quest, BedWars, SkyWars, Duell, Weltenlabor_1, FreebuildFarm, Freebuild, BauServer;

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
