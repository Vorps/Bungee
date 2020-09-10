package net.vorps.bungee.commands;

import net.vorps.api.commands.*;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.objects.BanSystem;
import net.vorps.bungee.objects.KickSystem;

public class Pardon {

    @CommandPermission("player")
    public static void pardon(CommandSender author, @CommandParameter("PLAYER_PARDON") Player player) {
        net.vorps.bungee.objects.BanSystem.pardonSystemCommand(author, player, net.vorps.bungee.objects.BanSystem.TypeBan.BAN);
    }


}
