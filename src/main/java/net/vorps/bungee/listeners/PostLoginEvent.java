package net.vorps.bungee.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.objects.BanSystem;
import net.vorps.bungee.players.PlayerData;
import net.vorps.bungee.objects.Servers;
import net.vorps.dispatcher.Dispatcher;
import net.vorps.dispatcher.ServerType;

public class PostLoginEvent implements Listener {

    @EventHandler
    public void onPostLogin(net.md_5.bungee.api.event.PostLoginEvent e) {
        String banMessage = BanSystem.isBan(e.getPlayer().getUniqueId(), BanSystem.TypeBan.BAN);
        if (banMessage != null){
            e.getPlayer().disconnect(new TextComponent(banMessage));
        } else{
            ServerInfo serverInfo =  ((Servers) Dispatcher.connectServer(ServerType.getServerTypeFromName("HUB"), true)).getServerInfo();
            if(serverInfo == null){
                e.getPlayer().disconnect(new TextComponent(Lang.getMessage("CONNECT.ERROR", PlayerData.getLang(e.getPlayer().getUniqueId()))));
            } else{
                e.getPlayer().connect(serverInfo);
                String playerIp = e.getPlayer().getSocketAddress().toString().split(":")[0];
                PlayerData.updatePlayerDataDataBase(e.getPlayer(), playerIp);
                PlayerData.setServer(e.getPlayer().getUniqueId(), Servers.getServer(serverInfo.getName()));
                new PlayerData(e.getPlayer().getUniqueId());
            }

        }
    }
}
