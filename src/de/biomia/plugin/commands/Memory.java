package de.biomia.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.NumberFormat;

public class Memory implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {


        if (sender.hasPermission("biomia.workload")) {

            Runtime runtime = Runtime.getRuntime();

            NumberFormat format = NumberFormat.getInstance();

            StringBuilder sb = new StringBuilder();
            long maxMemory = runtime.maxMemory();
            long allocatedMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();

            sb.append("�cAktuelle free memory: �6").append(format.format(freeMemory)).append("mb\n");
            sb.append("�cAllocated memory: �6").append(format.format(allocatedMemory)).append("mb\n");
            sb.append("�cMax memory: �6").append(format.format(maxMemory)).append("mb\n");
            sb.append("�cTotal free memory: �6").append(format.format((freeMemory + (maxMemory - allocatedMemory)))).append("mb");
            sender.sendMessage(sb.toString());
        }


        return false;
    }

}
