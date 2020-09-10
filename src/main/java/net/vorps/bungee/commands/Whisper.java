package net.vorps.bungee.commands;

import net.md_5.bungee.api.chat.TextComponent;
import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.data.Data;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.players.PlayerData;

public class Whisper {

    @CommandPermission("player")
    public static void whisper(CommandSender commandSender, @CommandParameter("PLAYER_EXCEPT_SENDER") Player player, String[] message){
        String message_concat = String.join(" ", message);
        commandSender.sendMessage("CMD.WHISPER_RESEND.SENDER", new Lang.Args(Lang.Parameter.PLAYER, player.getName()), new Lang.Args(Lang.Parameter.MESSAGE, message_concat));
        player.sendMessage("CMD.WHISPER_RESEND.PLAYER", new Lang.Args(Lang.Parameter.PLAYER, commandSender.getName()), new Lang.Args(Lang.Parameter.MESSAGE, message_concat));
        PlayerData.setWhisper_uuid(player.getUUID(), commandSender.getUUID());
    }
}
