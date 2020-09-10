package net.vorps.bungee.commands;

import lombok.Getter;
import net.vorps.api.commands.*;
import net.vorps.api.data.DataReload;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.Bungee;
import net.vorps.bungee.DataBungee;
import net.vorps.bungee.objects.BanSystem;
import net.vorps.bungee.objects.KickSystem;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum CommandManager {
    NICKNAME(new Command("nickname", 0, Nickname.class)),
    PARDON(new Command("pardon", 0, Pardon.class)),
    BAN(new Command("ban", 0, Ban.class)),
    MUTE(new Command("mute", 0, Mute.class)),
    UNMUTE(new Command("unmute", 0, Unmute.class)),
    KICK(new Command("kick", 0, Kick.class)),
    RELOAD(new Command("reload", 0, Reload.class)),
    HELP(new Command("help", 0, Help.class)),
    WHISPER(new Command("whisper", 0, Whisper.class)),
    RESEND(new Command("resend", 0, Resend.class)),
    VANISH(new Command("vanish", 0, Vanish.class)),
    CHANNEL(new Command("channel", 0, Channel.class));

    private @Getter final Command command;

    static {
        Command.addCompletion(new TabCompletion("PLAYER_PARDON", (commandSender) -> BanSystem.getBanList(KickSystem.TypeBan.BAN)){
            @Override
            public void error(String nameCommand, CommandSender commandSender, String parameter) {
                commandSender.sendMessage("CMD.PARDON.PLAYER_NOT_BAN.ERROR", new Lang.Args(Lang.Parameter.VAR, parameter));
            }
        });
        Command.addCompletion(new TabCompletion("PLAYER_UNMUTE", (commandSender) -> BanSystem.getBanList(KickSystem.TypeBan.MUTE)){
            @Override
            public void error(String nameCommand, CommandSender commandSender, String parameter) {
                commandSender.sendMessage("CMD.UNMUTE.PLAYER_NOT_BAN.ERROR", new Lang.Args(Lang.Parameter.VAR, parameter));
            }
        });
    }

    CommandManager(Command command){
        this.command = command;
    }
    private void active(){
        Bungee.getInstance().getProxy().getPluginManager().registerCommand(Bungee.getInstance(), new CommandListener(this.command));
    }
    public static void init(){
        for(CommandManager commandManager : CommandManager.values()) commandManager.active();

    }
}
