package de.biomia.spigot.minigames.versus.settings;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.minigames.GameInstance;
import de.biomia.spigot.minigames.GameType;
import de.biomia.spigot.minigames.kitpvp.KitPvP;
import de.biomia.spigot.minigames.versus.Versus;
import de.biomia.universal.Messages;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class VSRequest {

    private static final ArrayList<VSRequest> openRequests = new ArrayList<>();
    private static int lastID = 0;

    private int id;
    private String mapName;
    private GameType mode;
    private BiomiaPlayer leader;
    private BiomiaPlayer bp2;
    private boolean cancelRequest = false;

    public VSRequest(BiomiaPlayer leader, BiomiaPlayer bp) {
        if (leader.equals(bp)) {
            leader.getPlayer().sendMessage("§cDu kannst dir nicht selbst eine Anfrage schicken!");
            cancelRequest = true;
            return;
        }
        id = lastID++;
        this.bp2 = bp;
        this.leader = leader;
        openRequests.add(this);
    }

    public static boolean hasRequestSended(BiomiaPlayer von, BiomiaPlayer an) {
        VSRequest request = getRequest(von);
        return request != null && request.bp2.equals(an);
    }

    public static VSRequest getRequest(BiomiaPlayer von) {
        for (VSRequest request : openRequests) {
            if (request.leader.equals(von))
                return request;
        }
        return null;
    }

    public void sendRequest() {
        if (!cancelRequest && !isInRunningRound())
            if (canStart()) {
                Player p = leader.getPlayer();
                BaseComponent comp = new TextComponent(String.format("%sDer Spieler %s%s%s hat dich herausgefordert!\n", Messages.COLOR_MAIN, Messages.COLOR_SUB, leader.getName(), Messages.COLOR_MAIN));
                comp.addExtra(String.format("%sModus: %s%s%s / Map: %s%s", Messages.COLOR_AUX, Messages.COLOR_SUB, getModus().getDisplayName(), Messages.COLOR_AUX, Messages.COLOR_SUB, mapName));
                comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + p.getName()));
                bp2.getPlayer().spigot().sendMessage(comp);
                p.sendMessage(String.format("%sDu hast den Spieler %s%s%s herausgefordert!\n", Messages.COLOR_MAIN, Messages.COLOR_SUB, bp2.getName(), Messages.COLOR_MAIN));
            } else {
                leader.getPlayer().sendMessage("§cDeine Einstellungen sind zu genau!");
                openRequests.remove(this);
            }
    }

    private boolean isInRunningRound() {
        if (leader.getTeam() != null) {
            leader.getPlayer().sendMessage("§cDu bist bereits in einer Runde!");
            return true;
        } else if (bp2.getTeam() != null) {
            leader.getPlayer().sendMessage("§cDer Spieler ist bereits in einer Runde!");
            return true;
        }
        return false;
    }

    public void accept() {
        leader.getPlayer().sendMessage("§cDer Spieler §b" + bp2.getPlayer().getName() + " §chat die Herausforderung angenommen!");
        bp2.getPlayer().sendMessage("§cDu hast die Herausforderung von §b" + leader.getPlayer().getName() + " §cangenommen!");
        startServer();
        remove();
    }

    public void decline() {
        leader.getPlayer().sendMessage("§cDer Spieler §b" + bp2.getPlayer().getName() + " §chat die Herausforderung abgelehnt!");
        bp2.getPlayer().sendMessage("§cDu hast die Herausforderung von §b" + leader.getPlayer().getName() + " §cabgelehnt!");
        remove();
    }

    public void remove() {
        openRequests.remove(this);
    }

    public void startServer() {
        GameInstance gameInstance = new GameInstance(mode, mapName, Versus.getInstance().getManager().copyWorld(mode, id, mapName).getName(), 2, 1);
        gameInstance.registerPlayer(leader);
        gameInstance.registerPlayer(bp2);
        Versus.getInstance().getManager().getRequests().put(gameInstance, this);

        if (gameInstance.getType() == GameType.KIT_PVP_VS)
            ((KitPvP) gameInstance.getGameMode()).setGameKit(leader);
        gameInstance.getGameMode().start();
    }

    public boolean hasSameSettings() {
        return getRandomGroup(true) && searchForMap(true);
    }

    private boolean canStart() {
        return getRandomGroup(false) && searchForMap(false);
    }

    private boolean searchForMap(boolean checkBoth) {
        VSSettings requesterSettings = Versus.getInstance().getManager().getSettings(leader);
        VSSettings receiverSettings = Versus.getInstance().getManager().getSettings(bp2);

        ArrayList<String> maps = new ArrayList<>();

        int id = 100;
        VSSettingItem item;

        while (true) {
            item = VSSettings.getItem(mode, id++);
            if (item == null)
                break;
            if (requesterSettings.isEnabled(item)) {
                if (checkBoth && !receiverSettings.isEnabled(item)) {
                    continue;
                }
                maps.add(item.getName());
            }
        }

        if (maps.isEmpty())
            return false;
        mapName = maps.get(new Random().nextInt(maps.size()));
        return true;
    }

    private boolean getRandomGroup(boolean checkBoth) {

        VSSettings requesterSettings = Versus.getInstance().getManager().getSettings(leader);
        VSSettings receiverSettings = Versus.getInstance().getManager().getSettings(bp2);

        ArrayList<GameType> modes = new ArrayList<>();

        for (GameType mode : GameType.values()) {
            VSSettingItem item = VSSettings.getItem(mode, 0);
            if (item != null)
                if (requesterSettings.isEnabled(item)) {
                    if (checkBoth && !receiverSettings.isEnabled(item)) {
                        continue;
                    }
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
