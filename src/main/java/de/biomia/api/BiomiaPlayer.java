package de.biomia.api;

import cloud.timo.TimoCloud.api.objects.PlayerObject;
import de.biomia.api.coins.Coins;
import de.biomia.api.main.Main;
import de.biomia.api.mysql.MySQL;
import de.biomia.api.pex.Rank;
import de.biomia.quests.general.QuestPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BiomiaPlayer extends OfflineBiomiaPlayer{

    // CONSTANTS
    private final Player p;
    private final PAFPlayer spigotPafpl;

    // ATTRIBUTES
    private boolean build = false;
    private boolean trollmode = false;
    private boolean getDamage = true;
    private boolean damageEntitys = true;
    private QuestPlayer questPlayer;

    // CONSTRUCTOR
    public BiomiaPlayer(Player p) {
        super(p);
        this.p = p;
        spigotPafpl = PAFPlayerManager.getInstance().getPlayer(p.getUniqueId());
    }

    // PUBLIC METHODS

    // BIOMIA-COIN RELATED METHODS


    // GETTERS AND SETTERS

    public PlayerParty getParty() {
        return PartyManager.getInstance().getParty(spigotPafpl);
    }

    public boolean isPartyLeader() {
        return isInParty() && spigotPafpl.equals(getParty().getLeader());
    }

    public boolean isInTrollmode() {
        return trollmode;
    }

    public void setTrollmode(boolean trollmode) {
        this.trollmode = trollmode;
    }

    public Player getPlayer() {
        return p;
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

    // PRIVATE METHODS

    private boolean isInParty() {
        return getParty() != null;
    }

    public List<PAFPlayer> getFriends() {
        return spigotPafpl.getFriends();
    }

    public List<PAFPlayer> getOnlineFriends() {
        List<PAFPlayer> onlineFriends = new ArrayList<>();
        for (PAFPlayer pafplayer : getFriends()) {

            PlayerObject po = Main.getUniversalTimoapi().getPlayer(pafplayer.getUniqueId());
            if (po != null && po.isOnline()) {
                onlineFriends.add(pafplayer);
            }
        }
        return onlineFriends;
    }
}
