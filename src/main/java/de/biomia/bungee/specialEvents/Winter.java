package de.biomia.bungee.specialEvents;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Winter extends Command {

    public Winter(String s) {
        super(s);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender.hasPermission("biomia.winter.winner"))
            if (args.length == 1)
                WinterEvent.randomWin(Integer.valueOf(args[0]));
    }

}
