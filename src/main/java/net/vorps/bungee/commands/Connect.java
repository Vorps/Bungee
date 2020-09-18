package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.Bungee;
import net.vorps.dispatcher.Dispatcher;
import net.vorps.dispatcher.Server;

public class Connect {

    @CommandPermission(value = "sender", console = false)
    public static void connect(CommandSender commandSender, @CommandParameter("SERVER") String server){
        if(Dispatcher.connectServer(Server.getServer(server)))
            Bungee.getInstance().getProxy().getPlayer(commandSender.getUUID()).connect(Bungee.getInstance().getProxy().getServerInfo(server));
        else commandSender.sendMessage("CMD.CONNECT.ERROR", new Lang.Args(Lang.Parameter.VAR, server));
    }

    @CommandPermission(value = "player")
    public static void connect(CommandSender commandSender, @CommandParameter("SERVER") String server, @CommandParameter("PLAYER_ONLINE") Player player){
        if(Dispatcher.connectServer(Server.getServer(server))) {
            Bungee.getInstance().getProxy().getPlayer(player.getUUID()).connect(Bungee.getInstance().getProxy().getServerInfo(server));
            commandSender.sendMessage("CMD.CONNECT.SENDER", new Lang.Args(Lang.Parameter.PLAYER, player.getName()), new Lang.Args(Lang.Parameter.VAR, server));
            player.sendMessage("CMD.CONNECT.PLAYER", new Lang.Args(Lang.Parameter.PLAYER, commandSender.getName()), new Lang.Args(Lang.Parameter.VAR, server));
        }
        else commandSender.sendMessage("CMD.CONNECT.ERROR", new Lang.Args(Lang.Parameter.VAR, server));
    }
}
