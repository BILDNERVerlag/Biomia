package de.biomia.spigot.minigames;

import de.biomia.spigot.OfflineBiomiaPlayer;

public enum GameRewards {

    KILL(50), WIN(30, 15), PLAYED(10);

    private final int coins;
    private final int versusCoins;

    GameRewards(int coins, int versusCoins) {
        this.coins = coins;
        this.versusCoins = versusCoins;
    }

    GameRewards(int coins) {
        this.versusCoins = this.coins = coins;
    }

    public void giveReward(OfflineBiomiaPlayer bp, GameInstance instance) {

        int tempCoins = instance.getType().isVersus() ? versusCoins : coins;

        switch (this) {
            case PLAYED:
                tempCoins *= instance.getGameMode().getPlayedTime() / 60;
                break;
            case WIN:
                tempCoins *= instance.getPlayersOnStart();
                break;
            default:
                break;
        }

        bp.addCoins(tempCoins, true);
    }
}

