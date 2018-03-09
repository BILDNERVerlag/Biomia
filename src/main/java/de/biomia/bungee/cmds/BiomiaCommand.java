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
        TextComponent Youtube = new TextComponent(ChatColor.AQUA + "Youtube" + ChatColor.RED + ",");
        Youtube.setClickEvent(
                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://youtube.com/channel/UCmu44PNBGvIU-gjtf-jJBsQ"));

        TextComponent Facebook = new TextComponent(ChatColor.AQUA + "Facebook" + ChatColor.RED + ",");
        Facebook.setClickEvent(
                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://facebook.com/Biomia-293893057667561"));

        TextComponent Internetseite = new TextComponent(
                ChatColor.RED + "unserer " + ChatColor.AQUA + "Internetseite" + ChatColor.RED + " oder sogar");
        Internetseite.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://biomia.de"));

        TextComponent TS = new TextComponent(
                ChatColor.RED + "auf unserem " + ChatColor.AQUA + "TeamSpeak" + ChatColor.RED + "!");
        TS.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "ts.biomia.de"));

        sender.sendMessage(new TextComponent("§7§m------------§r§7[§cBiomia§bHilfe§7]§m-------------"));
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent(ChatColor.RED + "Besucht uns auf"));
        sender.sendMessage(Facebook);
        sender.sendMessage(Youtube);
        sender.sendMessage(Internetseite);
        sender.sendMessage(TS);
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent("§7§m-----------------------------------"));
    }

}
