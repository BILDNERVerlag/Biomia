package de.biomia.spigot.minigames;

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

    public int getReward(GameInstance instance) {

        int tempCoins = instance.getType().isVersus() ? versusCoins : coins;

        switch (this) {
            case PLAYED:
                tempCoins *= (double) instance.getGameMode().getPlayedTime() / 60D;
                break;
            case WIN:
                tempCoins *= instance.getPlayersOnStart();
                break;
        }
         return tempCoins;
    }
}

