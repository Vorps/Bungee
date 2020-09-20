package net.vorps.bungee.objects;

import lombok.Getter;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.data.DataBungee;
import net.vorps.bungee.players.PlayerData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Project Bungee Created by Vorps on 07/02/2017 at 20:30.
 */
public class BanSystem extends KickSystem{

    private final Long time;

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
        this(UUID.fromString(result.getString(1)), UUID.fromString(result.getString(2)), result.getTimestamp(3) != null ? result.getTimestamp(3).getTime() : null, result.getString(4), TypeBan.valueOf(result.getString(5)));
    }


    public BanSystem(UUID uuid, UUID author, Long time, String reason, TypeBan typeBan) {
        super(uuid, author, reason, typeBan);
        this.time = time;
        BanSystem.banMuteList.put(this.uuid+":"+this.typeBan.name(), this);
    }

    public static BanSystem getBanMute(UUID uuid, TypeBan typeBan){
        for(BanSystem banSystem : BanSystem.banMuteList.values()){
            if(banSystem.typeBan == typeBan && banSystem.uuid.equals(uuid)) return banSystem;
        }
        return null;
    }

    public static List<String> getBanList(TypeBan typeBan){
        return BanSystem.banMuteList.values().stream().filter((e) -> e.typeBan == typeBan).map(e -> Data.getNamePlayer(e.uuid)).collect(Collectors.toList());
    }

    public void remove(){
        try {
            Database.BUNGEE.getDatabase().delete("ban_system", "ban_uuid = '" + this.uuid + "' && ban_type = '"+this.typeBan.name()+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(PlayerData.isPlayerDataCore(this.uuid)) {
            PlayerData.getPlayerData(this.uuid).sendMessage("CMD."+this.typeBan.name()+".PLAYER.PARDON");
        } else net.vorps.api.players.PlayerData.addNotification(this.uuid, Lang.getMessage("CMD."+this.typeBan.name()+".PLAYER.PARDON", PlayerData.getLang(this.uuid)));

        BanSystem.banMuteList.remove(this.uuid+":"+this.typeBan);
    }

    public String toString(String lang) {
        String message;
        if(time == null) message = super.toString(lang);
        else if (time > 0 && reason == null)
            message = Lang.getMessage("CMD."+this.typeBan.name()+".PLAYER.TIME", lang, new Lang.Args(Lang.Parameter.TIME, Data.FORMAT_DAY_MONTH_YEAR_HOUR_MINUTE_SECOND.format(new Date(time))));
        else
            message = Lang.getMessage("CMD."+this.typeBan.name()+".PLAYER.TIME_REASON", lang, new Lang.Args(Lang.Parameter.TIME, Data.FORMAT_DAY_MONTH_YEAR_HOUR_MINUTE_SECOND.format(new Date(time))), new Lang.Args(Lang.Parameter.MESSAGE, reason));
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

    public static BanSystem banSystemCommand(net.vorps.api.commands.CommandSender author, Player player, String time, String reason, TypeBan typeBan){
        BanSystem banSystem = BanSystem.getBanMute(player.getUUID(), typeBan);
        if(banSystem != null) banSystem.update();
        banSystem = BanSystem.getBanMute(player.getUUID(), typeBan);
        if (banSystem == null) {
            long timeLeft = -1;
            if(time != null){
                try{
                    timeLeft = BanSystem.getTime(time);
                } catch (NumberFormatException ignored){}
            }
            try {
                Database.BUNGEE.getDatabase().insertTable("ban_system", player.getUUID().toString(),Data.getUUIDPlayer(author.getName()).toString(), timeLeft != -1 ? new Date(timeLeft) : null, reason, typeBan.name());
                BanSystem banMute = new BanSystem(player.getUUID(), Data.getUUIDPlayer(author.getName()), timeLeft != -1 ? timeLeft : null, reason, typeBan);
                author.sendMessage("CMD.BAN_SYSTEM.SENDER", new Lang.Args(Lang.Parameter.PLAYER, player.getName()), new Lang.Args(Lang.Parameter.MESSAGE, banMute.toString(PlayerData.getLang(author.getUUID()))));
                return banMute;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            author.sendMessage("CMD.BAN_SYSTEM.ERROR.ALREADY_BAN", new Lang.Args(Lang.Parameter.MESSAGE, BanSystem.getBanMute(player.getUUID(),typeBan).toString(PlayerData.getLang(author.getUUID()))));
        return null;
    }

    public static void pardonSystemCommand(CommandSender author, Player player, TypeBan typeBan){
        BanSystem banSystem = BanSystem.getBanMute(player.getUUID(), typeBan);
        banSystem.remove();
        author.sendMessage("CMD."+typeBan.getPardon()+".PLAYER.SENDER", new Lang.Args(Lang.Parameter.PLAYER, player.getName()));
    }
}
