package net.vorps.bungee.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vorps.api.Exceptions.SqlException;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.DataBungee;
import net.vorps.bungee.players.PlayerData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Project Bungee Created by Vorps on 07/02/2017 at 20:30.
 */
public class BanSystem {

    @AllArgsConstructor
    public enum TypeBan{
        BAN("PARDON"),MUTE("UNMUTE"),CHANNEL("PARDON_CHANNEL");

        private final String pardon;
    }

    private final UUID uuid;
    private final UUID author;
    private final Long time;
    private final String reason;
    private final TypeBan typeBan;

    public boolean update() {
        if (this.time != null && this.time <= System.currentTimeMillis()){
            this.remove();
            return true;
        }
        return false;
    }

    public static String isBan(UUID playerUUID, TypeBan typeBan) {
        BanSystem banSystem = BanSystem.getBanMute(playerUUID, typeBan);
        if (banSystem != null){
            String lang = PlayerData.getLang(playerUUID);
            if(!banSystem.update()) {
                return Lang.getMessage("CMD.BAN_SYSTEM.PLAYER.SHOW", lang, new Lang.Args(Lang.Parameter.MESSAGE, banSystem.toString(lang)));
            }
        }
        return null;
    }

    protected static @Getter HashMap<String, BanSystem> banMuteList;

    static {
        BanSystem.banMuteList = new HashMap<>();;
        DataBungee.loadBanSystem();
    }

    public BanSystem(ResultSet result) throws SQLException{
        this(UUID.fromString(result.getString(1)), UUID.fromString(result.getString(2)), result.getTimestamp(3) != null ? result.getTimestamp(3).getTime() : null, result.getString(4), result.getString(5));
    }


    public BanSystem(UUID uuid, UUID author, Long time, String reason, String typeBan) {
        this.uuid = uuid;
        this.author = author;
        this.time = time;
        this.reason = reason;
        this.typeBan = TypeBan.valueOf(typeBan);
        BanSystem.banMuteList.put(this.uuid+":"+this.typeBan.name(), this);
    }

    public static BanSystem getBanMute(UUID uuid, TypeBan typeBan){
        for(BanSystem banSystem : BanSystem.banMuteList.values()){
            if(banSystem.typeBan == typeBan && banSystem.uuid.equals(uuid)) return banSystem;
        }
        return null;
    }

    public void remove(){
        try {
            Database.BUNGEE.getDatabase().delete("ban_system", "ban_uuid = '" + this.uuid + "' && ban_type = '"+this.typeBan.name()+"'");
        } catch (SqlException e) {
            e.printStackTrace();
        }
        if(PlayerData.isPlayerDataCore(this.uuid)) {
            PlayerData playerData =  PlayerData.getPlayerData(this.uuid);
            playerData.getProxiedPlayer().sendMessage(new TextComponent(Lang.getMessage("CMD."+typeBan.name()+".PLAYER.PARDON", playerData.getLang())));
        } else net.vorps.api.players.PlayerData.addNotification(this.uuid, Lang.getMessage("CMD."+typeBan.name()+".PLAYER.PARDON", PlayerData.getLang(this.uuid)));

        BanSystem.banMuteList.remove(this.uuid+":"+this.typeBan);
    }

    public String toString(String lang) {
        String message;
        if (time == null && reason == null)
            message = Lang.getMessage("CMD."+this.typeBan.name()+".PLAYER.DEF", lang, new Lang.Args(Lang.Parameter.AUTHOR, Data.getNamePlayer(author)));
        else if (time == null)
            message = Lang.getMessage("CMD."+this.typeBan.name()+".PLAYER.DEF_REASON", lang, new Lang.Args(Lang.Parameter.AUTHOR, Data.getNamePlayer(author)), new Lang.Args(Lang.Parameter.REASON, reason));
        else if (time > 0 && reason == null)
            message = Lang.getMessage("CMD."+this.typeBan.name()+".PLAYER.TIME", lang, new Lang.Args(Lang.Parameter.AUTHOR, Data.getNamePlayer(author)), new Lang.Args(Lang.Parameter.TIME, Data.FORMAT_DAY_MONTH_YEAR_HOUR_MINUTE_SECOND.format(new Date(time))));
        else
            message = Lang.getMessage("CMD."+this.typeBan.name()+".PLAYER.TIME_REASON", lang, new Lang.Args(Lang.Parameter.AUTHOR, Data.getNamePlayer(author)), new Lang.Args(Lang.Parameter.TIME, Data.FORMAT_DAY_MONTH_YEAR_HOUR_MINUTE_SECOND.format(new Date(time))), new Lang.Args(Lang.Parameter.REASON, reason));
        return message;
    }

