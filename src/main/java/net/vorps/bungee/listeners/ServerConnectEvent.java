package net.vorps.bungee.listeners;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Project Bungee Created by Vorps on 07/02/2017 at 21:05.
 */
public class ServerConnectEvent implements Listener {

    @EventHandler
    public void onConnectServerEvent(net.md_5.bungee.api.event.ServerConnectEvent e) {

        /*ProxiedPlayer player = e.getPlayer();
        String playerIp = e.getPlayer().getSocketAddress().toString().split(":")[0];
        if(!PlayerData.updatePlayerDataDataBase(e.getPlayer(), playerIp)){
            player.disconnect(new TextComponent("Error database"));
            e.setCancelled(false);
            return;
        }

        Mute.update(player, playerData.getLang());
        for (String notification : net.vorps.api.players.PlayerData.getNotification(player.getUniqueId()))
            player.sendMessage(InteractMessage.isInteractMessage(notification) ? InteractMessage.getInteractMessage(notification).get(notification, playerData.getLang()) : new TextComponent(notification));

        /*if (e.getTarget() != null) {
            if (Servers.isTypeServer(e.getTarget().getName(), "HUB")) playerData.setServer(e.getTarget().getName());
            Channel channel = playerData.getChannel();
            if (!channel.getVisibility().contains(Servers.getTypeServer(e.getTarget().getName())))

                Channel.getChannel(Channel.ALL).join(player, e.getTarget().getName(), true);
            else if (e.getPlayer().getServer() == null) channel.join(player, e.getTarget().getName(), false);
            playerData.setServer(e.getTarget().getName());
        }*/

    }
}
