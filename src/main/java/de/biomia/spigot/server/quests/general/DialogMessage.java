package de.biomia.spigot.server.quests.general;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.Main;
import de.biomia.spigot.server.quests.QuestEvents.Event;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogMessage {

    public static final HashMap<Integer, DialogMessage> questMessages = new HashMap<>();
    private static int lastQuestID = 0;
    private final String[] fortsetzungen = new String[5];
    private final DialogMessage[] nexterAbschnitt = new DialogMessage[5];
    private final ArrayList<Event> events = new ArrayList<>();
    private final Quest q;
    private final NPC npc;
    private String inhalt;
    private boolean addPlayerToQuest = false;
    private boolean removePlayerFromQuest = false;
    private boolean setfinish = false;
    private States state;

    public DialogMessage(Quest q, NPC npc) {
        this.npc = npc;
        this.q = q;
    }

    public DialogMessage setInhalt(String dialog) {
        inhalt = dialog;
        return this;
    }

    public void execute(QuestPlayer qp) {
        if (qp.getDialog() != null) {
            sendeAntwort(qp);
        }
    }

    public DialogMessage setNext(String s, int i, NPC npc) {
        return nexterAbschnitt[i] = new DialogMessage(q, npc).setInhalt(s);
    }

    private void sendeAntwort(QuestPlayer qp) {

        BiomiaPlayer bp = Biomia.getBiomiaPlayer(qp.getPlayer());
        ArrayList<QuestPlayer> players = new ArrayList<>();

        if (bp.isPartyLeader()) {
            bp.getParty().getAllPlayers().forEach(each -> {
                Player p = Bukkit.getPlayer(each.getUniqueId());
                if (p != null && !qp.getPlayer().equals(p))
                    players.add(Biomia.getQuestPlayer(p));
            });
        }
        players.add(qp);

        if (qp.getPlayer().getWalkSpeed() != 0 && !isLast())
            qp.getPlayer().setWalkSpeed(0);

        players.forEach(each -> {
            each.getPlayer().sendMessage("\u00A7c" + npc.getName() + "\u00A77: \u00A7b" + inhalt);
            executeEvent(each);
        });
        if (fortsetzungen[0] != null) {
            for (int i = 0; i < fortsetzungen.length; i++) {
                if (fortsetzungen[i] != null) {
                    TextComponent msg = new TextComponent("\u00A77" + (i + 1) + ". \u00A7b" + fortsetzungen[i]);
                    if (qp.getDialog() != null && !qp.getDialog().isLast()) {
                        lastQuestID++;
                        ClickEvent clickEvent = new ClickEvent(Action.RUN_COMMAND, "/q " + lastQuestID);
                        questMessages.put(lastQuestID, qp.getDialog().getNext(i));
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
        if (getFortsetzung() == null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    qp.setDialog(qp.getDialog().getNext(0));
                    if (qp.getDialog() != null)
                        qp.getDialog().execute(qp);
                }
            }.runTaskLater(Main.getPlugin(), 20);
        }
    }

    private String getFortsetzung() {
        return fortsetzungen[0];
    }

    public DialogMessage setFortsetzung(String anwortMoeglichkeit) {
        for (int i = 0; i < fortsetzungen.length; i++) {
            if (fortsetzungen[i] == null) {
                fortsetzungen[i] = anwortMoeglichkeit;
                break;
            }
        }
        return this;
    }

    public DialogMessage getNext(int slot) {
        return nexterAbschnitt[slot];
    }

    private void executeEvent(QuestPlayer qp) {
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
        return this.getNext(0) == null && this.getNext(1) == null && this.getNext(2) == null && this.getNext(3) == null && this.getNext(4) == null;
    }
}
