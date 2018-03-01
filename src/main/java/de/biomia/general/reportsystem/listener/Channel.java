package de.biomia.general.reportsystem.listener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.biomia.Main;
import de.biomia.general.reportsystem.PlayerBan;
import org.bukkit.Bukkit;

public class Channel {

    public static void send(ByteArrayDataOutput out) {
        Iterables.getLast(Bukkit.getOnlinePlayers()).sendPluginMessage(Main.plugin, "BiomiaChannel", out.toByteArray());
    }

    public static void send(PlayerBan ban) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("BanTimeReason");
        out.writeInt(ban.getBiomiaPlayer().getBiomiaPlayerID());
        out.writeInt(ban.getToBanID());
        out.writeInt(ban.getTime());
        out.writeUTF(ban.getReason());

        send(out);
    }

}