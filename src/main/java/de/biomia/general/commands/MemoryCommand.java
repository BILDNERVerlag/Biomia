package de.biomia.general.commands;

import org.bukkit.command.CommandSender;

import java.text.NumberFormat;

public class MemoryCommand extends BiomiaCommand {

    public MemoryCommand() {
        super("memory");
    }

    @Override
    public boolean execute(CommandSender sender, String arg2, String[] args) {

        if (sender.hasPermission("biomia.workload")) {

            Runtime runtime = Runtime.getRuntime();

            NumberFormat format = NumberFormat.getInstance();

            StringBuilder sb = new StringBuilder();
            long maxMemory = runtime.maxMemory();
            long allocatedMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();

            sb.append("\u00A7cAktuelle free memory: \u00A76").append(format.format(freeMemory)).append("mb\n");
            sb.append("\u00A7cAllocated memory: \u00A76").append(format.format(allocatedMemory)).append("mb\n");
            sb.append("\u00A7cMax memory: \u00A76").append(format.format(maxMemory)).append("mb\n");
            sb.append("\u00A7cTotal free memory: \u00A76").append(format.format((freeMemory + (maxMemory - allocatedMemory)))).append("mb");
            sender.sendMessage(sb.toString());
        }

        return false;
    }

}
