package de.biomia.spigot.server.quests.band1;

import de.biomia.universal.Time;
import de.biomia.spigot.Biomia;
import de.biomia.spigot.server.quests.QuestConditions.ItemConditions;
import de.biomia.spigot.server.quests.QuestEvents.AddCoinEvent;
import de.biomia.spigot.server.quests.QuestEvents.GiveItemEvent;
import de.biomia.spigot.server.quests.QuestEvents.SummonEntity;
import de.biomia.spigot.server.quests.QuestEvents.TakeItemEvent;
import de.biomia.spigot.server.quests.Quests;
import de.biomia.spigot.server.quests.general.*;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class WirFeiernEinFest implements Listener {
    private final Quest q = Biomia.getQuestManager().registerNewQuest("WirFeiernEinFest", 1);
    private final NPC habil;

    private DialogMessage startDialog;
    private DialogMessage comeBackWithFireworks;
    private DialogMessage comeBackWithoutFireworks;
    private DialogMessage nachQuest;
    private final Location habilLoc = new Location(Bukkit.getWorld("Quests"), 111, 72, -278, 0, 0);

    public WirFeiernEinFest() {
        q.setInfoText("Habil ist an der Organisation eines gro�en Dorffestes beteiligt, und obwohl er f�r das Feuerwerk zust�ndig ist, hat er noch keine Raketen. Vielleicht kannst du welche auftreiben, drei St�ck sollten gen�gen.");
        q.setDisplayName("Wir feiern ein Fest!");
        habil = q.createNPC(EntityType.PLAYER, "Habil");
        habil.spawn(habilLoc);

        q.setRepeatable(true);
        q.setCooldown(1, Time.Tage);

        initDialog();
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (habil.getEntity().equals(e.getRightClicked())) {
            QuestPlayer qp = Biomia.getQuestPlayer(e.getPlayer());

            if (qp.getDialog() == null) {
                States state = qp.getState(q);
                if (state != null) {
                    switch (state) {
                        case STATUS1:
                            if (ItemConditions.hasItemInInventory(qp, Material.FIREWORK, 3))
                                qp.setDialog(comeBackWithFireworks);
                            else
                                qp.setDialog(comeBackWithoutFireworks);
                            break;
                        default:
                            break;
                    }
                } else
                    Quests.restartQuestIfTimeOver(qp, q, startDialog, nachQuest);
                qp.getDialog().execute(qp);
            }
        }
    }

    private void initDialog() {

        startDialog = new DialogMessage(q, habil).setInhalt(
                "Hallo! Hast du schon geh�rt? Wir feiern gerade, dass unsere letzte Ernte so gut ausgefallen ist. Leider fehlt nocht etwas, was die Feier perfekt machen w�rde.");
        startDialog.setFortsetzung("Kann ich euch helfen?").setFortsetzung("Kein Interesse! Tsch�ss!");
        startDialog.setNext("K�nntest du f�r uns ein paar Feuerwerksraketen bauen? Ich wollte schon immer mal"
                + " welche davon sehen, wie sie bunte Lichter an den Himmel zaubern. "
                + "Bisher hat es leider nie geklappt, weil sich niemand traut, gegen so viele Creeper "
                + "anzutreten. Nur wenn man sie im Kampf besiegt, bekommt man genug Schwarzpulver"
                + " daf�r. K�nntest du mir vielleicht, sagen wir, drei St�ck besorgen? Also sei vorsichtig, wenn du dich auf den Weg machst! Wenn du die drei"
                + " Feuerwerksraketen hast, bring sie zu mir!", 0, habil);

        startDialog.setNext("Oh, okay... Ciao!", 1, habil);
        startDialog.getNext(0).addPlayerToQuest();

        // comeback wo
        comeBackWithoutFireworks = new DialogMessage(q, habil)
                .setInhalt("Na, traust du dich nicht an die Creeper ran?");

        // comeback w
        comeBackWithFireworks = new DialogMessage(q, habil).setInhalt("Unglaublich! Du musst ein wahrlich"
                + " guter K�mpfer sein, wenn du so viele Creeper besiegt hast! Vielen Dank! Hier, "
                + "als kleines Dankesch�n schenke ich dir mein Feuerzeug!" + "Wir freuen uns schon!");
        comeBackWithFireworks.addEvent(new TakeItemEvent(Material.FIREWORK, 3));
        comeBackWithFireworks.addEvent(new GiveItemEvent(Material.FLINT_AND_STEEL, 1));
        comeBackWithFireworks.addEvent(new AddCoinEvent(300));
        Location loc = habilLoc.clone().add(0, 3, 0);
        comeBackWithFireworks.addEvent(new SummonEntity(loc, EntityType.FIREWORK,
                3));
        comeBackWithFireworks.finish();

        // nach quest
        nachQuest = new DialogMessage(q, habil).setInhalt("Gro�artiges Feuerwerk war das.");
    }
}
