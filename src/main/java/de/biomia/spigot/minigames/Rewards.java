package de.biomia.spigot.minigames;

import de.biomia.spigot.BiomiaPlayer;

public enum Rewards {

    KILL(50), WIN(300), PLAYED(10);

    int coins;

    Rewards(int i) {
        this.coins = i;
    }

    public int getCoins() {
        return coins;
    }

    public void giveReward(BiomiaPlayer bp, GameInstance instance) {

        if (this.equals(PLAYED))
            coins *= instance.getPlayedTime() / 60;

        bp.addCoins(coins, true);
    }

}
