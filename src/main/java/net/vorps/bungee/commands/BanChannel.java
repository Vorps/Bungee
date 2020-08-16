package net.vorps.bungee.commands;


import net.vorps.api.commands.CommandSender;

public class BanChannel {

    public static void banchannel(CommandSender author, String namePlayer, String time, String reason) {
        net.vorps.bungee.objects.BanSystem.banSystemCommand(author, namePlayer, time, reason, net.vorps.bungee.objects.BanSystem.TypeBan.CHANNEL);
    }

    public static void banchannel(CommandSender author, String namePlayer, String time) {
        BanChannel.banchannel(author, namePlayer, time, "");
    }

    public static void banchannel(CommandSender author, String namePlayer) {
        BanChannel.banchannel(author, namePlayer, null, "");
    }
}
