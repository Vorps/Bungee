package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;

public class Pardon {

    @CommandPermission("player")
    public static void pardon(CommandSender author, @CommandParameter("player") String namePlayer) {
        net.vorps.bungee.objects.BanSystem.pardonSystemCommand(author, namePlayer, net.vorps.bungee.objects.BanSystem.TypeBan.BAN);
    }
}
