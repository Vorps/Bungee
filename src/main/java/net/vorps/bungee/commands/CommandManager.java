package net.vorps.bungee.commands;

import lombok.Getter;
import net.vorps.api.commands.*;
import net.vorps.api.lang.Lang;
import net.vorps.api.lang.LangSetting;
import net.vorps.bungee.Bungee;
import net.vorps.bungee.objects.BanSystem;
import net.vorps.bungee.objects.KickSystem;
import net.vorps.bungee.objects.Servers;

import java.util.ArrayList;
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
    CHANNEL(new Command("channel", 0, Channel.class)),
    PING(new Command("ping", 0, Ping.class)),
    FLY(new Command("fly", 0, Fly.class)),
    INVSEE(new Command("invsee", 0, InvSee.class)),
    CONNECT(new Command("connect", 0, Connect.class)),
    HUB(new Command("hub", 0, Hub.class)),
    LANG(new Command("lang", 0, net.vorps.bungee.commands.Lang.class));

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
        Command.addCompletion(new TabCompletion("PLAYER_SAME_SERVER", "PLAYER", (commandSender) -> Bungee.getInstance().getProxy().getPlayers().stream().filter(e -> e.getServer().getInfo().getName().equals(Bungee.getInstance().getProxy().getPlayer(commandSender.getUUID()).getServer().getInfo().getName())).map(net.md_5.bungee.api.CommandSender::getName).collect(Collectors.toList())));
        Command.addCompletion(new TabCompletion("SERVER",  (commandSender) -> Servers.getServers()));
        Command.addCompletion(new TabCompletion("HUB_NUMBER", (commandSender) -> Servers.getServers().stream().filter(e -> e.startsWith("HUB_")).map(e -> e.split("_")[1]).collect(Collectors.toList())));
        Command.addCompletion(new TabCompletion("LANG", (commandSender) -> new ArrayList<>(LangSetting.getListLangSetting())));
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
