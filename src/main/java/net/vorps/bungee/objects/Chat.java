package net.vorps.bungee.objects;

import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vorps.api.data.Data;
import net.vorps.api.lang.Lang;
import net.vorps.api.utils.ChatColor;
import net.vorps.api.utils.StringBuilder;
import net.vorps.bungee.DataBungee;
import net.vorps.bungee.players.PlayerData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Project Bungee Created by Vorps on 26/02/2017 at 14:17.
 */
public class Chat {

    private @Getter final String word;
    private @Getter final String replace;

    public Chat(ResultSet resultSet) throws SQLException {
        this.word = resultSet.getString(1);
        this.replace = resultSet.getString(2);
        Chat.chatList.put(this.word, this);
    }

    private static HashMap<String, Chat> chatList;

    static {
        Chat.chatList = new HashMap<>();
        DataBungee.loadChat();
    }

    public static boolean isWord(String word) {
        return Chat.chatList.containsKey(word);
    }

    public static Chat getChat(String word) {
        return Chat.chatList.get(word);
    }

    public static void clear() {
        Chat.chatList.clear();
    }

    public static String getMessage(String message, String... word) {
        String[] args = new StringBuilder(message, " ").getArgs();
        StringBuilder stringBuilder = new StringBuilder(args, " ", 0);
        for (int i = 0; i < args.length; i++) {
            if (Chat.isWord(args[i].toLowerCase())) {
                Chat chat = Chat.getChat(args[i].toLowerCase());
                if (chat.replace != null) {
                    stringBuilder.replace(i, chat.replace);
                } else {
                    stringBuilder = new StringBuilder();
                    break;
                }
            }
            for (String s : word)
                if (s.equalsIgnoreCase(args[i])) {
                    stringBuilder = new StringBuilder();
                    break;
                }
        }
        return stringBuilder.getString();
    }

    public static void sendMessage(ProxiedPlayer sender, String message) {
        String message_1 = Chat.getMessage(message);
        PlayerData playerData = PlayerData.getPlayerData(sender.getName());
        sender.getServer().getInfo().getPlayers().forEach((ProxiedPlayer player) -> player.sendMessage(new TextComponent(Chat.getMessage(sender, playerData, message_1))));
        /*String message_1 = Chat.getMessage(message);
        if (!message_1.isEmpty()) {
            PlayerData playerData = PlayerData.getPlayerData(sender.getName());
            switch (playerData.getChannel().getName()) {
                case Channel.ALERT:
                    ProxyServer.getInstance().getPlayers().forEach((ProxiedPlayer player) -> player.sendMessage(new TextComponent(Chat.getMessage(sender, playerData, message_1))));
                    break;
                case Channel.FRIENDS:
                    playerData.getFriends().getFriends().keySet().forEach((String player) -> ProxyServer.getInstance().getPlayer(player).sendMessage(new TextComponent(Chat.getMessage(sender, playerData, message_1))));
                    break;
                case Channel.PARTY:
                    playerData.getParty().getMembers().keySet().forEach((String player) -> ProxyServer.getInstance().getPlayer(player).sendMessage(new TextComponent(Chat.getMessage(sender, playerData, message_1))));
                    break;
                case Channel.ALL:
                    sender.getServer().getInfo().getPlayers().forEach((ProxiedPlayer player) -> player.sendMessage(new TextComponent(Chat.getMessage(sender, playerData, message_1))));
                    break;
                default:
                    ProxyServer.getInstance().getServers().values().forEach((ServerInfo server) -> {
                        for (String serverTmp : playerData.getChannel().getVisibility()) {
                            if (Servers.isTypeServer(server.getName(), serverTmp)) {
                                ProxyServer.getInstance().getPlayers().forEach((ProxiedPlayer player) -> {
                                    if (player.getServer().getInfo().getName().equals(server.getName()) && PlayerData.getPlayerData(player.getName()).getChannel().equals(playerData.getChannel())) {
                                        player.sendMessage(new TextComponent(Chat.getMessage(sender, playerData, message_1)));
                                    }
                                });
                            }
                        }
                    });
                    break;
            }
        } else {
            // TODO: 02/03/2017 not accepted
        }*/
    }

    public static String getMessage(ProxiedPlayer sender, PlayerData playerData, String message) {
        String label = ChatColor.chatColor(playerData.getChannel().getLabel());
        return (label.isEmpty() ? "" : label + " ") + (playerData.getChannel().getAdmin().contains(Data.getUUIDPlayer(sender.getName()).toString()) ? Lang.getMessage("BUNGEE.CHAT.CHANNEL.ADMIN", playerData.getLang()) + " " : "") + "§f" + playerData.toString() + " : " + ChatColor.chatColor(message);
    }
}
