package de.biomia.bungee.cmds;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Help extends Command {

    public Help(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] arg1) {

        TextComponent biomia = new TextComponent(ChatColor.GOLD + "/biomia " + ChatColor.GREEN + "um Links und die IP von unserem TS zu sehen!");
        biomia.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/biomia"));

        TextComponent hub = new TextComponent(ChatColor.GOLD + "/hub " + ChatColor.GREEN + "um zur Lobby zurückzukehren");
        hub.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/hub"));

        TextComponent register = new TextComponent(ChatColor.GOLD + "/register " + ChatColor.GREEN + "um dich zu registrieren");
        register.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/register"));

        TextComponent coins = new TextComponent(ChatColor.GOLD + "/coins " + ChatColor.GREEN + " um deinen BC-Kontostand abzurufen");
        coins.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/coins"));

        TextComponent party = new TextComponent(ChatColor.GOLD + "/party " + ChatColor.GREEN + "um andere Spieler in deine Party einzuladen");
        party.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/party"));

        TextComponent friend = new TextComponent(ChatColor.GOLD + "/friend " + ChatColor.GREEN + "um Freunde zu adden");
        friend.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/friend"));

        sender.sendMessage(new TextComponent("§8-----------§5Biomia§2Commands§8-----------"));
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(friend);
        sender.sendMessage(hub);
        sender.sendMessage(biomia);
        sender.sendMessage(party);
        sender.sendMessage(register);
        sender.sendMessage(coins);
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent("§8-----------------------------------"));
    }
}
