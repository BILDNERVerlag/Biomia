package de.biomia.bungee.cmds;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.text.NumberFormat;

public class WorkloadCommand extends Command {

    public WorkloadCommand(String name) {
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


            sb.append("§cfree memory: §b").append(format.format(freeMemory)).append("mb\n");
            sb.append("§callocated memory: §b").append(format.format(allocatedMemory)).append("mb\n");
            sb.append("§cmax memory: §b").append(format.format(maxMemory)).append("mb\n");
            sender.sendMessage(new TextComponent(sb.toString()));
        }
    }
}
