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
        TextComponent Youtube = new TextComponent(ChatColor.GOLD + "Youtube" + ChatColor.GREEN + ",");
        Youtube.setClickEvent(
                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://youtube.com/channel/UCmu44PNBGvIU-gjtf-jJBsQ"));

        TextComponent Facebook = new TextComponent(ChatColor.GOLD + "Facebook" + ChatColor.GREEN + ",");
        Facebook.setClickEvent(
                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://facebook.com/Biomia-293893057667561"));

        TextComponent Internetseite = new TextComponent(
                ChatColor.GREEN + "unserer " + ChatColor.GOLD + "Internetseite" + ChatColor.GREEN + " oder sogar");
        Internetseite.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://biomia.de"));

        TextComponent TS = new TextComponent(
                ChatColor.GREEN + "auf unserem " + ChatColor.GOLD + "TeamSpeak" + ChatColor.GREEN + "!");
        TS.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "ts.biomia.de"));

        sender.sendMessage(new TextComponent("§8------------§5Biomia§2Infos§8----------"));
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent(ChatColor.GREEN + "Besucht uns auf"));
        sender.sendMessage(Facebook);
        sender.sendMessage(Youtube);
        sender.sendMessage(Internetseite);
        sender.sendMessage(TS);
        sender.sendMessage(new TextComponent(""));
        sender.sendMessage(new TextComponent("§8-------------------------------"));
    }

}
