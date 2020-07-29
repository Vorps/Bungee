package net.vorps.bungee.utils;

import lombok.Getter;
import net.vorps.api.utils.StringBuilder;

/**
 * Project Bungee Created by Vorps on 29/03/2016 at 23:43.
 */
public class BanMuteUtils {

    private static char[][] id;

    static {
        BanMuteUtils.id = new char[][]{{'J', 'D'}, {'H', 'H'}, {'M', 'M'}, {'S', 'S'}};
    }

    private @Getter
    long time;
    private @Getter
    String reason;

    private @Getter
    String[] args;

    public BanMuteUtils(String[] args, int start) {
        this.args = args;
        this.setTime();
        this.setReason(start);
    }

    private void setTime() {
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        for (int i = 1; i < this.args.length; i++) {
            if (this.args[i].length() >= 2) {
                if (this.isId(0, this.args[i]))
                    try {
                        day = Integer.parseInt(this.args[i].substring(0, this.args[i].length() - 1)) * 86400000;
                    } catch (Exception e) {
                        //
                    }
                else if (this.isId(1, this.args[i]))
                    try {
                        hour = Integer.parseInt(this.args[i].substring(0, this.args[i].length() - 1)) * 3600000;
                    } catch (Exception e) {
                        //
                    }
                else if (this.isId(2, this.args[i]))
                    try {
                        minute = Integer.parseInt(this.args[i].substring(0, this.args[i].length() - 1)) * 60000;
                    } catch (Exception e) {
                        //
                    }
                else if (this.isId(3, this.args[i]))
                    try {
                        second = Integer.parseInt(this.args[i].substring(0, this.args[i].length() - 1)) * 1000;
                    } catch (Exception e) {
                        //
                    }
            }

        }
        int total = day + hour + minute + second;
        this.time = total > 0 ? total + System.currentTimeMillis() : 0;
    }

    private void setReason(int start) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (args[i].length() >= 2 && (this.isId(0, this.args[i]) || this.isId(1, this.args[i]) || this.isId(2, this.args[i]) || this.isId(3, this.args[i])))
                continue;
            sb.append(args[i]).append(' ');
        }
        String stringBuilder = new StringBuilder(sb.toString()).withColor(true).getString();
        this.reason = stringBuilder.length() > 0 ? stringBuilder : null;
    }

    private boolean isId(int id, String args) {
        char c = args.charAt(args.length() - 1);
        boolean stateDigit = true;
        boolean state = false;
        for (int y = 0; y < args.length() - 1; y++) if (!Character.isDigit(args.charAt(y))) stateDigit = false;
        if (stateDigit) for (int i = 0; i < 2; i++)
            if (c == BanMuteUtils.id[id][i] || c == (((int) BanMuteUtils.id[id][i]) + 32)) state = true;
        return state;
    }
}
