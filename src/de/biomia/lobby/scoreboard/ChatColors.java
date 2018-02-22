package de.biomia.lobby.scoreboard;

import de.biomiaAPI.pex.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class ChatColors implements Listener {

    public static String getGroupName(Player p) {

        String rankName = Rank.getRank(p);
        if (rankName.contains("Eins")) {
            rankName = "Premium I";
        }
        if (rankName.contains("Zwei")) {
            rankName = "Premium II";
        }
        if (rankName.contains("Drei")) {
            rankName = "Premium III";
        }
        if (rankName.contains("Vier")) {
            rankName = "Premium IV";
        }
        if (rankName.contains("Fuenf")) {
            rankName = "Premium V";
        }
        if (rankName.contains("Sechs")) {
            rankName = "Premium VI";
        }
        if (rankName.contains("Sieben")) {
            rankName = "Premium VII";
        }
        if (rankName.contains("Acht")) {
            rankName = "Premium VIII";
        }
        if (rankName.contains("Neun")) {
            rankName = "Premium IX";
        }
        if (rankName.contains("Zehn")) {
            rankName = "Premium X";
        }
        if (rankName.contains("Reg")) {
            rankName = "Spieler";
        }
        if (rankName.contains("Unreg")) {
            rankName = "Registriert";
        }
        return rankName;
    }

    public static void sendRegMsg(Player p) {
        if (Rank.getRank(p).equals("UnregSpieler")) {
            TextComponent register = new TextComponent();
            p.sendMessage(ChatColor.DARK_PURPLE + "Du bist noch nicht registriert!");
            register.setText(ChatColor.BLUE + "Registriere dich jetzt auf: " + ChatColor.GRAY + "www."
                    + ChatColor.DARK_PURPLE + "Bio" + ChatColor.DARK_GREEN + "mia"
                    + ChatColor.GRAY + ".de");
            register.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://biomia.de"));
            p.spigot().sendMessage(register);
            p.sendMessage(ChatColor.GRAY + "Oder später mit " + ChatColor.GOLD + "/register");
        }

    }

}
