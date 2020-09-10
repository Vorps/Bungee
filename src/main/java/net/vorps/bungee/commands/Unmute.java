package net.vorps.bungee.commands;

import net.vorps.api.commands.*;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.objects.BanSystem;
import net.vorps.bungee.objects.KickSystem;

public class Unmute {

    @CommandPermission("player")
    public static void unmute(CommandSender author, @CommandParameter("PLAYER_UNMUTE") Player player) {
        net.vorps.bungee.objects.BanSystem.pardonSystemCommand(author, player, BanSystem.TypeBan.MUTE);
    }

}
