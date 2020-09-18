package net.vorps.bungee.commands;

import net.vorps.api.commands.*;
import net.vorps.bungee.players.PlayerData;

public class Fly {

    @CommandPermission(value = "sender", console = false)
    public static void fly(CommandSender commandSender){
        new CommandsAction("FLY", commandSender, (e) -> PlayerData.setFly(commandSender.getUUID(), e), () -> PlayerData.isFly(commandSender.getUUID())).toggle();
    }

    @CommandPermission(value = "sender", console = false)
    public static void on(CommandSender commandSender){
        new CommandsAction("FLY", commandSender, (e) -> PlayerData.setFly(commandSender.getUUID(), e), () -> PlayerData.isFly(commandSender.getUUID())).on();
    }

    @CommandPermission(value = "sender", console = false)
    public static void off(CommandSender commandSender){
        new CommandsAction("FLY", commandSender, (e) -> PlayerData.setFly(commandSender.getUUID(), e), () -> PlayerData.isFly(commandSender.getUUID())).off();
    }

    @CommandPermission("player")
    public static void fly(CommandSender commandSender, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player){
        new CommandsAction("FLY", commandSender, (e) -> PlayerData.setFly(player.getUUID(), e), () -> PlayerData.isFly(player.getUUID())).toggle(player);
    }

    @CommandPermission("player")
    public static void on(CommandSender commandSender, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player){
        new CommandsAction("FLY", commandSender, (e) -> PlayerData.setFly(player.getUUID(), e), () -> PlayerData.isFly(player.getUUID())).on(player);
    }

    @CommandPermission("player")
    public static void off(CommandSender commandSender, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player){
        new CommandsAction("FLY", commandSender, (e) -> PlayerData.setFly(player.getUUID(), e), () -> PlayerData.isFly(player.getUUID())).off(player);
    }
}
