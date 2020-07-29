package net.vorps.bungee.listeners;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.vorps.bungee.Settings;

/**
 * Project Bungee Created by Vorps on 10/02/2017 at 05:14.
 */
public class ProxyPingEvent implements Listener {

    @EventHandler
    public void onProxyPing(net.md_5.bungee.api.event.ProxyPingEvent e) {
        ServerPing sp = e.getResponse();
        sp.setVersion(new ServerPing.Protocol("Développement", 777));
        sp.setDescriptionComponent(new TextComponent(Settings.getMotd()));
        e.setResponse(sp);
    }
}
