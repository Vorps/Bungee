package net.vorps.bungee.utils;

import net.md_5.bungee.api.chat.TextComponent;
import net.vorps.api.commands.CommandSender;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Project FortycubeBungee Created by Vorps on 10/03/2016 at 02:40.
 */
public class ChatColor {

    public static String chatColor(CommandSender sender, String message, String permission) {
        StringBuilder messageBuild = new StringBuilder(message);
        if (sender.hasPermission(new ArrayList<>(Collections.singleton(permission)))) {
            for (int i = 0; i < messageBuild.length(); i++) {
                if (messageBuild.charAt(i) == '&') {
                    messageBuild.replace(i, i + 1, "§");
                }
            }
        }
        return messageBuild.toString().trim();
    }

    public static String chatColor(String message) {
        StringBuilder messageBuild = new StringBuilder(message);
        for (int i = 0; i < messageBuild.length(); i++) {
            if (messageBuild.charAt(i) == '&') {
                messageBuild.replace(i, i + 1, "§");
            }
        }
        return messageBuild.toString().trim();
    }

    public static StringBuilder colorMessage(StringBuilder message) {
        for (int i = 0; i < message.length(); i++) if (message.charAt(i) == '&') message.replace(i, i + 1, "§");
        return message;
    }
}
