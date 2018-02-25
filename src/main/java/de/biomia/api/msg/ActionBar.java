package de.biomia.api.msg;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {

	public static void sendActionBarTime(String nachricht, Player p, int a, int b, int c) {
		String s = ChatColor.translateAlternateColorCodes('&', nachricht);
		IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s + "\"}");
		PacketPlayOutTitle titel = new PacketPlayOutTitle(EnumTitleAction.ACTIONBAR, icbc, a, b, c);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(titel);
	}

	public static void sendActionBar(String nachricht, Player p) {
		String s = ChatColor.translateAlternateColorCodes('&', nachricht);
		IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s + "\"}");
		PacketPlayOutTitle titel = new PacketPlayOutTitle(EnumTitleAction.ACTIONBAR, icbc, 0, 20, 0);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(titel);
	}
}