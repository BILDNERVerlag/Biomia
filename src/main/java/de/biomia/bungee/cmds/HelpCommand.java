package de.biomia.bungee.cmds;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class HelpCommand extends Command {

    public HelpCommand(String name) {
        super(name);
    }

    //TODO: move from bungee to spigot?

    @Override
    public void execute(CommandSender sender, String[] arg1) {

        TextComponent biomia = new TextComponent(ChatColor.GRAY + "/" + ChatColor.AQUA + "biomia " + ChatColor.RED + "um Links und die IP von unserem TS zu sehen!");
        biomia.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/biomia"));

        TextComponent hub = new TextComponent(ChatColor.GRAY + "/" + ChatColor.AQUA + "hub " + ChatColor.RED + "um zur Lobby zurückzukehren");
        hub.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/hub"));

        TextComponent register = new TextComponent(ChatColor.GRAY + "/" + ChatColor.AQUA + "register " + ChatColor.RED + "um dich zu registrieren");
        register.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/register"));

        TextComponent coins = new TextComponent(ChatColor.GRAY + "/" + ChatColor.AQUA + "coins " + ChatColor.RED + " um deinen BC-Kontostand abzurufen");
        coins.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/coins"));

        TextComponent party = new TextComponent(ChatColor.GRAY + "/" + ChatColor.AQUA + "party " + ChatColor.RED + "um andere Spieler in deine Party einzuladen");
        party.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/party"));

        TextComponent friend = new TextComponent(ChatColor.GRAY + "/" + ChatColor.AQUA + "friend " + ChatColor.RED + "um Freunde zu adden");
        friend.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/friend"));

        sender.sendMessage(new TextComponent("§7§m-----------§r§7[§cBiomia§bCommands§7]§m-----------"));
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(friend);
        sender.sendMessage(hub);
        sender.sendMessage(biomia);
        sender.sendMessage(party);
        sender.sendMessage(register);
        sender.sendMessage(coins);
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent("§7§m-----------------------------------"));
    }
}
