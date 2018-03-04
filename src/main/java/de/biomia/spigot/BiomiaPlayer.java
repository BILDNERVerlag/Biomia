package de.biomia.spigot;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.PlayerObject;
import de.biomia.spigot.server.quests.general.QuestPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BiomiaPlayer extends OfflineBiomiaPlayer {

    private int actualOnlineMinutes;

    // CONSTANTS
    private final PAFPlayer spigotPafpl;

    // ATTRIBUTES
    private boolean build = false;
    private boolean trollmode = false;
    private boolean getDamage = true;
    private boolean damageEntitys = true;
    private QuestPlayer questPlayer;
    private final Player player;

    // CONSTRUCTOR
    public BiomiaPlayer(Player p) {
        super(OfflineBiomiaPlayer.getBiomiaPlayerID(p.getName()), p.getName());
        this.player = p;
        spigotPafpl = PAFPlayerManager.getInstance().getPlayer(getUUID());
    }

    // GETTERS AND SETTERS
    public PlayerParty getParty() {
        return PartyManager.getInstance().getParty(spigotPafpl);
    }

    public boolean isPartyLeader() {
        return getParty() != null && spigotPafpl.equals(getParty().getLeader());
    }

    public boolean isInTrollmode() {
        return trollmode;
    }

    public void setTrollmode(boolean trollmode) {
        this.trollmode = trollmode;
    }

    public QuestPlayer getQuestPlayer() {
        return questPlayer != null ? questPlayer : (questPlayer = new QuestPlayer(this));
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

    public Player getPlayer() {
        return player;
    }

    public List<PAFPlayer> getFriends() {
        return spigotPafpl.getFriends();
    }

    public List<PAFPlayer> getOnlineFriends() {
        List<PAFPlayer> onlineFriends = new ArrayList<>();
        for (PAFPlayer pafplayer : getFriends()) {

            PlayerObject po = TimoCloudAPI.getUniversalInstance().getPlayer(pafplayer.getUniqueId());
            if (po != null && po.isOnline()) {
                onlineFriends.add(pafplayer);
            }
        }
        return onlineFriends;
    }

    public void incrementOnlineMinutes() {
        actualOnlineMinutes++;
    }

    public int getActualOnlineMinutes() {
        return actualOnlineMinutes;
    }
}
