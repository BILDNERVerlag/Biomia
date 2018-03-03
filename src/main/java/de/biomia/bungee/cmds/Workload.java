package de.biomia.bungee.cmds;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.text.NumberFormat;

public class Workload extends Command {

    public Workload(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] arg1) {

        if (sender.hasPermission("biomia.workload")) {

            Runtime runtime = Runtime.getRuntime();

            NumberFormat format = NumberFormat.getInstance();

            StringBuilder sb = new StringBuilder();
            long maxMemory = runtime.maxMemory();
            long allocatedMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();


            sb.append("aktuell freie memory: ").append(format.format(freeMemory)).append("mb\n");
            sb.append("aktuell zugewiesene memory: ").append(format.format(allocatedMemory)).append("mb\n");
            sb.append("max. memory: ").append(format.format(maxMemory)).append("mb\n");
            sb.append("insgesammt freier memory: ").append(format.format((freeMemory + (maxMemory - allocatedMemory)))).append("mb");
            sender.sendMessage(new TextComponent(sb.toString()));
        }
    }
}
