package de.biomiaAPI.achievements;

import java.util.Observable;
import java.util.Observer;

import org.bukkit.event.player.PlayerJoinEvent;

import de.biomiaAPI.Biomia;
import de.biomiaAPI.BiomiaPlayer;

public class Achievements implements Observer {

	/*
	 * Biomia Achievements-System basierend auf dem Java
	 * Observer-Observable-Pattern. Observer ist ein Interface, Observable eine
	 * erbbare Klasse.
	 * 
	 * Diese Achievementsklasse stellt den "Observer" dar, jede Klasse, die für
	 * Achievements interessante Daten enthält, sollte "Observable" extenden. Jedes
	 * Mal wenn in der observable Klasse etwas interessantes passiert, ruft man dann
	 * erst "setChanged()" auf, d.h. soviel wie
	 * "eine interessante Änderung ist aufgetreten", danach
	 * "notifyObservers(Object c)", das alle Observer des Observable dann darüber
	 * informiert (wenn davor nicht setChanged() aufgerufen wird, passiert nichts).
	 * 
	 * Jedes Mal, wenn ein Observable "notfiyObservers(Object c)" aufruft, wird im
	 * Observer "update(Observable o, Object arg)" aufgerufen. "Observable o" ist
	 * immer das Objekt, von dem die Notification kommt und "Object arg" ist das
	 * Objekt, das der Observer mitschickt, um zu identifizieren, was gerade
	 * passiert ist. Ich denke es ist sinnvoll, dafür verschiedene
	 * "AchievementEvent"-Klassen anzulegen.
	 * 
	 * zB könnte man jedes Mal wenn ein Spieler Geld bekommt, das Achievementsystem
	 * informieren (notfifyObservers(GiveCoinAchievementEvent(50))), die Daten
	 * speichern, und wenn irgendwann ein bestimmter Punkt erreicht ist, ein
	 * Achievement unlocken.
	 * 
	 */

	private enum BiomiaAchievement {
		LogDichFünfmalAufDemQuestServerEin
	}

	@Override
	public void update(Observable o, Object arg) {
		// Beispielcode
		switch (o.getClass().getName()) {
		/*
		 * Achievement für "Logge dich 5 mal auf dem QuestServer ein" (random Beispiel)
		 * Alles, was sonst nötig ist, ist im PlayerJoinEvent von Quests setChanged()
		 * und notifyObservers(e) hinzuzufügen
		 */
		case "QuestListener":
			if (arg instanceof PlayerJoinEvent) {
				// speichere in der datenbank, dass der spieler sich eingeloggt hat
				// falls in der datenbank jetzt 5 oder mehr steht:
				unlock(Biomia.getBiomiaPlayer(((PlayerJoinEvent) arg).getPlayer()),
						BiomiaAchievement.LogDichFünfmalAufDemQuestServerEin);
			}
			break;
		default:
			break;
		}
	}

	private void unlock(BiomiaPlayer bp, BiomiaAchievement achievement) {
		// speichere das achievement für den spieler in der datenbank
	}

}
