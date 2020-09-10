package net.vorps.bungee.commands;


import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;

public class PardonChannel {

    public static void pardonchannel(CommandSender author, Player player) {
        net.vorps.bungee.objects.BanSystem.pardonSystemCommand(author, player, net.vorps.bungee.objects.BanSystem.TypeBan.CHANNEL);
    }
}
