package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Ban {

    @CommandPermission("player")
    public static void ban(CommandSender author, @CommandParameter("player") String namePlayer, @CommandParameter("time") String time, @CommandParameter("reason") String[] reason) {
        net.vorps.bungee.objects.BanSystem.banSystemCommand(author, namePlayer, time, String.join(" ", reason), net.vorps.bungee.objects.BanSystem.TypeBan.BAN);
    }
    @CommandPermission("no_reason")
    public static void ban(CommandSender author, @CommandParameter("player") String namePlayer, @CommandParameter("time") String time) {
        Ban.ban(author, namePlayer, time);
    }

    @CommandPermission("player")
    public static void def(CommandSender author, @CommandParameter("player") String namePlayer, @CommandParameter("reason") String[] reason) {
        Ban.ban(author, namePlayer, null, reason);
    }

    @CommandPermission("no_reason")
    public static void def(CommandSender author, @CommandParameter("player") String namePlayer) {
        Ban.ban(author, namePlayer, null);
    }

}
