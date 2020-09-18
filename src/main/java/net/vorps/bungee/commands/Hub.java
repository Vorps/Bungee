package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;

public class Hub {

    @CommandPermission(value = "sender", console = false)
    public static void hub(CommandSender commandSender, @CommandParameter("HUB_NUMBER") String number){
        Connect.connect(commandSender, "HUB_"+number);
    }

    @CommandPermission(value = "player")
    public static void hub(CommandSender commandSender, @CommandParameter("HUB_NUMBER") String number, @CommandParameter("PLAYER_ONLINE") Player player){
        Connect.connect(commandSender, "HUB_"+number, player);
    }
}
