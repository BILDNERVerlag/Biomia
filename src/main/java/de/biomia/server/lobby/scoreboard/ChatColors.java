package de.biomia.server.lobby.scoreboard;

import de.biomia.tools.RankManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class ChatColors implements Listener {

    public static String getGroupName(Player p) {

        String rankName = RankManager.getRank(p);
        if (rankName.contains("Premium")) {
            rankName = rankName.substring(7);
            switch (rankName) {
            case "Eins":
                rankName = "Premium I";
                break;
            case "Zwei":
                rankName = "Premium II";
                break;
            case "Drei":
                rankName = "Premium III";
                break;
            case "Vier":
                rankName = "Premium IV";
                break;
            case "Fuenf":
                rankName = "Premium V";
                break;
            case "Sechs":
                rankName = "Premium VI";
                break;
            case "Sieben":
                rankName = "Premium VII";
                break;
            case "Acht":
                rankName = "Premium VIII";
                break;
            case "Neun":
                rankName = "Premium IX";
                break;
            case "Zehn":
                rankName = "Premium X";
                break;
            case "Reg":
                rankName = "Spieler";
                break;
            case "Unreg":
                rankName = "Registriert";
                break;
            }
        }
        return rankName;
    }

    public static void sendRegMsg(Player p) {
        if (RankManager.getRank(p).equals("UnregSpieler")) {
            TextComponent register = new TextComponent();
            p.sendMessage(ChatColor.DARK_PURPLE + "Du bist noch nicht registriert!");
            register.setText(ChatColor.BLUE + "Registriere dich jetzt auf: " + ChatColor.GRAY + "www."
                    + ChatColor.DARK_PURPLE + "Bio" + ChatColor.DARK_GREEN + "mia"
                    + ChatColor.GRAY + ".de");
            register.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://biomia.de"));
            p.spigot().sendMessage(register);
            p.sendMessage(ChatColor.GRAY + "Oder sp\u00fcter mit " + ChatColor.GOLD + "/register");
        }

    }

}
