package net.vorps.bungee.listeners;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.vorps.bungee.PlayerData;
import net.vorps.bungee.objects.Servers;
import net.vorps.dispatcher.Dispatcher;
import net.vorps.dispatcher.ServerType;

public class PostLoginEvent implements Listener {

    @EventHandler
    public void onPostLogin(net.md_5.bungee.api.event.PostLoginEvent e) {
        ServerInfo serverInfo =  ((Servers) Dispatcher.connectServer(ServerType.getServerTypeFromName("HUB"), true)).getServerInfo();
        e.getPlayer().connect(serverInfo);
        new PlayerData(e.getPlayer().getUniqueId(), e.getPlayer().getSocketAddress().toString().split(":")[0]);
    }
}
