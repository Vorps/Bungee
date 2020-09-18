package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.players.PlayerData;

public class Rank {

    @CommandPermission(value = "sender", console = false)
    public static void rank(CommandSender commandSender, @CommandParameter("rank") String rank){
        PlayerData.setRank(commandSender.getUUID(), net.vorps.api.objects.Rank.getRank(rank));
        commandSender.sendMessage("CMD.RANK.SENDER",  new Lang.Args(Lang.Parameter.RANK, rank));
    }

    @CommandPermission("player")
    public static void rank(CommandSender commandSender, @CommandParameter("rank") String rank, @CommandParameter("player") Player player){
        PlayerData.setRank(player.getUUID(), net.vorps.api.objects.Rank.getRank(rank));
        commandSender.sendMessage("CMD.RANK.PLAYER.SENDER",  new Lang.Args(Lang.Parameter.PLAYER, player.getName()), new Lang.Args(Lang.Parameter.RANK, rank));
        player.sendMessage("CMD.RANK.PLAYER" , new Lang.Args(Lang.Parameter.PLAYER, commandSender.getName()), new Lang.Args(Lang.Parameter.RANK, rank));
    }
}
