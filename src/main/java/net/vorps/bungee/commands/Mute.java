package net.vorps.bungee.commands;


import net.vorps.api.commands.CommandSender;

public class Mute {

    public static void mute(CommandSender author, String namePlayer, String time, String reason) {
        net.vorps.bungee.objects.BanSystem.banSystemCommand(author, namePlayer, time, reason, net.vorps.bungee.objects.BanSystem.TypeBan.MUTE);
    }

    public static void mute(CommandSender author, String namePlayer, String time) {
        System.out.println("Test");
        Mute.mute(author, namePlayer, time, "");
    }

    public static void mute(CommandSender author, String namePlayer) {
        Mute.mute(author, namePlayer, null, "");
    }
}
