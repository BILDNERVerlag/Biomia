package de.biomia.server.freebuild.newhome;

import de.biomia.commands.BiomiaCommand;
import de.biomia.dataManager.MySQL;
import org.bukkit.command.CommandSender;

public class HomeCommands extends BiomiaCommand {

    protected HomeCommands(String command) {
        super(command);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        int biomiaPlayerID = 0;
        switch (getName().toLowerCase()) {
            case "sethome":
                MySQL.executeQuerygetint("SELECT `x` FROM `freebuildHome` where ID = '"+ biomiaPlayerID+ "')", "x", MySQL.Databases.biomia_db);
                MySQL.executeQuerygetint("SELECT `y` FROM `freebuildHome` where ID = '"+ biomiaPlayerID+ "')", "y", MySQL.Databases.biomia_db);
                MySQL.executeQuerygetint("SELECT `z` FROM `freebuildHome` where ID = '"+ biomiaPlayerID+ "')", "z", MySQL.Databases.biomia_db);
                break;
            case "home":
                //check if there is an argument
                    //if there is, check if there is a home with that name
                        //if there is, teleport there
                        //if there is not, error "no home with that name"
                    //if there is not, check if player has multiple homes
                        //if he has none: error
                        //exactly 1: tp there
                        //more than 1: give him a list of possible homes
                break;
            default:
                break;
        }
        return true;
    }

}
