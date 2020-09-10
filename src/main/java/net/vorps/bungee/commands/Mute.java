package net.vorps.bungee.commands;


import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.objects.BanSystem;
import net.vorps.bungee.players.PlayerData;

public class Mute {

    @CommandPermission("player")
    public static void mute(CommandSender author, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player, @CommandParameter("time") String time, @CommandParameter("reason") String[] reason) {
        BanSystem banSystem = net.vorps.bungee.objects.BanSystem.banSystemCommand(author, player, time, reason.length == 0 ? null :  String.join(" ", reason), net.vorps.bungee.objects.BanSystem.TypeBan.MUTE);
        if(banSystem != null) player.sendMessage("CMD.BAN_SYSTEM.PLAYER.SHOW", new Lang.Args(Lang.Parameter.MESSAGE, banSystem.toString(PlayerData.getLang(player.getUUID()))));
    }
    @CommandPermission("no_reason")
    public static void mute(CommandSender author, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player, @CommandParameter("time") String time) {
        Mute.mute(author, player, time, new String[] {});
    }

    @CommandPermission("player")
    public static void def(CommandSender author, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player, @CommandParameter("reason") String[] reason) {
        Mute.mute(author, player, null, reason);
    }

    @CommandPermission("no_reason")
    public static void def(CommandSender author, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player) {
        Mute.mute(author, player, null, new String[] {});
    }
}
