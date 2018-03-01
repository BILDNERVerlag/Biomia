package de.biomia.plugin.specialEvents.winterEvent;//package de.biomia.specialEvents.winterEvent;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import BiomiaPluginMain;
//import de.biomia.BiomiaPlayer;
//import de.biomia.dataManager.MySQL;
//
//public class WinterTag {
//
//	public static void open(BiomiaPlayer biomiaPlayer, int day, int rewardID) {
//		MySQL.executeUpdate("Insert into `WinterEvent`(`Day`, `BiomiaPlayer`, `rewardID`) VALUES (" + day + ", "
//				+ biomiaPlayer.getBiomiaPlayerID() + ", " + rewardID + ")");
//	}
//
//	public static boolean hasOpened(BiomiaPlayer biomiaPlayer, int day) {
//		return (MySQL.executeQuerygetint("Select Day from WinterEvent where BiomiaPlayer = "
//				+ biomiaPlayer.getBiomiaPlayerID() + " and Day = " + day, "Day") == day) ? true : false;
//	}
//
//	public static void bindCalendarDayToEntity(int day, UUID uuid) {
//		List<String> entities = BiomiaPluginMain.getPlugin().getConfig().getStringList("Calendar." + day);
//		if(entities == null) {
//			entities = new ArrayList<>();
//		}
//		entities.add(uuid.toString());
//		BiomiaPluginMain.getPlugin().getConfig().set("Calendar." + day, entities);
//		BiomiaPluginsaveConfig();;
//	}
//}