    public static long getTime(String time)  throws NumberFormatException{
        int day = 0, hour = 0,minute = 0,second = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < time.length(); i++){
            char c = time.charAt(i);
            switch (c){
                case 'd':
                    day = Integer.parseInt(stringBuilder.toString()) * 86400000;
                    stringBuilder.delete(0, stringBuilder.length());
                    break;
                case 'h':
                    hour = Integer.parseInt(stringBuilder.toString()) * 3600000;
                    stringBuilder.delete(0, stringBuilder.length());
                    break;
                case 'm':
                    minute = Integer.parseInt(stringBuilder.toString()) * 60000;
                    stringBuilder.delete(0, stringBuilder.length());
                    break;
                case 's':
                    second = Integer.parseInt(stringBuilder.toString()) * 1000;
                    stringBuilder.delete(0, stringBuilder.length());
                    break;
                default:
                    stringBuilder.append(c);
                    break;
            }
        }
        return day + hour + minute + second + System.currentTimeMillis();
    }

    public static void banSystemCommand(net.vorps.api.commands.CommandSender author, String namePlayer, String time, String reason, TypeBan typeBan){
        if (!author.getName().equals(namePlayer)) {
            if (Data.isPlayer(namePlayer)) {
                BanSystem banSystem = BanSystem.getBanMute(Data.getUUIDPlayer(namePlayer), typeBan);
                if(banSystem != null) banSystem.update();
                banSystem = BanSystem.getBanMute(Data.getUUIDPlayer(namePlayer), typeBan);
                if (banSystem == null) {
                    long timeLeft = -1;
                    if(time != null){
                        try{
                            timeLeft = BanSystem.getTime(time);
                        } catch (NumberFormatException ignored){}
                    }
                    try {
                        Database.BUNGEE.getDatabase().insertTable("ban_system", Data.getUUIDPlayer(namePlayer).toString(),Data.getUUIDPlayer(author.getName()).toString(), timeLeft != -1 ? new Date(timeLeft) : null, reason, typeBan.name());
                        BanSystem banMute = new BanSystem(Data.getUUIDPlayer(namePlayer), Data.getUUIDPlayer(author.getName()), timeLeft != -1 ? timeLeft : null, reason, typeBan.name());
                        author.sendMessage(Lang.getMessage("CMD.BAN_SYSTEM.SENDER", author.getLang(), new Lang.Args(Lang.Parameter.PLAYER, namePlayer), new Lang.Args(Lang.Parameter.MESSAGE, banMute.toString(author.getLang()))));
                        if (PlayerData.isPlayerDataCore(namePlayer))
                            ProxyServer.getInstance().getPlayer(namePlayer).disconnect(new TextComponent(Lang.getMessage("CMD.BAN_SYSTEM.PLAYER.SHOW", PlayerData.getPlayerData(namePlayer).getLang(), new Lang.Args(Lang.Parameter.MESSAGE, banMute.toString(PlayerData.getPlayerData(namePlayer).getLang())))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    author.sendMessage(Lang.getMessage("CMD.BAN_SYSTEM.ERROR.ALREADY_BAN", author.getLang(), new Lang.Args(Lang.Parameter.MESSAGE, BanSystem.getBanMute(Data.getUUIDPlayer(namePlayer),typeBan).toString(author.getLang()))));
            } else
                author.sendMessage(Lang.getMessage("CMD.ERROR.DONT_KNOW_PLAYER", author.getLang(), new Lang.Args(Lang.Parameter.PLAYER, namePlayer)));
        } else
            author.sendMessage(Lang.getMessage("CMD."+typeBan.name()+".ERROR.CANT_BAN_YOURSELF", author.getLang()));
    }

    public static void pardonSystemCommand(CommandSender author, String namePlayer, TypeBan typeBan){
        BanSystem banSystem = BanSystem.getBanMute(Data.getUUIDPlayer(namePlayer), typeBan);
        if (banSystem != null && !banSystem.update()) {
            banSystem.remove();
            author.sendMessage(Lang.getMessage("CMD."+typeBan.pardon+".PLAYER", author.getLang(), new Lang.Args(Lang.Parameter.PLAYER, namePlayer)));
        } else {
            author.sendMessage(Lang.getMessage("CMD."+typeBan.pardon+".PLAYER_NOT_BAN.ERROR", author.getLang(), new Lang.Args(Lang.Parameter.PLAYER, namePlayer)));
        }
    }
}
