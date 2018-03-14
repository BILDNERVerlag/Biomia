package de.biomia.spigot.specialEvents.winterEvent;

import de.biomia.spigot.BiomiaPlayer;
import de.biomia.spigot.configs.Config;
import de.biomia.universal.MySQL;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WinterTag {

    public static void open(BiomiaPlayer biomiaPlayer, int day, int rewardID) {
        MySQL.executeUpdate("Insert into `WinterEvent`(`Day`, `BiomiaPlayer`, `rewardID`) VALUES (" + day + ", "
                + biomiaPlayer.getBiomiaPlayerID() + ", " + rewardID + ")", MySQL.Databases.biomia_db);
    }

    public static boolean hasOpened(BiomiaPlayer biomiaPlayer, int day) {
        return (MySQL.executeQuerygetint("Select Day from WinterEvent where BiomiaPlayer = "
                + biomiaPlayer.getBiomiaPlayerID() + " and Day = " + day, "Day", MySQL.Databases.biomia_db) == day);
    }

    public static void bindCalendarDayToEntity(int day, UUID uuid) {
        List<String> entities = Config.getConfig().getStringList("Calendar." + day);
        if (entities == null) {
            entities = new ArrayList<>();
        }
        entities.add(uuid.toString());
        Config.getConfig().set("Calendar." + day, entities);
        Config.saveConfig();
    }
}
