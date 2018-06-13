package de.biomia.bungee.cmds;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class BiomiaCommand extends Command {

    public BiomiaCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent Youtube = new TextComponent(String.format("%sYoutube%s,", ChatColor.AQUA, ChatColor.RED));
        Youtube.setClickEvent(
                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://youtube.com/channel/UCmu44PNBGvIU-gjtf-jJBsQ"));

        TextComponent Facebook = new TextComponent(String.format("%sFacebook%s,", ChatColor.AQUA, ChatColor.RED));
        Facebook.setClickEvent(
                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://facebook.com/Biomia-293893057667561"));

        TextComponent Internetseite = new TextComponent(
                String.format("%sder %sBIOMIA-Website%s,", ChatColor.RED, ChatColor.AQUA, ChatColor.RED));
        Internetseite.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://biomia.de"));

        TextComponent TS = new TextComponent(
                String.format("%sunserem %sTeamSpeak%s oder", ChatColor.RED, ChatColor.AQUA, ChatColor.RED));
        TS.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "ts.biomia.de"));

        TextComponent Discord = new TextComponent(
                String.format("%sunserem %sDiscord%s!", ChatColor.RED, ChatColor.AQUA, ChatColor.RED));
        Discord.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/DUDJQdQ"));

        sender.sendMessage(new TextComponent("§7§m------------§r§7[§cBiomia§bHilfe§7]§m-------------"));
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent(ChatColor.RED + "Besucht uns auf"));
        sender.sendMessage(Facebook);
        sender.sendMessage(Youtube);
        sender.sendMessage(Internetseite);
        sender.sendMessage(TS);
        sender.sendMessage(Discord);
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent("§7§m-----------------------------------"));
    }
}
