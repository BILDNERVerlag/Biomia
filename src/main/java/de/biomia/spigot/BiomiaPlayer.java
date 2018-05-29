package de.biomia.spigot;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.PlayerObject;
import de.biomia.spigot.minigames.GameTeam;
import de.biomia.spigot.quests.DialogNodeConnector;
import de.biomia.spigot.server.quests.general.QuestPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BiomiaPlayer extends OfflineBiomiaPlayer {

    private class BiomiaPlayerCantBeAnNPCException extends Exception {
    }

    @Getter
    private int actualOnlineMinutes;

    // CONSTANTS
    private final PAFPlayer spigotPafpl;

    // ATTRIBUTES
    @Setter
    @Getter
    private boolean inBuildmode = false;
    @Setter
    @Getter
    private boolean inTrollmode = false;
    @Setter
    @Getter
    private boolean damageable = true;
    @Setter
    @Getter
    private boolean dangerous = true;
    private QuestPlayer questPlayer;
    @Getter
    private final Player player;
    @Setter
    @Getter
    private GameTeam team;
    @Setter
    @Getter
    private DialogNodeConnector dnc;

    // CONSTRUCTOR
    public BiomiaPlayer(Player p) {
        super(OfflineBiomiaPlayer.getBiomiaPlayerID(p.getName()), p.getName());
        if (p.hasMetadata("NPC")) {
            new BiomiaPlayerCantBeAnNPCException().printStackTrace();
        }
        this.player = p;
        spigotPafpl = PAFPlayerManager.getInstance().getPlayer(getUUID());
    }

    public QuestPlayer getQuestPlayer() {
        return questPlayer != null ? questPlayer : (questPlayer = new QuestPlayer(this));
    }

    public List<PAFPlayer> getFriends() {
        return spigotPafpl.getFriends();
    }

    public List<PAFPlayer> getOnlineFriends() {
        List<PAFPlayer> onlineFriends = new ArrayList<>();
        for (PAFPlayer pafplayer : getFriends()) {

            PlayerObject po = TimoCloudAPI.getUniversalAPI().getPlayer(pafplayer.getUniqueId());
            if (po != null && po.isOnline()) {
                onlineFriends.add(pafplayer);
            }
        }
        return onlineFriends;
    }

    public PlayerParty getParty() {
        return PartyManager.getInstance().getParty(spigotPafpl);
    }

    public boolean isPartyLeader() {
        return getParty() != null && spigotPafpl.equals(getParty().getLeader());
    }

    public void incrementOnlineMinutes() {
        actualOnlineMinutes++;
    }

}
