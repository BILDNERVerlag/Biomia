package de.biomiaAPI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.biomiaAPI.Quests.QuestPlayer;
import de.biomiaAPI.coins.Coins;
import de.biomiaAPI.main.Main;
import de.biomiaAPI.pex.Rank;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;

//only to not use "deprecation"
@SuppressWarnings("deprecation")
public class BiomiaPlayer{

	private Player p = null;
	private boolean build = false;
	private boolean trollmode = false;
	private boolean getDamage = true;
	private boolean damageEntitys = true;
	private int coins = -1;
	private de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer spigotPafpl;

	public BiomiaPlayer(Player p) {

		spigotPafpl = PAFPlayerManager.getInstance().getPlayer(p.getUniqueId());

		setPlayer(p);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (p.isOnline())
					coins = Coins.getCoins(p);
			}
		}.runTaskTimer(Main.plugin, 600, 600);
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
		if (coins == -1)
			coins = Coins.getCoins(p);
		return coins;
	}

	public boolean takeCoins(int coins) {
		boolean b = Coins.takeCoins(coins, this);
		if (b)
			this.coins = this.coins - coins;
		return b;
	}

	public boolean addCoins(int coins) {
		boolean b = Coins.addCoins(coins, this);
		if (b) {
			this.coins = this.coins + coins;
			this.getPlayer().sendMessage("ß7Du erh‰ltst ßf" + coins + "ß7 BC's!");
		}
		return b;

	}

	public void setCoins(int coins) {
		Coins.setCoins(coins, this);
		this.coins = coins;
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
		String rank = Rank.getRank(p);

		if (rank.contains("Premium"))
			return true;
		else if (rank.contains("YouTube"))
			return true;
		else if (rank.contains("Moderator"))
			return true;
		else if (rank.contains("Builder"))
			return true;
		else if (rank.contains("Admin"))
			return true;
		else if (rank.contains("Owner"))
			return true;
		else
			return false;

	}

	//FIXME: sollte "isStaff" heiﬂen
	public boolean isStuff() {
		String rank = Rank.getRank(p);

		if (rank.contains("Moderator"))
			return true;
		else if (rank.contains("Builder"))
			return true;
		else if (rank.contains("Admin"))
			return true;
		else if (rank.contains("Owner"))
			return true;
		else
			return false;
	}

	//FIXME: sollte "isYoutuber" heiﬂen
	public boolean isYouTouber() {

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
}
