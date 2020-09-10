package net.vorps.bungee.commands;


import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;

public class BanChannel {

    public static void banchannel(CommandSender author, Player player, String time, String reason) {
        net.vorps.bungee.objects.BanSystem.banSystemCommand(author, player, time, reason, net.vorps.bungee.objects.BanSystem.TypeBan.CHANNEL);
    }

    public static void banchannel(CommandSender author, Player player, String time) {
        BanChannel.banchannel(author, player, time, "");
    }

    public static void banchannel(CommandSender author, Player player) {
        BanChannel.banchannel(author, player, null, "");
    }
}
