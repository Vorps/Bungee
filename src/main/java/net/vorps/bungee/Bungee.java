package net.vorps.bungee;

import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;
import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.api.message.NetWorkServer;
import net.vorps.bungee.listeners.*;
import net.vorps.bungee.objects.Servers;
import net.vorps.dispatcher.CreateServerException;
import net.vorps.dispatcher.Dispatcher;

import java.io.IOException;

/**
 * Project Bungee Created by Vorps on 07/11/2016 at 00:28.
 */
public class Bungee extends Plugin {

    private @Getter
    static Bungee instance;

    @SneakyThrows
    @Override
    public void onEnable() {
        Bungee.instance = this;
        Data.setDatabase(Database.BUNGEE.getDatabase());
        Settings.initSettings();
        getProxy().registerChannel( "BungeeCord" );
        new ListenerManager(this, new PostLoginEvent(), new ServerConnectEvent(), new PlayerDisconnectEvent(), new ChatEvent(), new ProxyPingEvent());
        new CommandManager();
        DataBungee.loadServerType();
        Servers.registerServer();
        try {
            Dispatcher.init();
        } catch (CreateServerException e){
            e.printStackTrace();
        }
        try {
            NetWorkServer.open("localhost", 6666, (name, port, serverState) -> {
                switch (serverState){
                    case WAITING:
                        new Servers(name, port);
                        break;
                    case STOP:
                        Servers.getServer(name).removeServer();
                        break;
                    default:
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void onDisable() {
        //for (PlayerData playerData : PlayerData.getPlayerDataList().values()) playerData.removePlayerData();
        //Channel.onDisable();
        NetWorkServer.close();
        Database.closeAllDataBases();
    }
}
