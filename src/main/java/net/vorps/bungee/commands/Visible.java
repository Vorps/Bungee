package net.vorps.bungee.commands;

import net.vorps.api.commands.*;
import net.vorps.bungee.players.PlayerData;

public class Visible {

    @CommandPermission(value = "sender", console = false)
    public static void visible(CommandSender commandSender){
        new CommandsAction("VISIBLE", commandSender, (e) -> PlayerData.setBuild(commandSender.getUUID(), e), () -> PlayerData.isBuild(commandSender.getUUID())).toggle();
    }

    @CommandPermission(value = "sender", console = false)
    public static void on(CommandSender commandSender){
        new CommandsAction("VISIBLE", commandSender, (e) -> PlayerData.setBuild(commandSender.getUUID(), e), () -> PlayerData.isBuild(commandSender.getUUID())).on();
    }

    @CommandPermission(value = "sender", console = false)
    public static void off(CommandSender commandSender){
        new CommandsAction("VISIBLE", commandSender, (e) -> PlayerData.setBuild(commandSender.getUUID(), e), () -> PlayerData.isBuild(commandSender.getUUID())).off();
    }

    @CommandPermission("player")
    public static void visible(CommandSender commandSender, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player){
        new CommandsAction("VISIBLE", commandSender, (e) -> PlayerData.setBuild(player.getUUID(), e), () -> PlayerData.isBuild(player.getUUID())).toggle(player);
    }

    @CommandPermission("player")
    public static void on(CommandSender commandSender, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player){
        new CommandsAction("VISIBLE", commandSender, (e) -> PlayerData.setBuild(player.getUUID(), e), () -> PlayerData.isBuild(player.getUUID())).on(player);
    }

    @CommandPermission("player")
    public static void off(CommandSender commandSender, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player){
        new CommandsAction("VISIBLE", commandSender, (e) -> PlayerData.setBuild(player.getUUID(), e), () -> PlayerData.isBuild(player.getUUID())).off(player);
    }

}
