package de.biomia.quests.general;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Quest {
	
	ArrayList<Integer> getNpcIDs();

	ArrayList<NPC> getNpcs();

	String getQuestName();

	void setQuestName(String questName0);

	void setDialog(DialogMessage dialogpl, States stat);

	HashMap<States, DialogMessage> getDialogs();

	void removePlayer(QuestPlayer qp);

	void addPlayer(QuestPlayer qp);

	DialogMessage getDialog(States stat);

	int getQuestID();

	void registerQuestIfnotExist();

	int getBand();

	boolean isRepeatble();

	void setCooldown(int i, TIME t);

	void setRepeatable(boolean b);

	int getCooldown();

	boolean getRemoveOnReload();

	void setRemoveOnReload(boolean b);

	boolean getPlayableWithParty();

	void setPlayableWithParty(boolean b);

	List<QuestPlayer> getActiveOnlinePlayers();

	List<Integer> getActivePlayerBiomiaIDs();

	NPC createNPC(EntityType entity, String name);
	
	void setDisplayName(String s);
	
	void setInfoText(String s);
	
	String getDisplayName();
	
	String getInfoText();
	
	// -----------------

}