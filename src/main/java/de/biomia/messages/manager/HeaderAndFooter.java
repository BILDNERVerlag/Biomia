package de.biomia.messages.manager;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class HeaderAndFooter {

	public static void sendHeaderAndFooter(Player p, String header, String footer) {
		if (footer == null)
			footer = "";
		if (header == null)
			header = "";

		IChatBaseComponent tabHeader = ChatSerializer.a("{\"text\":\"" + header + "\"}");
		IChatBaseComponent tabFooter = ChatSerializer.a("{\"text\":\"" + footer + "\"}");

		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

		try {
			Field fa = packet.getClass().getDeclaredField("a");
			Field fb = packet.getClass().getDeclaredField("b");
			fa.setAccessible(true);
			fa.set(packet, tabHeader);
			fa.setAccessible(false);
			fb.setAccessible(true);
			fb.set(packet, tabFooter);
			fb.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);

	}

}
