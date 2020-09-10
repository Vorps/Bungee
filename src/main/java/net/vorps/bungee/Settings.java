package net.vorps.bungee;

import lombok.Getter;

/**
 * Project Bungee Created by Vorps on 07/02/2017 at 18:14.
 */
public class Settings {

    private static @Getter String motd;

    public static void initSettings() {
        Settings.motd = net.vorps.api.utils.Settings.getSettings("motd").getMessage();
    }
}
