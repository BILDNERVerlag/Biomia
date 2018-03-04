package de.biomia.spigot.messages.manager;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Title {

	public static void sendTitel(String nachricht, Player p) {

		String s = ChatColor.translateAlternateColorCodes('&', nachricht);
		IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s + "\"}");
		PacketPlayOutTitle titel = new PacketPlayOutTitle(EnumTitleAction.TITLE, icbc, 0, 20, 0);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(titel);
	}

	public static void sendSubTitel(String nachricht, Player p) {

		String s = ChatColor.translateAlternateColorCodes('&', nachricht);
		IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s + "\"}");
		PacketPlayOutTitle titel = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, icbc, 0, 20, 0);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(titel);
	}

	public static void sendTitelTime(String nachricht, Player p, int a, int b, int c) {

		String s = ChatColor.translateAlternateColorCodes('&', nachricht);
		IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s + "\"}");
		PacketPlayOutTitle titel = new PacketPlayOutTitle(EnumTitleAction.TITLE, icbc, a, b, c);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(titel);
	}

	public static void sendSubTitelTime(String nachricht, Player p, int a, int b, int c) {
		String s = ChatColor.translateAlternateColorCodes('&', nachricht);
		IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s + "\"}");
		PacketPlayOutTitle titel = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, icbc, a, b, c);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(titel);
	}
}
