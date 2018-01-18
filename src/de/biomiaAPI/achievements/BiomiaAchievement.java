package de.biomiaAPI.achievements;

@SuppressWarnings("UnusedAssignment")
public enum BiomiaAchievement {

	LogDichFuenfmalAufDemQuestServerEin, VerdieneFuenftausendCoins;

	/**
	 * Eine etwas detaillierte Beschreibung des Achievements
	 */
	public String getDescription(BiomiaAchievement bA) {
		String out = "Leider ist keine Beschreibung vorhanden. Beschwer dich am besten bei den Admins.";
		switch (bA) {
		case LogDichFuenfmalAufDemQuestServerEin:
			out = "Fünfmal auf dem Questserver einloggen";
		case VerdieneFuenftausendCoins:
			out = "Verdiene insgesamt mindestens 5000 BC.";
			break;
		default:
			out = "Dieses Achievement existiert nicht. Wenn du diese Nachricht liest, ist irgendetwas gewaltig schiefgelaufen. (-gDescr-)";
			break;
		}
		return out;
	}

	/**
	 * Name, der fuer das Achievement ingame angezeigt werden soll.
	 */
	public String getDisplayName(BiomiaAchievement bA) {
		String out = bA.toString();
		switch (bA) {
		case LogDichFuenfmalAufDemQuestServerEin:
			out = "Echter Abenteurer";
		case VerdieneFuenftausendCoins:
			out = "Mr. Money jr.";
			break;
		default:
			out = "Dieses Achievement existiert nicht. Wenn du diese Nachricht liest, ist irgendetwas gewaltig schiefgelaufen. (-gDispl-)";
			break;
		}
		return out;
	}
}