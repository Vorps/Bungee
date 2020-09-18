package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.Bungee;

public class Ping {

    @CommandPermission(value = "sender", console = false)
    public static void ping(CommandSender commandSender){
        commandSender.sendMessage("CMD.PING.SENDER", new Lang.Args(Lang.Parameter.VAR, Bungee.getInstance().getProxy().getPlayer(commandSender.getUUID()).getPing()+""));
    }

    @CommandPermission(value = "player")
    public static void ping(CommandSender commandSender, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player){
        commandSender.sendMessage("CMD.PING.PLAYER", new Lang.Args(Lang.Parameter.PLAYER, player.getName()), new Lang.Args(Lang.Parameter.VAR, Bungee.getInstance().getProxy().getPlayer(player.getUUID()).getPing()+""));
    }
}
