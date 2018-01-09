package de.biomiaAPI.Quests;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;
import de.biomiaAPI.QuestEvents.Event;
import de.biomiaAPI.main.Main;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class DialogMessage {

	final int delay = 20;

	private String inhalt;
	private String[] fortsetzungen = new String[5];
	private DialogMessage[] nexterAbschnitt = new DialogMessage[5];
	private ArrayList<Event> events = new ArrayList<>();
	private Quest q;
	private boolean addPlayerToQuest = false;
	private boolean removePlayerFromQuest = false;
	private boolean setfinish = false;
	private States state;
	private NPC npc;

	public DialogMessage(Quest q, NPC npc) {
		this.npc = npc;
		this.q = q;
	}

	public DialogMessage setInhalt(String npcAuskunft) {
		inhalt = npcAuskunft;
		return this;
	}

	public void execute(QuestPlayer qp) {
		if (qp.getDialog() != null) {
			sendeAntwort(qp);
		}
	}

	public DialogMessage setFortsetzung(String anwortMöglichkeit) {
		for (int i = 0; i < fortsetzungen.length; i++) {
			if (fortsetzungen[i] == null) {
				fortsetzungen[i] = anwortMöglichkeit;
				break;
			}
			if (i == fortsetzungen.length) {
				Bukkit.broadcastMessage("Maximal 5 Antwortm�glichkeiten!");
			}
		}
		return this;
	}

	public DialogMessage setNext(String s, int i, NPC npc) {
		nexterAbschnitt[i] = new DialogMessage(q, npc).setInhalt(s);
		return nexterAbschnitt[i];
	}

	public DialogMessage setNext(DialogMessage dm0, int i) {
		nexterAbschnitt[i] = dm0;
		return nexterAbschnitt[i];
	}

	public void sendeAntwort(QuestPlayer qp) {

		ArrayList<QuestPlayer> players = new ArrayList<>();
		BiomiaPlayer bp = Biomia.getBiomiaPlayer(qp.getPlayer());

		if (bp.isInParty() && bp.isPartyLeader()) {
			bp.getParty().getAllPlayers().forEach(each -> {
				players.add(qp);
				return;
			});
		} else {
			players.add(qp);
		}

		if (qp.getPlayer().getWalkSpeed() != 0)
			qp.getPlayer().setWalkSpeed(0);

		players.forEach(each -> {
			each.getPlayer().sendMessage("�7" + npc.getName() + ": �6" + inhalt);
			executeEvent(each);
		});
		if (fortsetzungen[0] != null) {
			for (int i = 0; i < fortsetzungen.length; i++) {
				if (fortsetzungen[i] != null) {
					TextComponent msg = new TextComponent("�5" + (i + 1) + ". �2" + fortsetzungen[i]);
					if (qp.getDialog() != null && !qp.getDialog().isLast()) {
						ClickEvent clickEvent = new ClickEvent(Action.RUN_COMMAND, "/q " + Main.QuestIds);
						Main.questMessages.put(Main.QuestIds, qp.getDialog().getNext(i));
						Main.QuestIds++;
						msg.setClickEvent(clickEvent);
						qp.getPlayer().spigot().sendMessage(msg);
					} else {
						qp.getPlayer().setWalkSpeed(0.2F);
					}
				} else {
					return;
				}
			}
		}
		if (isLast()) {
			qp.setDialog(null);
			qp.getPlayer().setWalkSpeed(0.2F);
			return;
		}
		if (getFortsetzung(0) == null) {
			new BukkitRunnable() {
				@Override
				public void run() {
					qp.setDialog(qp.getDialog().getNext(0));
					qp.getDialog().execute(qp);
				}
			}.runTaskLater(Main.plugin, delay);
		}
	}

	public String getFortsetzung(int slot) {
		return fortsetzungen[slot];
	}

	public DialogMessage getNext(int slot) {
		return nexterAbschnitt[slot];
	}

	public String getInhalt() {
		return inhalt;
	}

	public void executeEvent(QuestPlayer qp) {
		for (Event e : events) {
			e.executeEvent(qp);
		}
		if (addPlayerToQuest)
			qp.addToQuest(q);
		if (state != null)
			qp.updateState(q, state);
		if (removePlayerFromQuest)
			qp.rmFromQuest(q);
		if (setfinish)
			qp.finish(q);
	}

	public void updatePlayerState(States state) {
		this.state = state;
	}

	public void addPlayerToQuest() {
		addPlayerToQuest = true;
	}

	public void removePlayerFromQuest() {
		removePlayerFromQuest = true;
	}

	public void finish() {
		setfinish = true;
	}

	public DialogMessage addEvent(Event e) {
		events.add(e);
		return this;
	}

	public boolean isLast() {
		if (this.getNext(0) == null && this.getNext(1) == null && this.getNext(2) == null && this.getNext(3) == null
				&& this.getNext(4) == null) {
			return true;
		}
		return false;
	}
}
