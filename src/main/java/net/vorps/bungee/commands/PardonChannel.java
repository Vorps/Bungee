package net.vorps.bungee.commands;


import net.vorps.api.commands.CommandSender;

public class PardonChannel {

    public static void pardonchannel(CommandSender author, String namePlayer) {
        net.vorps.bungee.objects.BanSystem.pardonSystemCommand(author, namePlayer, net.vorps.bungee.objects.BanSystem.TypeBan.CHANNEL);
    }
}
