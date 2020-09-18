package net.vorps.bungee.channel;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.vorps.api.channel.ChannelBuilder;

import java.util.UUID;

public class ChannelManager extends ChannelBuilder<Plugin> {

    @Override
    public void send(Plugin plugin, UUID uuid) {
        plugin.getProxy().getPlayer(uuid).getServer().sendData(this.channel, this.build());
    }
}
