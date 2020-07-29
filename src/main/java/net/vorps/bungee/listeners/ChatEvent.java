package net.vorps.bungee.listeners;

import net.vorps.bungee.PlayerData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Project Bungee Created by Vorps on 24/02/2016 at 03:34.
 */
public class ChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat(net.md_5.bungee.api.event.ChatEvent e) {
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        String message = e.getMessage();
        if(!message.startsWith("/")){
            String command = Commands.getCommand(message);
            if (command != null) e.setMessage(command);
            else e.setCancelled(true);
        }
        /*
        PlayerData playerData = PlayerData.getPlayerData(player.getName());

        if (!message.startsWith("/")) {
            if (!Mute.update(player, playerData.getLang()) && playerData.isChat()) Chat.sendMessage(player, message);
            e.setCancelled(true);
        } else {
            String command = Commands.getCommand(message);
            if (command != null) e.setMessage(command);
            else e.setCancelled(true);
        }*/
    }
}
