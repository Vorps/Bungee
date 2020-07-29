package net.vorps.bungee.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Project Bungee Created by Vorps on 02/02/2017 at 14:31.
 */
public class ListenerManager {

    public ListenerManager(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners)
            ProxyServer.getInstance().getPluginManager().registerListener(plugin, listener);
    }
}
