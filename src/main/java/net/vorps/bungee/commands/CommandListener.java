package net.vorps.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.vorps.api.utils.Settings;
import net.vorps.bungee.Bungee;
import net.vorps.bungee.players.PlayerData;

import java.util.ArrayList;
import java.util.UUID;

public class CommandListener extends Command implements TabExecutor {

    private final net.vorps.api.commands.Command command;

    public CommandListener(net.vorps.api.commands.Command command){
        super(command.getName());
        this.command = command;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        this.command.execute(CommandListener.getCommandSender(commandSender), args);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        return this.command.onTabComplete(CommandListener.getCommandSender(commandSender), args);
    }

    private static net.vorps.api.commands.CommandSender getCommandSender(CommandSender commandSender){
        return new net.vorps.api.commands.CommandSender() {
            @Override
            public void sendMessage(String message) {
                commandSender.sendMessage(new TextComponent(message));
            }

            @Override
            public boolean hasPermission(ArrayList<String> permission) {
                return  permission.stream().map(commandSender::hasPermission).reduce(true, (last, next) -> last && next);
            }

            @Override
            public String getLang() {
                if(commandSender instanceof ProxiedPlayer && PlayerData.isPlayerDataCore(commandSender.getName())) return PlayerData.getPlayerData(commandSender.getName()).getLang();
                return Settings.getConsoleLang();
            }

            @Override
            public boolean hasPermissionStartWith(String permission) {
                if(!(commandSender instanceof ProxiedPlayer)) return true;
                for(String perm : commandSender.getPermissions()){
                    if(perm.startsWith(permission)) return true;
                }
                return false;
            }

            @Override
            public String getName() {
                return commandSender.getName();
            }

            @Override
            public boolean isPlayer() {
                return commandSender instanceof ProxiedPlayer;
            }
        };
    }
}
