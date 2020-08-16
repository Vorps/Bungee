package net.vorps.bungee.commands;


import net.vorps.api.commands.CommandSender;

public class Unmute {

    public static void unmute(CommandSender author, String namePlayer) {
        net.vorps.bungee.objects.BanSystem.pardonSystemCommand(author, namePlayer, net.vorps.bungee.objects.BanSystem.TypeBan.MUTE);
    }
}
