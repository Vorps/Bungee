package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.players.PlayerData;

public class Nickname {

    @CommandPermission(value = "sender", console = false)
    public static void set(CommandSender sender,  @CommandParameter("nickname") String nickname ){
        PlayerData.setNickName(sender.getUUID(), nickname);;
        sender.sendMessage("CMD.NICKNAME.SET.SENDER", new Lang.Args(Lang.Parameter.VAR, nickname));
    }

    @CommandPermission("player")
    public static void set(CommandSender sender, @CommandParameter("player") Player player, @CommandParameter("nickname") String nickname){
        sender.sendMessage("CMD.NICKNAME.SET.PLAYER.SENDER", new Lang.Args(Lang.Parameter.PLAYER, player.getName()), new Lang.Args(Lang.Parameter.VAR, nickname));
        PlayerData.setNickName(player.getUUID(), nickname);
        player.sendMessage("CMD.NICKNAME.SET.PLAYER", new Lang.Args(Lang.Parameter.PLAYER, sender.getName()), new Lang.Args(Lang.Parameter.VAR, nickname));
    }

    @CommandPermission(value = "sender", console = false)
    public static void reset(CommandSender sender){
        if(!PlayerData.getNickName(sender.getUUID()).equals(sender.getName())){
            PlayerData.setNickName(sender.getUUID(), sender.getName());
            sender.sendMessage("CMD.NICKNAME.RESET.SENDER");
        } else sender.sendMessage("CMD.NICKNAME.RESET.SENDER.ERROR");
    }

    @CommandPermission("player")
    public static void reset(CommandSender sender, @CommandParameter("player") Player player){
        if(PlayerData.getNickName(player.getUUID()).equals(player.getName())){
            sender.sendMessage("CMD.NICKNAME.RESET.PLAYER.SENDER", new Lang.Args(Lang.Parameter.PLAYER, player.getName()));
            PlayerData.setNickName(player.getUUID(), player.getName());
            player.sendMessage("CMD.NICKNAME.RESET.PLAYER", new Lang.Args(Lang.Parameter.PLAYER, sender.getName()));
        } else sender.sendMessage("CMD.NICKNAME.RESET.PLAYER.ERROR", new Lang.Args(Lang.Parameter.PLAYER, player.getName()));
    }
}
