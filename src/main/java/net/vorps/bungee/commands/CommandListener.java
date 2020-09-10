package net.vorps.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.vorps.api.commands.Player;
import net.vorps.api.commands.PlayerAdapter;
import net.vorps.api.data.Data;
import net.vorps.api.lang.Lang;
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

    static {
        net.vorps.api.commands.Command.setPlayerAdapter(new PlayerAdapter() {
            @Override
            public Player getPlayer(String namePlayer) {
                return new Player() {
                    @Override
                    public void sendMessage(String key, Lang.Args... args) {
                        if(net.vorps.api.players.PlayerData.isPlayerDataCore(this.getUUID())){
                            Bungee.getInstance().getProxy().getPlayer(this.getUUID()).sendMessage(new TextComponent(Lang.getMessage(key, net.vorps.api.players.PlayerData.getLang(this.getUUID()), args)));
                        } else net.vorps.api.players.PlayerData.addNotification(this.getUUID(), key, args);
                    }

                    @Override
                    public String getName() {
                        return namePlayer;
                    }

                    @Override
                    public UUID getUUID() {
                        return Data.getUUIDPlayer(namePlayer);
                    }
                };
            }
        });
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
            public void sendMessage(String key, Lang.Args... args) {
                commandSender.sendMessage(new TextComponent(Lang.getMessage(key, PlayerData.getLang(this.getUUID()), args)));
            }

            @Override
            public boolean hasPermission(ArrayList<String> permission) {
                System.out.println(permission.get(0));
                return  permission.stream().map(commandSender::hasPermission).reduce(true, (last, next) -> last && next);
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
            public UUID getUUID() {
                return commandSender instanceof ProxiedPlayer ? ((ProxiedPlayer) commandSender).getUniqueId() : null;
            }
        };
    }
}
