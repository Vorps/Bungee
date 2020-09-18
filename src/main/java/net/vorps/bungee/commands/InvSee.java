package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.bungee.Bungee;
import net.vorps.bungee.channel.ChannelManager;

public class InvSee {

    @CommandPermission(value = "player", console = false)
    public static void invsee(CommandSender commandSender, @CommandParameter("PLAYER_SAME_SERVER") Player player){
        new ChannelManager().setChannel("BungeeCord").setSubChannel("INVENTORY_SEE").addValues(player.getUUID()).send(Bungee.getInstance(), commandSender.getUUID());
    }
}
