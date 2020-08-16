package net.vorps.bungee;

import lombok.Getter;

/**
 * Project Bungee Created by Vorps on 07/02/2017 at 18:14.
 */
public class Settings {

    private static @Getter String defaultLang;
    private static @Getter String motd;

    public static void initSettings() {
        Settings.defaultLang = net.vorps.api.utils.Settings.getSettings("default_lang").getMessage();
        Settings.motd = net.vorps.api.utils.Settings.getSettings("motd").getMessage();
    }
}
