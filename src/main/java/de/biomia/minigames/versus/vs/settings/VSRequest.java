package de.biomia.minigames.versus.vs.settings;

import de.biomia.minigames.versus.vs.game.GameInstance;
import de.biomia.minigames.versus.vs.main.VSMain;
import de.biomia.minigames.versus.vs.main.VSManager;
import de.biomia.api.BiomiaPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class VSRequest {

    private static final ArrayList<VSRequest> openRequests = new ArrayList<>();
    private static final ArrayList<BiomiaPlayer> playersInRound = new ArrayList<>();
    private static int lastID = 0;
    private int id;
    private int mapID;
    private VSManager.VSMode mode;
    private BiomiaPlayer leader;
    private BiomiaPlayer bp2;
    private GameInstance gameInstance;
    private boolean isServerRunning = false;
    private boolean badStart = false;

    public VSRequest(BiomiaPlayer leader, BiomiaPlayer bp2) {
        if (leader.equals(bp2)) {
            leader.getPlayer().sendMessage("\u00A7cDu kannst dir nicht selbst eine Anfrage schicken!");
            badStart = true;
            return;
        }
        id = lastID;
        this.leader = leader;
        this.bp2 = bp2;
        lastID++;
        openRequests.add(this);
    }

    public static boolean hasRequestSended(BiomiaPlayer von, BiomiaPlayer an) {
        return hasRequestSended(von) && an.equals(Objects.requireNonNull(getRequest(von)).bp2);
    }

    public static VSRequest getRequest(BiomiaPlayer von) {
        for (VSRequest request : openRequests) {
            if (request.leader.equals(von)) {
                return request;
            }
        }
        return null;
    }

    private static boolean hasRequestSended(BiomiaPlayer von) {
        for (VSRequest request : openRequests) {
            if (request.leader.equals(von)) {
                return true;
            }
        }
        return false;
    }

    public BiomiaPlayer getLeader() {
        return leader;
    }

    public BiomiaPlayer getReceiver() {
        return bp2;
    }

    public void sendRequest() {
        if (!badStart)
            if (!isInRunningRound())
                if (canStart()) {
                    Player p = leader.getPlayer();
                    BaseComponent comp = new TextComponent("\u00A7cDer Spieler \u00A7d" + leader.getPlayer().getName() + " \u00A7chat dich Herausgefordert!\n");
                    comp.addExtra("\u00A75Modus\u00A77/\u00A72Map\u00A78:\u00A75 " + getModus().name() + "\u00A77/\u00A72" + getMapName());
                    comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + p.getName()));
                    bp2.getPlayer().spigot().sendMessage(comp);
                    p.sendMessage("\u00A7cDu hast den Spieler \u00A7d" + bp2.getPlayer().getName() + " \u00A7cHerausgefordert!");
                } else
                    leader.getPlayer().sendMessage("\u00A7cDeine Einstellungen sind zu genau!");
    }

    private boolean isInRunningRound() {
        if (playersInRound.contains(leader)) {
            leader.getPlayer().sendMessage("\u00A7cDu bist bereits in einer Runde!");
            return true;
        } else if (playersInRound.contains(bp2)) {
            leader.getPlayer().sendMessage("\u00A7cDer Spieler ist bereits in einer Runde!");
            return true;
        }
        return false;
    }

    private int getID() {
        return id;
    }

    public boolean isServerRunning() {
        return isServerRunning;
    }

    public void accept() {
        leader.getPlayer().sendMessage("\u00A7cDer Spieler \u00A7d" + bp2.getPlayer().getName() + " \u00A7chat die Herausforderung angenommen!");
        bp2.getPlayer().sendMessage("\u00A7cDu hast die Herausforderung von \u00A7d" + leader.getPlayer().getName() + " \u00A7cangenommen!");
        startServer();
        gameInstance.getGameMode().start();
        remove();
        playersInRound.add(leader);
        playersInRound.add(bp2);
    }

    public void finish() {
        playersInRound.remove(leader);
        playersInRound.remove(bp2);
    }

    public void decline() {
        leader.getPlayer().sendMessage("\u00A7cDer Spieler \u00A7d" + bp2.getPlayer().getName() + " \u00A7chat die Herausforderung abgelehnt!");
        bp2.getPlayer().sendMessage("\u00A7cDu hast die Herausforderung von \u00A7d" + leader.getPlayer().getName() + " \u00A7cabgelehnt!");
        remove();
    }

    public void remove() {
        openRequests.remove(this);
    }

    public void startServer() {
        if (isServerRunning)
            return;
        isServerRunning = true;
        gameInstance = new GameInstance(mode, id, mapID, this);
    }

    public boolean hasSameSettings() {
        return getRandomGroupBOTH() && searchForMapBOTH();
    }

    private boolean canStart() {
        return getRandomGroup() && searchForMap();
    }

    private boolean searchForMap() {
        VSSettings requesterSettings = VSMain.getManager().getSettings(leader);
        ArrayList<Integer> maps = new ArrayList<>();

        int id = 100;
        VSSettingItem item = VSSettings.getItem(mode, id);
        while (item != null) {
            if (requesterSettings.getSetting(item)) {
                maps.add(id);
            }
            id++;
            item = VSSettings.getItem(mode, id);
        }
        if (maps.size() != 0) {
            mapID = maps.get(new Random().nextInt(maps.size()));
            return true;
        }
        return false;
    }

    private boolean searchForMapBOTH() {
        VSSettings requesterSettings = VSMain.getManager().getSettings(leader);
        VSSettings receiverSettings = VSMain.getManager().getSettings(bp2);

        ArrayList<Integer> maps = new ArrayList<>();

        int id = 100;
        VSSettingItem item = VSSettings.getItem(mode, id);
        while (item != null) {
            if (requesterSettings.getSetting(item) && receiverSettings.getSetting(item)) {
                maps.add(id);
            }
            id++;
            item = VSSettings.getItem(mode, id);
        }
        if (maps.size() != 0) {
            mapID = maps.get(new Random().nextInt(maps.size()));
            return true;
        }
        return false;
    }

    private boolean getRandomGroup() {

        VSSettings requesterSettings = VSMain.getManager().getSettings(leader);

        ArrayList<VSManager.VSMode> modes = new ArrayList<>();

        for (VSManager.VSMode mode : VSManager.VSMode.values()) {
            if (mode != VSManager.VSMode.Main) {
                VSSettingItem item = VSSettings.getItem(mode, 0);
                if (item != null)
                    if (requesterSettings.getSetting(item))
                        modes.add(mode);
            }
        }
        if (modes.size() == 0)
            return false;
        mode = modes.get(new Random().nextInt(modes.size()));
        return true;
    }

    private boolean getRandomGroupBOTH() {

        VSSettings requesterSettings = VSMain.getManager().getSettings(leader);
        VSSettings receiverSettings = VSMain.getManager().getSettings(bp2);

        ArrayList<VSManager.VSMode> modes = new ArrayList<>();

        for (VSManager.VSMode mode : VSManager.VSMode.values()) {
            if (mode != VSManager.VSMode.Main) {
                VSSettingItem item = VSSettings.getItem(mode, 0);
                if (requesterSettings.getSetting(item) && receiverSettings.getSetting(item)) {
                    modes.add(mode);
                }
            }
        }
        if (modes.size() == 0)
            return false;
        mode = modes.get(new Random().nextInt(modes.size()));
        return true;
    }

    private VSManager.VSMode getModus() {
        return mode;
    }

    private String getMapName() {
        return VSMain.getManager().getMapName(getModus(), mapID);
    }

}
