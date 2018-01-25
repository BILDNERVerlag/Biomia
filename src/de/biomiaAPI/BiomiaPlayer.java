package de.biomiaAPI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;
import org.bukkit.entity.Player;

import de.biomiaAPI.Quests.QuestPlayer;
import de.biomiaAPI.coins.Coins;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.mysql.MySQL;
import de.biomiaAPI.pex.Rank;

@SuppressWarnings("deprecation")
public class BiomiaPlayer {

	private Player p = null;
	private boolean build = false;
	private boolean trollmode = false;
	private boolean getDamage = true;
	private boolean damageEntitys = true;
	private final PAFPlayer spigotPafpl;
	private final int biomiaPlayerID;

	public BiomiaPlayer(Player p) {
		biomiaPlayerID = getBiomiaPlayerID(p);
		setPlayer(p);
		spigotPafpl = PAFPlayerManager.getInstance().getPlayer(p.getUniqueId());
	}

	private int getBiomiaPlayerID(Player p) {
		return MySQL.executeQuerygetint("Select id from BiomiaPlayer where uuid = '" + p.getUniqueId().toString() + "'",
				"id");
	}

	public Player getPlayer() {
		return p;
	}

	private void setPlayer(Player p) {
		this.p = p;
	}

	public QuestPlayer getQuestPlayer() {
		return Biomia.getQuestPlayer(p);
	}

	public boolean canBuild() {
		return build;
	}

	public void setBuild(boolean build) {
		this.build = build;
	}

	public boolean canGetDamage() {
		return getDamage;
	}

	public void setGetDamage(boolean getDamage) {
		this.getDamage = getDamage;
	}

	public boolean canDamageEntitys() {
		return damageEntitys;
	}

	public void setDamageEntitys(boolean damageEntitys) {
		this.damageEntitys = damageEntitys;
	}

	public int getCoins() {
		return Coins.getCoins(this);
	}

	public void takeCoins(int coins) {
		Coins.takeCoins(coins, this);
	}

	@Deprecated
	public boolean addCoins(int coins) {
		boolean b = Coins.addCoins(coins, this);
		if (b) {
			this.getPlayer().sendMessage("\u00A77Du erh\u00e4ltst \u00A7f" + coins + "\u00A77 BC!");
		}
		return b;
	}

	private void stopCoinBoost() {
		MySQL.executeUpdate("DELETE FROM `CoinBoost` WHERE BiomiaPlayer = " + biomiaPlayerID);
	}

	public void giveBoost(int percent, int timeinseconds) {

		stopCoinBoost();
		MySQL.executeUpdate("INSERT INTO `CoinBoost`(`BiomiaPlayer`, `percent`, `until`) VALUES (" + biomiaPlayerID
				+ "," + percent + "," + System.currentTimeMillis() / 1000 + timeinseconds + ")");

	}

	public void addCoins(int coins, boolean enableBoost) {

		Connection con = MySQL.Connect();
		int prozent = 100;
		try {
			assert con != null;
			PreparedStatement ps = con
					.prepareStatement("SELECT `percent`, `until` FROM `CoinBoost` WHERE BiomiaPlayer = ?");
			ps.setInt(1, getBiomiaPlayerID());
			ResultSet rs = ps.executeQuery();
            //noinspection LoopStatementThatDoesntLoop
            while (rs.next()) {
				long until = rs.getLong("until");
				if (System.currentTimeMillis() / 1000 > until) {
					prozent = rs.getInt("percent");
				} else {
					stopCoinBoost();
				}
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		if (prozent != 100) {
            double coinsDouble = (double) coins / 100 * prozent;
            coins = (int) coinsDouble;
        }

		boolean b = Coins.addCoins(coins, this);
		if (b) {
			this.getPlayer().sendMessage("\u00A77Du erh\u00e4ltst \u00A7f" + coins + "\u00A77 BC!");
		}

	}

	public void setCoins(int coins) {
		Coins.setCoins(coins, this);
	}

	public List<PAFPlayer> getFriends() {
		return spigotPafpl.getFriends();
	}

	public List<PAFPlayer> getOnlineFriends() {
		List<PAFPlayer> onlineFriends = new ArrayList<>();
		for (PAFPlayer pafplayer : getFriends()) {
			if (!Main.allPlayersOnAllServer.isEmpty()) {
				if (Main.allPlayersOnAllServer.contains(pafplayer.getName()))
					onlineFriends.add(pafplayer);
			}
		}
		return onlineFriends;
	}

	public boolean isPremium() {
		return Rank.isPremium(p);

	}

	public boolean isStaff() {
		String rank = Rank.getRank(p);

		return rank.contains("Spieler") || rank.contains("Premium") || rank.contains("YouTuber");
	}

	public boolean isYouTuber() {

		String rank = Rank.getRank(p);

		return rank.contains("YouTube");

	}

	public int getPremiumLevel() {
		return Rank.getPremiumLevel(p);
	}

	public PlayerParty getParty() {
		return PartyManager.getInstance().getParty(spigotPafpl);
	}

	public boolean isPartyLeader() {
		return isInParty() && spigotPafpl.equals(getParty().getLeader());
	}

	public boolean isInParty() {
		return getParty() != null;
	}

	public boolean isInTrollmode() {
		return trollmode;
	}

	public void setTrollmode(boolean trollmode) {
		this.trollmode = trollmode;
	}

	public int getBiomiaPlayerID() {
		return biomiaPlayerID;
	}
}
