package net.vorps.bungee.commands;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.data.Data;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.players.PlayerData;

public class Nickname {

    @CommandPermission(value = "sender", console = false)
    public static void set(CommandSender sender,  @CommandParameter("nickname") String nickname ){
        PlayerData.setNickName(Data.getUUIDPlayer(sender.getName()), nickname);
        PlayerData.getPlayerData(sender.getName()).setNickName(nickname);
        sender.sendMessage(Lang.getMessage("CMD.NICKNAME.SET.SENDER", sender.getLang(), new Lang.Args(Lang.Parameter.VAR, nickname)));
    }

    @CommandPermission("player")
    public static void set(CommandSender sender, @CommandParameter("player") String player, @CommandParameter("nickname") String nickname){
        if(Data.isPlayer(player)){
            sender.sendMessage(Lang.getMessage("CMD.NICKNAME.SET.PLAYER.SENDER", sender.getLang(), new Lang.Args(Lang.Parameter.PLAYER, player), new Lang.Args(Lang.Parameter.VAR, nickname)));
            PlayerData.setNickName(Data.getUUIDPlayer(player), nickname);
            if(PlayerData.isPlayerDataCore(player)) {
                PlayerData playerData = PlayerData.getPlayerData(player);
                playerData.setNickName(nickname);
                playerData.getProxiedPlayer().sendMessage(new TextComponent(Lang.getMessage("CMD.NICKNAME.SET.PLAYER", PlayerData.getPlayerData(player).getLang(), new Lang.Args(Lang.Parameter.PLAYER, sender.getName()), new Lang.Args(Lang.Parameter.VAR, nickname))));
            } else
                net.vorps.api.players.PlayerData.addNotification(Data.getUUIDPlayer(player), Lang.getMessage("CMD.NICKNAME.SET.PLAYER", net.vorps.api.players.PlayerData.getLang(Data.getUUIDPlayer(player)), new Lang.Args(Lang.Parameter.PLAYER, sender.getName()), new Lang.Args(Lang.Parameter.VAR, nickname)));
        } else sender.sendMessage(Lang.getMessage("CMD.ERROR.DONT_KNOW_PLAYER", sender.getLang(), new Lang.Args(Lang.Parameter.PLAYER, player)));
    }
    @CommandPermission(value = "sender", console = false)
    public static void reset(CommandSender sender){
        if(!PlayerData.getPlayerData(sender.getName()).getNickName().equals(sender.getName())){
            PlayerData.setNickName(Data.getUUIDPlayer(sender.getName()), sender.getName());
            PlayerData.getPlayerData(sender.getName()).setNickName(sender.getName());
            sender.sendMessage(Lang.getMessage("CMD.NICKNAME.RESET.SENDER", PlayerData.getPlayerData(sender.getName()).getLang()));
        } else sender.sendMessage(Lang.getMessage(" CMD.NICKNAME.RESET.SENDER.ERROR", PlayerData.getPlayerData(sender.getName()).getLang()));
    }

    @CommandPermission("player")
    public static void reset(CommandSender sender, @CommandParameter("player") String player){
        if(Data.isPlayer(player)){
            if(PlayerData.getNickName(Data.getUUIDPlayer(player)).equals(player)){
                sender.sendMessage(Lang.getMessage("CMD.NICKNAME.RESET.PLAYER.SENDER", sender.getLang(), new Lang.Args(Lang.Parameter.PLAYER, player)));
                PlayerData.setNickName(Data.getUUIDPlayer(player), player);
                if(PlayerData.isPlayerDataCore(player)) {
                    PlayerData playerData = PlayerData.getPlayerData(player);
                    playerData.setNickName(player);
                    playerData.getProxiedPlayer().sendMessage(new TextComponent(Lang.getMessage("CMD.NICKNAME.RESET.PLAYER", playerData.getLang(), new Lang.Args(Lang.Parameter.PLAYER, sender.getName()))));
                } else net.vorps.api.players.PlayerData.addNotification(Data.getUUIDPlayer(player), Lang.getMessage("CMD.NICKNAME.RESET.PLAYER", net.vorps.api.players.PlayerData.getLang(Data.getUUIDPlayer(player)), new Lang.Args(Lang.Parameter.PLAYER, sender.getName())));
            } else sender.sendMessage(Lang.getMessage("CMD.NICKNAME.RESET.PLAYER.ERROR", sender.getLang(), new Lang.Args(Lang.Parameter.PLAYER, player)));
        } else sender.sendMessage(Lang.getMessage("CMD.ERROR.DONT_KNOW_PLAYER", sender.getLang(), new Lang.Args(Lang.Parameter.PLAYER, player)));

    }
}
