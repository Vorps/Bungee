package net.vorps.bungee.commands;


import net.md_5.bungee.api.chat.TextComponent;
import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.data.Data;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.Bungee;
import net.vorps.bungee.objects.KickSystem;
import net.vorps.bungee.players.PlayerData;

/**
 * Project Bungee Created by Vorps on 03/03/2017 at 15:59.
 */
public class Kick{

    @CommandPermission("player")
    public static void kick(CommandSender author, @CommandParameter("PLAYER_ONLINE_EXCEPT_SENDER") Player player, @CommandParameter("reason") String[] reason) {
        System.out.println("OKKKKK");
        KickSystem kickSystem = new KickSystem(author.getUUID(), player.getUUID(), String.join(" ", reason), KickSystem.TypeBan.KICK);
        Bungee.getInstance().getProxy().getPlayer(player.getUUID()).disconnect(new TextComponent(Lang.getMessage("CMD.BAN_SYSTEM.PLAYER.SHOW", PlayerData.getLang(player.getUUID()), new Lang.Args(Lang.Parameter.MESSAGE, kickSystem.toString(PlayerData.getLang(player.getUUID()))))));
    }

    @CommandPermission("no_reason")
    public static void kick(CommandSender author, @CommandParameter("PLAYER_ONLINE_EXCEPT_SENDER") Player player) {
        Kick.kick(author, player, new String[]{});
    }

}
