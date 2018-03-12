package de.biomia.spigot.minigames.versus.settings;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.versus.Versus;
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
    private String mapName;
    private GameType mode;
    private BiomiaPlayer leader;
    private BiomiaPlayer bp2;
    private GameInstance gameInstance;
    private boolean badStart = false;

    public VSRequest(BiomiaPlayer leader, BiomiaPlayer bp2) {
        if (leader.equals(bp2)) {
            leader.getPlayer().sendMessage("\u00A7cDu kannst dir nicht selbst eine Anfrage schicken!");
            badStart = true;
            return;
        }
        id = lastID++;
        this.leader = leader;
        this.bp2 = bp2;
        openRequests.add(this);
    }

    public static boolean hasRequestSended(BiomiaPlayer von, BiomiaPlayer an) {
        return hasRequestSended(von) && an.equals(Objects.requireNonNull(getRequest(von)).bp2);
    }

    public static VSRequest getRequest(BiomiaPlayer von) {
        for (VSRequest request : openRequests) {
            if (request.leader.equals(von))
                return request;
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

    public void sendRequest() {
        if (!badStart)
            if (!isInRunningRound())
                if (canStart()) {
                    Player p = leader.getPlayer();
                    BaseComponent comp = new TextComponent("\u00A7cDer Spieler \u00A7d" + leader.getName() + " \u00A7chat dich Herausgefordert!\n");
                    comp.addExtra("\u00A75Modus\u00A77/\u00A72Map\u00A78:\u00A75 " + getModus().getDisplayName() + "\u00A77/\u00A72" + mapName);
                    comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + p.getName()));
                    bp2.getPlayer().spigot().sendMessage(comp);
                    p.sendMessage("\u00A7cDu hast den Spieler \u00A7d" + bp2.getName() + " \u00A7cHerausgefordert!");
                } else {
                    leader.getPlayer().sendMessage("\u00A7cDeine Einstellungen sind zu genau!");
                    openRequests.remove(this);
                }
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
        gameInstance = new GameInstance(mode, Versus.getInstance().getManager().copyWorld(mode, id, mapName), mapName, 2, 1);
        gameInstance.registerPlayer(leader);
        gameInstance.registerPlayer(bp2);
        ((Versus) Biomia.getSeverInstance()).getManager().getRequests().put(gameInstance, this);
    }

    public boolean hasSameSettings() {
        return getRandomGroupBOTH() && searchForMapBOTH();
    }

    private boolean canStart() {
        return getRandomGroup() && searchForMap();
    }

    private boolean searchForMap() {
        VSSettings requesterSettings = ((Versus) Biomia.getSeverInstance()).getManager().getSettings(leader);
        ArrayList<String> maps = new ArrayList<>();

        int id = 100;
        VSSettingItem item = VSSettings.getItem(mode, id);

        while (item != null) {
            if (requesterSettings.getSetting(item))
                maps.add(item.getName());
            id++;
            item = VSSettings.getItem(mode, id);
        }
        if (!maps.isEmpty()) {
            mapName = maps.get(new Random().nextInt(maps.size()));
            return true;
        }
        return false;
    }

    private boolean searchForMapBOTH() {
        VSSettings requesterSettings = ((Versus) Biomia.getSeverInstance()).getManager().getSettings(leader);
        VSSettings receiverSettings = ((Versus) Biomia.getSeverInstance()).getManager().getSettings(bp2);

        ArrayList<String> maps = new ArrayList<>();

        int id = 100;
        VSSettingItem item = VSSettings.getItem(mode, id);
        while (item != null) {
            if (requesterSettings.getSetting(item) && receiverSettings.getSetting(item)) {
                maps.add(item.getName());
            }
            id++;
            item = VSSettings.getItem(mode, id);
        }
        if (maps.size() != 0) {
            mapName = maps.get(new Random().nextInt(maps.size()));
            return true;
        }
        return false;
    }

    private boolean getRandomGroup() {

        VSSettings requesterSettings = ((Versus) Biomia.getSeverInstance()).getManager().getSettings(leader);

        ArrayList<GameType> modes = new ArrayList<>();

        for (GameType mode : GameType.values()) {
            VSSettingItem item = VSSettings.getItem(mode, 0);
            if (item != null)
                if (requesterSettings.getSetting(item))
                    modes.add(mode);
        }
        if (modes.size() == 0)
            return false;
        mode = modes.get(new Random().nextInt(modes.size()));
        return true;
    }

    private boolean getRandomGroupBOTH() {

        VSSettings requesterSettings = ((Versus) Biomia.getSeverInstance()).getManager().getSettings(leader);
        VSSettings receiverSettings = ((Versus) Biomia.getSeverInstance()).getManager().getSettings(bp2);

        ArrayList<GameType> modes = new ArrayList<>();

        for (GameType mode : GameType.values()) {
            VSSettingItem item = VSSettings.getItem(mode, 0);
            if (requesterSettings.getSetting(item) && receiverSettings.getSetting(item)) {
                modes.add(mode);
            }
        }
        if (modes.size() == 0)
            return false;
        mode = modes.get(new Random().nextInt(modes.size()));
        return true;
    }

    private GameType getModus() {
        return mode;
    }

}
