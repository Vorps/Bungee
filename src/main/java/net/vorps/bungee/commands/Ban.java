package net.vorps.bungee.commands;

import net.md_5.bungee.api.chat.TextComponent;
import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.Bungee;
import net.vorps.bungee.objects.BanSystem;
import net.vorps.bungee.players.PlayerData;

public class Ban {

    @CommandPermission("player")
    public static void ban(CommandSender author, @CommandParameter(value = "PLAYER_EXCEPT_SENDER") Player player, @CommandParameter("time") String time, @CommandParameter("reason") String[] reason) {
        BanSystem banSystem = net.vorps.bungee.objects.BanSystem.banSystemCommand(author, player, time, String.join(" ", reason), net.vorps.bungee.objects.BanSystem.TypeBan.BAN);
        if(banSystem != null && PlayerData.isPlayerDataCore(player.getUUID()))
            Bungee.getInstance().getProxy().getPlayer(player.getUUID()).disconnect(new TextComponent(Lang.getMessage("CMD.BAN_SYSTEM.PLAYER.SHOW", PlayerData.getLang(player.getUUID()), new Lang.Args(Lang.Parameter.MESSAGE, banSystem.toString(PlayerData.getLang(player.getUUID()))))));
    }
    @CommandPermission("no_reason")
    public static void ban(CommandSender author, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player, @CommandParameter("time") String time) {
        Ban.ban(author, player, time, new String[] {});
    }

    @CommandPermission("player")
    public static void def(CommandSender author, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player, @CommandParameter("reason") String[] reason) {
        Ban.ban(author, player, null, reason);
    }

    @CommandPermission("no_reason")
    public static void def(CommandSender author, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player) {
        Ban.ban(author, player, null, new String[] {});
    }

}
