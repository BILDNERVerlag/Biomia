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
import net.citizensnpcs.api.ai.GoalController;
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

import java.util.*;

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
            TextComponent output = new TextComponent("\u00A71\u00A7l\u00A7nAktive Quests:\n\n\n\u00A74(Noch keine!)");
            IChatBaseComponent page = ChatSerializer.a(ComponentSerializer.toString(output));
            pages.add(page);
        } else {
            // Alle coins nacheinander auflisten, pro Quest je eine neue Zeile
            TextComponent output = new TextComponent("\u00A71\u00A7l\u00A7nAktive Quests:\n\n\n");

            TextComponent text;

            for (int i = 0; i < activeQuests.size(); i++) {
                Quest q = activeQuests.get(i);
                text = new TextComponent("\u00A74" + q.getDisplayName() + "\n");
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
                    "\u00A71\u00A7l\u00A7nAbgeschlossene\n     \u00A71\u00A7l\u00A7nQuests:\n\n\u00A74(Noch keine!)");
            IChatBaseComponent page = ChatSerializer.a(ComponentSerializer.toString(output));
            pages.add(page);
        } else {
            // Alle Quests nacheinander auflisten, pro Quest je eine neue Zeile
            TextComponent output = new TextComponent(
                    "\u00A71\u00A7l\u00A7nAbgeschlossene\n     \u00A71\u00A7l\u00A7nQuests:\n\n");
            TextComponent text;

            for (int i = 0; i < finishedQuests.size(); i++) {
                Quest q = finishedQuests.get(i);
                text = new TextComponent("\u00A72" + finishedQuests.get(i).getDisplayName() + "\n");
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
        TextComponent verschiedenesUeberschrift = new TextComponent("\u00A71\u00A7l\u00A7nVerschiedenes\n\n");

        TextComponent statsButton = new TextComponent("\u00A79\u00A7l<Statistik>\n\n");
        statsButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/q stats"));
        verschiedenesUeberschrift.addExtra(statsButton);

        TextComponent respawnButton = new TextComponent("\u00A79\u00A7l<Respawn>\n\n");
        respawnButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/q respawn"));
        verschiedenesUeberschrift.addExtra(respawnButton);

        TextComponent resetButton = new TextComponent(
                "\u00A7c\u00A7l<Notfall-Reset>\n\u00A7c\u00A7oKlicke hier nur, falls\n"
                        + "\u00A7c\u00A7odu stecken bleibst,\n" + "\u00A7c\u00A7odich nichtmehr bewe-\n"
                        + "\u00A7c\u00A7ogen kannst, etc.\n\n");
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
                        && is.getItemMeta().getDisplayName().equals("\u00A7cTagebuch")) {
                    p.getInventory().setItem(i, qp.getBook());
                    // Spieler ueber Tagebuch-Update informieren
                    ActionBar.sendActionBar("\u00A76Tagebuch wurde aktualisiert!", p);
                    return;
                }
            } catch (Exception e) {
                // do nothing
            }
            i++;
        }

    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        long startTime = System.currentTimeMillis();
        if (sender instanceof Player) {

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
                    qCommand(args,p,qp);
                    break;
            }

            // COMMANDS FOR PLAYERS WITH SPECIAL PERMISSIONS
            if (sender.hasPermission("biomia.quests.*")) {
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
                    case "aion":
                        aionCommand();
                        break;
                    case "aioff":
                        aioffCommand();
                        break;
                    case "aitoggle":
                        aitoggleCommand();
                        break;
                    case "filldiary":
                        qfilldiaryCommand(sender, qp);
                        break;
                    case "log":
                        logTime = !logTime;
                        sender.sendMessage(
                                "\u00A78\u00A7kzzz\u00A7r\u00A76Logging-Schalter bet\u00e4tigt.\u00A78\u00A7kzzz");
                        break;
                    case "test":
                        Bukkit.dispatchCommand(sender,
                                "summon wither_skeleton ~ ~1 ~ {ArmorItems:[{},{},{id:\"minecraft:leather_chestplate\",Count:1b,tag:{display:{color:0}}},"
                                        + "{id:\"minecraft:leather_helmet\",Count:1b,tag:{display:{color:0}}}],"
                                        + "HandItems:[{id:\"minecraft:iron_hoe\",Count:1b},{}],ActiveEffects:[{Id:14,Amplifier:0,Duration:20000,ShowParticles:0b}]}");
                        break;
                    default:
                        break;
                }
            }

        } else {
            sender.sendMessage(Messages.NO_PLAYER);
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        if (logTime)
            Bukkit.broadcastMessage("\u00A78\u00A7kzzz\u00A7r\u00A77Methodenzeit:\u00A78\u00A7kzz\u00A7r\u00A77"
                    + elapsedTime + "ms\u00A78\u00A7kzzz");

        return true;
    }

    private void respawnCommand(CommandSender sender, QuestPlayer qp) {
        if (sender instanceof Player)
            if (qp.getDialog() == null) {
                Player p = ((Player) sender);
                p.setHealth(0);
            }
    }

    private void qresetCommand(Player p, QuestPlayer qp) {
        p.setWalkSpeed(0.2f);
        qp.setDialog(null);
        p.sendMessage("\u00A78------------------\u00A76Notfall-Reset-Info\u00A78------------------");
        p.sendMessage("\u00A7aAlle deine Biomia-Einstellungen wurden wieder auf ihre "
                + "Standardwerte zur\u00fcckgesetzt. Falls du denkst, dass es sich "
                + "um ein weiterbestehendes Problem handelt, benutze bitte die "
                + "\u00A76/report\u00A7a-Funktion, damit das Problem behandelt werden kann.");
        p.sendMessage("\u00A77\u00A7oJede Benutzung des Notfall-Resets wird verzeichnet. Den "
                + "Notfall-Reset zu missbrauchen - auf welche Art auch immer - "
                + "stellt einen Regelversto\u00df dar und kann mit einem tempor\u00e4ren "
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
            sender.sendMessage("\u00A78----------\u00A76Quest Stats & Info\u00A78----------");
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
                    sender.sendMessage("\u00A76Band " + i + ": \u00A7aFortschritt: "
                            + Math.round((double) playerProgress / (double) questsProBand[i] * 100) + "%"
                            + "\n   \u00A77" + playerProgress + "/" + questsProBand[i]
                            + " Quests erfolgreich abgeschlossen.");
                }
            }
        } else {
            int band = -1;
            try {
                band = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("\u00A7c" + args[1] + " ist keine erlaubte Zahl.");
            }
            sender.sendMessage("\u00A76Band " + band + ": \u00A7aFortschritt: " + qp.getQuestPercentage(band) + "%");
        }
    }

    /**
     * Listet ALLE questserverspezifischen QuestCommands auf.
     */
    private void qhelpCommand(CommandSender sender) {
        sender.sendMessage("\u00A78------------\u00A76QuestCommands\u00A78-----------");
        if (sender.hasPermission("biomia.quests")) {
            sender.sendMessage("\u00A76/qalign \u00A7asammelt alle NPCs an einem best. Punkt.");
            sender.sendMessage("\u00A76/qrestore \u00A7asendet alle NPCs wieder zur\u00fcck.");
            sender.sendMessage("\u00A76/qr [NAME] \u00A7aum dich aus coins zu entfernen.");
        }
        sender.sendMessage("\u00A76/qlist \u00A7aum alle deine coins aufzulisten.");
        sender.sendMessage("\u00A76/qstats \u00A7af\u00fcr Infos zu deinem Questfortschritt.");
        sender.sendMessage("\u00A76/qinfo <NAME> \u00A7af\u00fcr Infos zu einer bestimmten Quest");
        sender.sendMessage("\u00A76/tagebuch \u00A7aum ein neues Tagebuch zu erhalten.");
        if (sender.hasPermission("biomia.quests")) {
            sender.sendMessage("\u00A78--------\u00A7cExperimentelle QuestCommands\u00A78--------");
            sender.sendMessage(
                    "\u00A7c\u00A7lAchtung! \u00A7r\u00A7cK\u00f6nnen bei unpr\u00e4ziser Nutzung unvorhergesehene Konsequenzen herbeif\u00fchren."
                            + " Benutze sie nur, wenn du wirklich wei\u00dft, was du tust.");
            sender.sendMessage("\u00A7c/aion \u00A7aNPC-AI an.  \u00A7c/aioff \u00A7aNPC-AI aus.");
            sender.sendMessage("\u00A7c/aitoggle \u00A7asch\u00e4lt NPC-AI an bzw. aus.");
        }
        sender.sendMessage("\u00A78------------------------------------");

    }

    private void aitoggleCommand() {
        /*
         * deactivates npc AI, but be wary that npcs still finish their current task.
         * e.g. if an npc has found a destination location and you turn off the ai, the
         * npc will still continue to walk until he's reached his destination.
         */
        StringBuilder aktiviert = new StringBuilder(" ");
        StringBuilder deaktiviert = new StringBuilder(" ");
        Set<NPC> npcs = new HashSet<>();
        for (Quest q : Biomia.getQuestManager().getQuests()) {
            npcs.addAll(q.getNpcs());
        }
        for (NPC n : npcs) {
            GoalController gc = n.getDefaultGoalController();
            if (gc.isPaused()) {
                gc.setPaused(false);
                n.getNavigator().getLocalParameters().speedModifier(0.8f);
                gc.addBehavior(WanderGoal.createWithNPC(n), 3);
                if (!n.getName().equals(""))
                    aktiviert.append("\u00A72<\u00A7a").append(n.getName()).append("\u00A72>\u00A7a, ");
            } else {
                gc.cancelCurrentExecution();
                gc.setPaused(true);
                if (!n.getName().equals(""))
                    deaktiviert.append("\u00A72<\u00A7a").append(n.getName()).append("\u00A72>\u00A7a, ");
            }
        }
        Bukkit.broadcastMessage("\u00A7dAI aktiviert f\u00fcr:");
        if (aktiviert.length() >= 2)
            Bukkit.broadcastMessage("\u00A72{" + aktiviert.substring(0, aktiviert.length() - 2) + "\u00A72 }");
        else
            Bukkit.broadcastMessage("\u00A72{ }");
        Bukkit.broadcastMessage("\u00A7dAI deaktiviert f\u00fcr:");
        if (deaktiviert.length() >= 2)
            Bukkit.broadcastMessage("\u00A72{" + deaktiviert.substring(0, deaktiviert.length() - 2) + "\u00A72 }");
        else
            Bukkit.broadcastMessage("\u00A72{ }");
    }

    private void aioffCommand() {
        /*
         * deactivates npc AI, but be wary that npcs still finish their current task.
         * e.g. if an npc has found a destination location and you turn off the ai, the
         * npc will still continue to walk until he's reached his destination.
         */
        for (Quest q : Biomia.getQuestManager().getQuests()) {
            for (NPC n : q.getNpcs()) {
                n.getDefaultGoalController().clear();
            }
        }
        Bukkit.broadcastMessage(QuestMessages.aiOFF);
    }

    private void aionCommand() {
        // activates npc AI
        for (Quest q : Biomia.getQuestManager().getQuests()) {
            for (NPC n : q.getNpcs()) {
                n.getNavigator().getLocalParameters().speedModifier(0.8f);
                n.getDefaultGoalController().addBehavior(WanderGoal.createWithNPC(n), 3);
            }
        }
        Bukkit.broadcastMessage(QuestMessages.aiON);
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
            temp.append("\u00A72<\u00A7a").append(q.getDisplayName()).append("\u00A72>, ");
        }
        if (!temp.toString().equals(""))
            sender.sendMessage("\u00A72{" + temp.substring(0, temp.length() - 2) + "\u00A72}");
        else
            sender.sendMessage("\u00A72{ }");
        temp = new StringBuilder();
        sender.sendMessage(QuestMessages.finishedQuests);
        for (Quest q : (Biomia.getQuestPlayer((Player) sender)).getFinishedQuests()) {
            if (q == null)
                continue;
            temp.append("\u00A72<\u00A7a").append(q.getDisplayName()).append("\u00A72>, ");
        }
        if (!temp.toString().equals(""))
            sender.sendMessage("\u00A72{" + temp.substring(0, temp.length() - 2) + "\u00A72}");
        else
            sender.sendMessage("\u00A72{ }");
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
                sender.sendMessage("\u00A7c/qr <Quest>");
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
                    } catch (NumberFormatException ignored) {}
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
                    && is.getItemMeta().getDisplayName().equals("\u00A7cTagebuch")) {
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
            sender.sendMessage("\u00A7d\u00A7l" + q.getDisplayName());
            if (q.getInfoText() == null)
                sender.sendMessage(QuestMessages.noInformationAboutThisQuest);
            else
                sender.sendMessage("\u00A7a" + q.getInfoText());
            sender.sendMessage("");
            sender.sendMessage("\u00A7d" + QuestMessages.involvedNPCs);
            StringBuilder s = new StringBuilder();
            for (NPC n : q.getNpcs()) {
                if (!n.getName().equals(""))
                    s.append("\u00A7a").append(n.getName()).append(", ");
            }
            if (s.toString().equals(""))
                s = new StringBuilder("\u00A7aKeine.");
            else
                s = new StringBuilder(s.substring(0, s.length() - 2));
            sender.sendMessage(s.toString());
            sender.sendMessage(QuestMessages.dividerLine);
            if (Biomia.getBiomiaPlayer(p).isStaff()) {
                sender.sendMessage("\u00A78ID=" + q.getQuestID() + ", \u00A78Cooldown=" + q.getCooldown()
                        + "s, \u00A78Repeatable=" + q.isRepeatable());
                sender.sendMessage(QuestMessages.dividerLine);
            }
        } else {
            sender.sendMessage("\u00A7c/qinfo <QuestName>");
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