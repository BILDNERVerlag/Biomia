package de.biomiaAPI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import de.biomiaAPI.Quests.QuestPlayer;
import de.biomiaAPI.coins.Coins;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.mysql.MySQL;
import de.biomiaAPI.pex.Rank;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;

@SuppressWarnings("deprecation")
public class BiomiaPlayer {

	private Player p = null;
	private boolean build = false;
	private boolean trollmode = false;
	private boolean getDamage = true;
	private boolean damageEntitys = true;
	private PAFPlayer spigotPafpl;
	private int biomiaPlayerID = -1;

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

	public void setPlayer(Player p) {
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

	public boolean takeCoins(int coins) {
		boolean b = Coins.takeCoins(coins, this);
		return b;
	}

	@Deprecated
	public boolean addCoins(int coins) {
		boolean b = Coins.addCoins(coins, this);
		if (b) {
			this.getPlayer().sendMessage("§7Du erhältst §f" + coins + "§7 BC's!");
		}
		return b;
	}

	public void stopCoinBoost() {
		MySQL.executeUpdate("DELETE FROM `CoinBoost` WHERE BiomiaPlayer = " + biomiaPlayerID);
	}

	public void giveBoost(int percent, int timeinseconds) {

		stopCoinBoost();
		MySQL.executeUpdate("INSERT INTO `CoinBoost`(`BiomiaPlayer`, `percent`, `until`) VALUES (" + biomiaPlayerID
				+ "," + percent + "," + System.currentTimeMillis() / 1000 + timeinseconds + ")");

	}

	public boolean addCoins(int coins, boolean enableBoost) {

		Connection con = MySQL.Connect();
		int prozent = 100;
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT `percent`, `until` FROM `CoinBoost` WHERE BiomiaPlayer = ?");
			ps.setInt(1, getBiomiaPlayerID());
			ResultSet rs = ps.executeQuery();
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
			return false;
		}

		coins = coins / 100 * prozent;

		boolean b = Coins.addCoins(coins, this);
		if (b) {
			this.getPlayer().sendMessage("§7Du erhältst §f" + coins + "§7 BC's!");
		}
		return b;

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
		if (Rank.getRank(p).contains("Premium"))
			return true;
		return false;

	}

	public boolean isStaff() {
		String rank = Rank.getRank(p);

		if (rank.contains("Spieler") || rank.contains("Premium") || rank.contains("YouTuber"))
			return false;
		else
			return true;
	}

	public boolean isYouTuber() {

		String rank = Rank.getRank(p);

		if (rank.contains("YouTube"))
			return true;

		return false;

	}

	public int getPremiumLevel() {
		String rank = Rank.getRank(p);

		rank = rank.replaceAll("Premium", "");

		switch (rank) {
		case "Eins":
			return 1;
		case "Zwei":
			return 2;
		case "Drei":
			return 3;
		case "Vier":
			return 4;
		case "Fuenf":
			return 5;
		case "Sechs":
			return 6;
		case "Sieben":
			return 7;
		case "Acht":
			return 8;
		case "Neun":
			return 9;
		case "Zehn":
			return 10;
		default:
			return -1;
		}

	}

	public PlayerParty getParty() {
		return PartyManager.getInstance().getParty(spigotPafpl);
	}

	public boolean isPartyLeader() {
		if (isInParty()) {
			if (spigotPafpl.equals(getParty().getLeader())) {
				return true;
			}
		}
		return false;
	}

	public boolean isInParty() {
		if (getParty() != null)
			return true;
		return false;
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
