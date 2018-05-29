package de.biomia.spigot.commands.quest;

import de.biomia.spigot.Biomia;
import de.biomia.spigot.commands.BiomiaCommand;
import de.biomia.spigot.messages.QuestMessages;
import de.biomia.spigot.messages.manager.ActionBar;
import de.biomia.spigot.server.quests.general.DialogMessage;
import de.biomia.spigot.server.quests.general.NPCManager;
import de.biomia.spigot.server.quests.general.Quest;
import de.biomia.spigot.server.quests.general.QuestPlayer;
import de.biomia.universal.Messages;
import net.citizensnpcs.api.ai.goals.WanderGoal;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class QuestCommands extends BiomiaCommand {

    private static boolean logTime = false;

    public QuestCommands() {
        super("q");
    }

    @SuppressWarnings("unchecked")
    public static void qupdatebookCommand(QuestPlayer qp) {

        BookMeta bookMeta = (BookMeta) qp.getBook().getItemMeta();
        List<IChatBaseComponent> pages;

        // Referenz auf die Liste der Buchseiten holen
        try {
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
            pages.clear();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return;
        }

        // Aktive und abgeschlossene coins laden
        ArrayList<Quest> activeQuests = new ArrayList<>();
        ArrayList<Quest> finishedQuests = new ArrayList<>();

        for (Quest q : qp.getActiveQuests()) {
            if (q == null)
                continue;
            activeQuests.add(q);
        }
        for (Quest q : qp.getFinishedQuests()) {
            if (q == null)
                continue;
            finishedQuests.add(q);
        }

        // Seite(n) mit aktiven Quests befuellen
        if (activeQuests.isEmpty()) {
            TextComponent output = new TextComponent("§1§l§nAktive Quests:\n\n\n§4(Noch keine!)");
            IChatBaseComponent page = ChatSerializer.a(ComponentSerializer.toString(output));
            pages.add(page);
        } else {
            // Alle coins nacheinander auflisten, pro Quest je eine neue Zeile
            TextComponent output = new TextComponent("§1§l§nAktive Quests:\n\n\n");

            TextComponent text;

            for (int i = 0; i < activeQuests.size(); i++) {
                Quest q = activeQuests.get(i);
                text = new TextComponent("§4" + q.getDisplayName() + "\n");
                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/q info " + q.getQuestName()));
                output.addExtra(text);

                if ((i % 10 == 0 && i != 0) || i == activeQuests.size() - 1) {
                    // falls zehn schleifendurchlaeufe vergangen sind ODER falls
                    // dies der letzte
                    // schleifendurchlauf ist werden die bisher durchlaufenen
                    // quests als neue seite
                    // hinzugefuegt
                    IChatBaseComponent page = ChatSerializer.a(ComponentSerializer.toString(output));
                    pages.add(page);
                    output = new TextComponent("\n");
                }
            }
        }
        // Seite mit abgeschlossenen Quests befuellen
        if (finishedQuests.isEmpty()) {
            TextComponent output = new TextComponent(
                    "§1§l§nAbgeschlossene\n     §1§l§nQuests:\n\n§4(Noch keine!)");
            IChatBaseComponent page = ChatSerializer.a(ComponentSerializer.toString(output));
            pages.add(page);
        } else {
            // Alle Quests nacheinander auflisten, pro Quest je eine neue Zeile
            TextComponent output = new TextComponent(
                    "§1§l§nAbgeschlossene\n     §1§l§nQuests:\n\n");
            TextComponent text;

            for (int i = 0; i < finishedQuests.size(); i++) {
                Quest q = finishedQuests.get(i);
                text = new TextComponent("§2" + finishedQuests.get(i).getDisplayName() + "\n");
                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/q info " + q.getQuestName()));
                output.addExtra(text);

                if ((i % 10 == 0 && i != 0) || i == finishedQuests.size() - 1) {
                    // falls zehn schleifendurchlaeufe vergangen sind ODER falls
                    // dies der letzte
                    // schleifendurchlauf ist werden die bisher durchlaufenen
                    // quests als neue seite
                    // hinzugefuegt
                    IChatBaseComponent page = ChatSerializer.a(ComponentSerializer.toString(output));
                    pages.add(page);
                    output = new TextComponent("\n");
                }
            }
        }
        // Bonusseite befuellen (Optionen, Stats, etc)
        TextComponent verschiedenesUeberschrift = new TextComponent("§1§l§nVerschiedenes\n\n");

        TextComponent statsButton = new TextComponent("§9§l<Statistik>\n\n");
        statsButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/q stats"));
        verschiedenesUeberschrift.addExtra(statsButton);

        TextComponent respawnButton = new TextComponent("§9§l<Respawn>\n\n");
        respawnButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/q respawn"));
        verschiedenesUeberschrift.addExtra(respawnButton);

        TextComponent resetButton = new TextComponent(
                "§c§l<Notfall-Reset>\n§c§oKlicke hier nur, falls\n"
                        + "§c§odu stecken bleibst,\n" + "§c§odich nichtmehr bewe-\n"
                        + "§c§ogen kannst, etc.\n\n");
        resetButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/q reset"));
        verschiedenesUeberschrift.addExtra(resetButton);

        IChatBaseComponent page = ChatSerializer.a(ComponentSerializer.toString(verschiedenesUeberschrift));
        pages.add(page);

        // Buch-Itemstack updaten
        Player p = qp.getPlayer();
        bookMeta.setAuthor(p.getName());
        qp.getBook().setItemMeta(bookMeta);

        // Buch im Spieler-Inventar updaten
        int i = 0;
        for (ItemStack is : p.getInventory().getContents()) {
            try {
                if (is.getType() == Material.WRITTEN_BOOK
                        && is.getItemMeta().getDisplayName().equals("§cTagebuch")) {
                    p.getInventory().setItem(i, qp.getBook());
                    // Spieler ueber Tagebuch-Update informieren
                    ActionBar.sendActionBar("§6Tagebuch wurde aktualisiert!", p);
                    return;
                }
            } catch (Exception e) {
                // do nothing
            }
            i++;
        }

    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {

        Player p = (Player) sender;
        QuestPlayer qp = Biomia.getQuestPlayer(p);

        // COMMANDS FOR ALL PLAYERS
        switch (args[0].toLowerCase()) {
            case "list":
                qlistCommand(sender);
                break;
            case "tagebuch":
                qTagebuchCommand(p, qp);
                break;
            case "info":
                qInfoCommand(sender, args, p);
                break;
            case "help":
                qhelpCommand(sender);
                break;
            case "stats":
                qstatCommand(sender, qp, args);
                break;
            case "respawn":
                respawnCommand(sender, qp);
                break;
            case "updatebook":
                qupdatebookCommand(qp);
                break;
            case "reset":
                qresetCommand(p, qp);
                break;
            default:
                qCommand(args, p, qp);
                break;
        }

        // COMMANDS FOR PLAYERS WITH SPECIAL PERMISSIONS
        if (!Biomia.getBiomiaPlayer(p).isOwnerOrDev()) {
            sender.sendMessage(Messages.NO_PERM);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "r":
                qrCommand(sender, args, qp);
                break;
            case "align":
                qalignCommand(sender);
                break;
            case "restore":
                qrestoreCommand();
                break;
            case "filldiary":
                qfilldiaryCommand(sender, qp);
                break;
            case "test":
                Bukkit.dispatchCommand(sender,
                        "summon wither_skeleton ~ ~1 ~ {ArmorItems:[{},{},{id:\"minecraft:leather_chestplate\",Count:1b,tag:{display:{color:0}}},"
                                + "{id:\"minecraft:leather_helmet\",Count:1b,tag:{display:{color:0}}}],"
                                + "HandItems:[{id:\"minecraft:iron_hoe\",Count:1b},{}],ActiveEffects:[{Id:14,Amplifier:0,Duration:20000,ShowParticles:0b}]}");
                break;
        }
    }

    private void respawnCommand(CommandSender sender, QuestPlayer qp) {
        if (qp.getDialog() == null) {
            Player p = ((Player) sender);
            p.setHealth(0);
        }
    }

    private void qresetCommand(Player p, QuestPlayer qp) {
        p.setWalkSpeed(0.2f);
        qp.setDialog(null);
        p.sendMessage("§8------------------§6Notfall-Reset-Info§8------------------");
        p.sendMessage("§aAlle deine Biomia-Einstellungen wurden wieder auf ihre "
                + "Standardwerte zurückgesetzt. Falls du denkst, dass es sich "
                + "um ein weiterbestehendes Problem handelt, benutze bitte die "
                + "§6/report§a-Funktion, damit das Problem behandelt werden kann.");
        p.sendMessage("§7§oJede Benutzung des Notfall-Resets wird verzeichnet. Den "
                + "Notfall-Reset zu missbrauchen - auf welche Art auch immer - "
                + "stellt einen Regelverstoß dar und kann mit einem temporären "
                + "oder permanenten Bann bestraft werden.");
    }

    private void qstatCommand(CommandSender sender, QuestPlayer qp, String[] args) {
        // Array, das speichert, wie viele Quests es pro Band gibt, zB questsProBand[1]
        // waere 17, falls es 17 Quests fuer Band 1 gibt etc
        if (args.length == 1) {
            int[] questsProBand = new int[5];
            for (Quest q : Biomia.getQuestManager().getQuests()) {
                questsProBand[q.getBand()]++;
            }
            sender.sendMessage("§8----------§6Quest Stats & Info§8----------");
            for (int i = 0; i < questsProBand.length; i++) {
                int playerProgress = 0;
                if (questsProBand[i] != 0) {
                    for (Quest q : qp.getFinishedQuests()) {
                        if (q == null)
                            continue;
                        if (q.getBand() == i) {
                            playerProgress++;
                        }
                    }
                    sender.sendMessage("§6Band " + i + ": §aFortschritt: "
                            + Math.round((double) playerProgress / (double) questsProBand[i] * 100) + "%"
                            + "\n   §7" + playerProgress + "/" + questsProBand[i]
                            + " Quests erfolgreich abgeschlossen.");
                }
            }
        } else {
            int band = -1;
            try {
                band = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§c" + args[1] + " ist keine erlaubte Zahl.");
            }
            sender.sendMessage("§6Band " + band + ": §aFortschritt: " + qp.getQuestPercentage(band) + "%");
        }
    }

    /**
     * Listet ALLE questserverspezifischen QuestCommands auf.
     */
    private void qhelpCommand(CommandSender sender) {
        sender.sendMessage("§8------------§6QuestCommands§8-----------");
        sender.sendMessage("§6/qlist §aum alle deine coins aufzulisten.");
        sender.sendMessage("§6/qstats §afür Infos zu deinem Questfortschritt.");
        sender.sendMessage("§6/qinfo <NAME> §afür Infos zu einer bestimmten Quest");
        sender.sendMessage("§6/tagebuch §aum ein neues Tagebuch zu erhalten.");
        if (Biomia.getBiomiaPlayer((Player) sender).isOwnerOrDev()) {
            sender.sendMessage("§8--------§cDev Commands§8--------");
            sender.sendMessage(
                    "§c§lAchtung! §r§cKönnen bei unpräziser Nutzung unvorhergesehene Konsequenzen herbeiführen."
                            + " Benutze sie nur, wenn du wirklich weißt, was du tust.");
            sender.sendMessage("§c/aion §aNPC-AI an.  §c/aioff §aNPC-AI aus.");
            sender.sendMessage("§c/aitoggle §aschält NPC-AI an bzw. aus.");
            sender.sendMessage("§6/qalign §asammelt alle NPCs an einem best. Punkt.");
            sender.sendMessage("§6/qrestore §asendet alle NPCs wieder zurück.");
            sender.sendMessage("§6/qr [NAME] §aum dich aus coins zu entfernen.");
        }
        sender.sendMessage("§8------------------------------------");

    }

    private void qrestoreCommand() {
        NPCManager.restoreNPCLocations();
    }

    private void qalignCommand(CommandSender sender) {
        // aligns all npcs in a row at the hardcoded location
        int i = 0;
        int j;
        for (Quest q : Biomia.getQuestManager().getQuests()) {
            for (NPC n : q.getNpcs()) {
                j = (i % 2 == 0) ? 0 : 1;
                n.teleport(new Location(Bukkit.getWorld("Quests"), 160 + i, 64, -263 + j), TeleportCause.COMMAND);
                n.getDefaultGoalController().addBehavior(WanderGoal.createWithNPCAndRange(n, 1, 1), 3);
                n.getDefaultGoalController().removeBehavior(WanderGoal.createWithNPCAndRange(n, 1, 1));
                i++;
            }
        }
        sender.sendMessage(QuestMessages.allNPCsAligned.replace("%n", i + ""));
    }

    private void qlistCommand(CommandSender sender) {
        // lists all quests, sorted by whether theyre currently active or already
        // finished

        StringBuilder temp = new StringBuilder();
        sender.sendMessage(QuestMessages.activeQuests);
        for (Quest q : (Biomia.getQuestPlayer((Player) sender)).getActiveQuests()) {
            if (q == null)
                continue;
            temp.append("§2<§a").append(q.getDisplayName()).append("§2>, ");
        }
        if (!temp.toString().equals(""))
            sender.sendMessage("§2{" + temp.substring(0, temp.length() - 2) + "§2}");
        else
            sender.sendMessage("§2{ }");
        temp = new StringBuilder();
        sender.sendMessage(QuestMessages.finishedQuests);
        for (Quest q : (Biomia.getQuestPlayer((Player) sender)).getFinishedQuests()) {
            if (q == null)
                continue;
            temp.append("§2<§a").append(q.getDisplayName()).append("§2>, ");
        }
        if (!temp.toString().equals(""))
            sender.sendMessage("§2{" + temp.substring(0, temp.length() - 2) + "§2}");
        else
            sender.sendMessage("§2{ }");
    }

    private void qrCommand(CommandSender sender, String[] args, QuestPlayer qp) {
        // removes all currently active and already finished quests from
        // their
        // respective lists
        switch (args.length) {
            case 2:
                sender.sendMessage(QuestMessages.tryingToRemoveQuest);
                Quest temp = null;
                // active quests
                for (Quest q : qp.getActiveQuests()) {
                    if (q == null)
                        continue;
                    if (args[1].equalsIgnoreCase(q.getQuestName())) {
                        temp = q;
                        qp.rmFromQuest(temp);
                        sender.sendMessage(QuestMessages.questRemovedFromActive.replace("%q", args[0]));
                    }
                }
                // finishedQuests
                for (Quest q : qp.getFinishedQuests()) {
                    if (q == null)
                        continue;
                    if (args[1].equalsIgnoreCase(q.getQuestName())) {
                        temp = q;
                        qp.unfinish(temp);
                        sender.sendMessage(QuestMessages.questRemovedFromFinished.replace("%q", args[0]));
                    }
                }
                if (temp == null) {
                    sender.sendMessage("Keine Quest mit dem Namen " + args[0] + " gefunden.");
                }
                break;
            case 1:

                sender.sendMessage(QuestMessages.tryingToRemoveAllQuests);

                for (Quest q : qp.getActiveQuests()) {
                    if (q == null)
                        continue;
                    qp.rmFromQuest(q);
                    sender.sendMessage(QuestMessages.questRemovedFromActive.replace("%q", q.getQuestName()));
                }

                for (Quest q : qp.getFinishedQuests()) {
                    if (q == null)
                        continue;
                    qp.unfinish(q);
                    sender.sendMessage(QuestMessages.questRemovedFromFinished.replace("%q", q.getQuestName()));
                }

                qp.getBuildableBlocks().clear();
                qp.getMineableBlocks().clear();

                break;
            default:
                sender.sendMessage("§c/qr <Quest>");
                break;
        }
        qp.updateBook();
    }

    private void qCommand(String[] args, Player p, QuestPlayer qp) {
        if (args.length == 1) {
            if (qp.getDialog() != null) {
                if (qp.getDialog() != null && !qp.getDialog().isLast()) {
                    int argInt = 1;
                    try {
                        argInt = Integer.valueOf(args[0]);
                    } catch (NumberFormatException ignored) {
                    }
                    qp.setDialog(DialogMessage.questMessages.get(argInt));
                    DialogMessage.questMessages.remove(argInt);
                    try {
                        qp.getDialog().execute(qp);
                    } catch (NullPointerException e) {
                        // do nothing
                    }
                } else {
                    qp.setDialog(null);
                    p.setWalkSpeed(0.2F);
                }
            }
        } else if (args.length > 1 && args[0].equals("next")) {
            if (qp.getDialog() != null && !qp.getDialog().isLast()) {
                qp.setDialog(qp.getDialog().getNext(0));
                if (qp.getDialog() != null) {
                    qp.getDialog().execute(qp);
                } else {
                    qp.setDialog(null);
                    p.setWalkSpeed(0.2F);
                }
            }
        }
    }

    private void qTagebuchCommand(Player p, QuestPlayer qp) {
        // gives a diary
        for (ItemStack is : p.getInventory().getContents()) {
            if (is != null && is.getType() == Material.WRITTEN_BOOK
                    && is.getItemMeta().getDisplayName().equals("§cTagebuch")) {
                p.sendMessage(QuestMessages.alreadyHaveABook);
                return;
            }
        }
        p.getInventory().addItem(qp.getBook());
    }

    private void qInfoCommand(CommandSender sender, String[] args, Player p) {
        // get info about a specific quest
        if (args.length == 2) {
            // get quest
            Quest q = Biomia.getQuestManager().getQuest(args[1]);
            if (q == null) {
                sender.sendMessage(QuestMessages.questDoesNotExist);
                return;
            }
            // send info messages
            sender.sendMessage("");
            sender.sendMessage(QuestMessages.dividerLine);
            sender.sendMessage("§b§l" + q.getDisplayName());
            if (q.getInfoText() == null)
                sender.sendMessage(QuestMessages.noInformationAboutThisQuest);
            else
                sender.sendMessage("§a" + q.getInfoText());
            sender.sendMessage("");
            sender.sendMessage("§b" + QuestMessages.involvedNPCs);
            StringBuilder s = new StringBuilder();
            for (NPC n : q.getNpcs()) s.append("§a").append(n.getName()).append(", ");
            if (s.toString().equals("")) s = new StringBuilder("§aKeine.");
            else s = new StringBuilder(s.substring(0, s.length() - 2));
            sender.sendMessage(s.toString());
            sender.sendMessage(QuestMessages.dividerLine);
            if (Biomia.getBiomiaPlayer(p).isStaff()) {
                sender.sendMessage("§8ID=" + q.getQuestID() + ", §8Cooldown=" + q.getCooldown()
                        + "s, §8Repeatable=" + q.isRepeatable());
                sender.sendMessage(QuestMessages.dividerLine);
            }
        } else {
            sender.sendMessage("§c/qinfo <QuestName>");
        }
    }

    private void qfilldiaryCommand(CommandSender sender, QuestPlayer qp) {
        for (Quest q : Biomia.getQuestManager().getQuests()) {
            qp.addToQuest(q);
            qp.finish(q);
            qp.addToQuest(q);
            qp.updateBook();
        }
        sender.sendMessage(QuestMessages.bookFilled);
    }

}