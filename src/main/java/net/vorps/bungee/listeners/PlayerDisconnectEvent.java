package net.vorps.bungee.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.vorps.bungee.PlayerData;

/**
 * Project Bungee Created by Vorps on 08/02/2017 at 01:53.
 */
public class PlayerDisconnectEvent implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(net.md_5.bungee.api.event.PlayerDisconnectEvent e) {
       // PlayerData.getPlayerData(e.getPlayer().getName()).removePlayerData();

    }
}