package net.vorps.bungee.commands;

import net.vorps.api.commands.*;
import net.vorps.bungee.players.PlayerData;

public class Vanish {

    @CommandPermission(value = "sender", console = false)
    public static void vanish(CommandSender commandSender){
        new CommandsAction("VANISH", commandSender, (e) -> PlayerData.setVanish(commandSender.getUUID(), e), () -> PlayerData.isVanish(commandSender.getUUID())).toggle();
    }

    @CommandPermission(value = "sender", console = false)
    public static void on(CommandSender commandSender){
        new CommandsAction("VANISH", commandSender, (e) -> PlayerData.setVanish(commandSender.getUUID(), e), () -> PlayerData.isVanish(commandSender.getUUID())).on();
    }

    @CommandPermission(value = "sender", console = false)
    public static void off(CommandSender commandSender){
        new CommandsAction("VANISH", commandSender, (e) -> PlayerData.setVanish(commandSender.getUUID(), e), () -> PlayerData.isVanish(commandSender.getUUID())).on();
    }

    @CommandPermission("player")
    public static void vanish(CommandSender commandSender, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player){
        new CommandsAction("VANISH", commandSender, (e) -> PlayerData.setVanish(player.getUUID(), e), () -> PlayerData.isVanish(player.getUUID())).toggle(player);
    }

    @CommandPermission("player")
    public static void on(CommandSender commandSender, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player){
        new CommandsAction("VANISH", commandSender, (e) -> PlayerData.setVanish(player.getUUID(), e), () -> PlayerData.isVanish(player.getUUID())).on(player);
    }

    @CommandPermission("player")
    public static void off(CommandSender commandSender, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player){
        new CommandsAction("VANISH", commandSender, (e) -> PlayerData.setVanish(player.getUUID(), e), () -> PlayerData.isVanish(player.getUUID())).on(player);
    }

}
