package de.biomia.spigot.minigames;

import de.biomia.spigot.BiomiaPlayer;

public enum GameRewards {

    KILL(50), WIN(30, 15), PLAYED(10);

    private int coins;
    private int versusCoins;

    GameRewards(int coins, int versusCoins) {
        this.coins = coins;
        this.versusCoins = versusCoins;
    }

    GameRewards(int coins) {
        this.versusCoins = this.coins = coins;
    }

    public void giveReward(BiomiaPlayer bp, GameInstance instance) {

        int tempCoins = instance.getType().isVersus() ? versusCoins : coins;

        switch (this) {
        case PLAYED:
            tempCoins *= instance.getPlayedTime() / 60;
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