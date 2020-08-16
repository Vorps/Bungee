package net.vorps.bungee.commands;

import lombok.Getter;
import net.vorps.api.commands.*;
import net.vorps.bungee.Bungee;

public enum CommandManager {
    NICKNAME(new Command("nickname", 0, Nickname.class)),
    PARDON(new Command("pardon", 0, Pardon.class)),
    BAN(new Command("ban", 0, Ban.class));

    private @Getter final Command command;

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
