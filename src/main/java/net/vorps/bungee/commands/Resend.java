package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.data.Data;
import net.vorps.bungee.players.PlayerData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class Resend {

    @CommandPermission(value = "player", console = false)
    public static void resend(CommandSender commandSender, String[] message){
        UUID whisper_uuid = PlayerData.getWhisper_uuid(commandSender.getUUID());
        if(whisper_uuid != null) {
            ArrayList<String> args = new ArrayList<>(Collections.singleton(Data.getNamePlayer(whisper_uuid)));
            args.addAll(Arrays.asList(message));
            CommandManager.WHISPER.getCommand().execute(commandSender, args.toArray(String[]::new));
            PlayerData.setWhisper_uuid(commandSender.getUUID(), null);
        }
        else commandSender.sendMessage("CMD.RESEND.ERROR");
    }
}
