package net.vorps.bungee.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.vorps.bungee.objects.BanSystem;
import net.vorps.bungee.objects.Chat;
import net.vorps.bungee.objects.Commands;
import net.vorps.bungee.players.PlayerData;

/**
 * Project Bungee Created by Vorps on 24/02/2016 at 03:34.
 */
public class ChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat(net.md_5.bungee.api.event.ChatEvent e) {
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        String message = e.getMessage();
        if(message.startsWith("/")){
            String command = Commands.getCommand(message.split(" ")[0].substring(1).toLowerCase());
            if (command != null) e.setMessage("/"+command);
        } else {
            PlayerData playerData = PlayerData.getPlayerData(player.getName());
            if(playerData.isChat()){
                String mute = BanSystem.isBan(player.getUniqueId(), BanSystem.TypeBan.MUTE);
                if(mute != null) player.sendMessage(new TextComponent(mute));
                else Chat.sendMessage(player, message);
            }
            e.setCancelled(true);
        }
    }
}
