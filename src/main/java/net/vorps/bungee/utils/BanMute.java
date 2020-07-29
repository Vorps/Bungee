package net.vorps.bungee.utils;

import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * Project Bungee Created by Vorps on 07/02/2017 at 20:30.
 */
@AllArgsConstructor
public abstract class BanMute {

    protected UUID uuid;
    protected UUID author;
    protected Long time;
    protected String reason;

    public void update() {
        if (time != null && time <= System.currentTimeMillis()) this.remove();
    }

    public abstract void remove();

    public abstract String toString(String lang);

}
