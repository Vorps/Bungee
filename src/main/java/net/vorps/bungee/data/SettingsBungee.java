package net.vorps.bungee.data;

import lombok.Getter;
import net.vorps.api.data.DataCore;
import net.vorps.api.data.SettingCore;

/**
 * Project Bungee Created by Vorps on 07/02/2017 at 18:14.
 */
public class SettingsBungee extends SettingCore{

    private static @Getter String motd;

    public static void initSettings(){
        SettingCore.initFunction = () -> {
            SettingsBungee.motd = net.vorps.api.data.Settings.getSettings("motd").getMessage();
        };
        DataCore.loadSetting();
    }
}
