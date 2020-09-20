package net.vorps.bungee.objects;

import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vorps.api.utils.StringBuilder;
import net.vorps.bungee.data.DataBungee;
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
        if (!message_1.isEmpty()) PlayerData.getChannel(sender.getUniqueId()).sendMessage(message_1);
    }

    /*public static String getMessage(ProxiedPlayer sender, PlayerData playerData, String message) {
        String label = ChatColor.chatColor(playerData.getChannel().getLabel());
        return (label.isEmpty() ? "" : label + " ") + "§f" + playerData.toString() + " : " + ChatColor.chatColor(message);
    }*/
}
