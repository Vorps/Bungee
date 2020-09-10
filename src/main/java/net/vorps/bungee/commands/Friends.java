package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.data.Data;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.players.PlayerData;


public class Friends {

    @CommandPermission(value = "add", console = false)
    public static void add(CommandSender sender, @CommandParameter("player") Player player) {
            net.vorps.bungee.objects.Friends friends = PlayerData.getFriends(sender.getUUID());
            friends.addMember(player.getUUID());
    }

}