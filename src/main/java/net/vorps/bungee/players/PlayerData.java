package net.vorps.bungee.players;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.api.databases.DatabaseManager;
import net.vorps.api.lang.Lang;
import net.vorps.api.objects.Rank;
import net.vorps.bungee.Bungee;
import net.vorps.bungee.channel.ChannelManager;
import net.vorps.bungee.objects.*;
import net.vorps.dispatcher.Server;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Project Bungee Created by Vorps on 07/02/2017 at 17:01.
 */
public class PlayerData extends net.vorps.api.players.PlayerData {

    private Server server;
    private Channel channel;
    private Friends friends;
    private boolean isChat;
    private @Setter @Getter UUID whisper_uuid;


    public PlayerData(UUID uuid) {
        super(uuid, ProxyServer.getInstance().getPlayer(uuid).getName());
        Permissions.permissionRank(super.UUID, this.rank.getRank());
        this.showNotification();
    }

    public static void updatePlayerDataDataBase(ProxiedPlayer player, String playerIp) {
        try {
            List<DatabaseManager.Values> valuesArrayList = new ArrayList<>();
            valuesArrayList.add(new DatabaseManager.Values("p_online", true));
            ResultSet result = Database.BUNGEE.getDatabase().getData("player", "p_uuid = '" + player.getUniqueId() + "'");
            if (!result.next()) {
                CallableStatement call = Database.BUNGEE.getDatabase().getConnection().prepareCall("{call ps_create_user(?, ?, ?)}");
                call.setString(1, player.getUniqueId().toString());
                call.setString(2, player.getName());
                call.setString(3, playerIp);
                call.execute();
                Data.addPlayer(player.getName(), player.getUniqueId());
            } else {
                if (!result.getString(3).equals(playerIp))
                    valuesArrayList.add(new DatabaseManager.Values("p_ip", playerIp));
                if (!result.getString(2).equals(player.getName()))
                    valuesArrayList.add(new DatabaseManager.Values("p_name", player.getName()));
                valuesArrayList.add(new DatabaseManager.Values("p_date_last", new Date(System.currentTimeMillis())));
                valuesArrayList.add(new DatabaseManager.Values("p_online", true));
            }
            Database.BUNGEE.getDatabase().updateTable("player", "p_uuid = '" + player.getUniqueId() + "'", valuesArrayList.toArray(new DatabaseManager.Values[0]));
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    @Override
    public void init() {
        this.channel = PlayerData.getChannel(this.UUID);
        this.friends = PlayerData.getFriends(this.UUID);
        this.isChat = PlayerData.isChat(this.UUID);
    }

    public void removePlayerData() {
        super.removePlayerData();
        PlayerData.setChat(super.UUID, this.isChat);
        try {
        Database.BUNGEE.getDatabase().updateTable("player", "p_uuid = '" + super.UUID + "'", new DatabaseManager.Values("p_online", false));

        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    private void showNotification() {
        ArrayList<String> notification = new ArrayList<>();
        try {
            ResultSet resultSet = Database.BUNGEE.getDatabase().getData("notification", "n_uuid = '" + super.UUID.toString() + "'");
            while (resultSet.next()) notification.add(resultSet.getString("n_message"));
            Database.BUNGEE.getDatabase().delete("notification", "n_uuid = '" + super.UUID.toString() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(!notification.isEmpty()) {
            this.sendMessage("§a✴--------------------------------------------------✴");
            notification.forEach(this::sendMessage);
        }
    }

    public static void setServer(UUID uuid, Server server) {
        if(PlayerData.isPlayerDataCore(uuid)){
            PlayerData.getPlayerData(uuid).server = server;
        }
        try {
            Database.BUNGEE.getDatabase().updateTable("player", "p_uuid = '" + uuid + "'", new DatabaseManager.Values("p_server", server.getName()));
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    public static void setNickName(UUID uuid, String nickname) {
        if(PlayerData.isPlayerDataCore(uuid)){
            PlayerData.getPlayerData(uuid).nickName = nickname;
        }
        try {
            Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_nickname", nickname));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setWhisper_uuid(UUID uuid, UUID whisper_uuid) {
        if(PlayerData.isPlayerDataCore(uuid)){
            PlayerData.getPlayerData(uuid).whisper_uuid = whisper_uuid;
        }
        try {
            Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_whisper_uuid", uuid.toString()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static UUID getWhisper_uuid(UUID uuid) {
        UUID whisper_uuid = null;
        if(PlayerData.isPlayerDataCore(uuid)){
            whisper_uuid = PlayerData.getPlayerData(uuid).whisper_uuid;
        } else {
            try {
                whisper_uuid = java.util.UUID.fromString(Database.BUNGEE.getDatabase().getDataUnique("player_setting", "ps_uuid = '" + uuid + "'").getString("ps_whisper_uuid"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return whisper_uuid;
    }

    public static Friends getFriends(UUID uuid){
        Friends friends = null;
        if(PlayerData.isPlayerDataCore(uuid)){
            friends = PlayerData.getPlayerData(uuid).friends;
        } else if(Data.isPlayer(uuid)){
            try {
                friends = new Friends(uuid, Database.BUNGEE.getDatabase().getDataUnique("player_setting", "ps_uuid = '" + uuid + "'").getBoolean("ps_friends_enable"));
            } catch (SQLException var3) {
                var3.printStackTrace();
            }
        }
        return friends;
    }

    public static Channel getChannel(UUID uuid) {
        Channel channel = null;
        if(uuid != null){
            if(PlayerData.isPlayerDataCore(uuid)){
                channel = PlayerData.getPlayerData(uuid).channel;
            } else if(Data.isPlayer(uuid)){
                try {
                    channel = Channel.getChannel(Database.BUNGEE.getDatabase().getDataUnique("player_setting", "ps_uuid = '" + uuid + "'").getString("ps_channel"));
                } catch (SQLException var3) {
                    var3.printStackTrace();
                }
            }
        }
        return channel;
    }

    public static void setChannel(UUID uuid, Channel channel) {
        if(uuid != null){
            if(Data.isPlayer(uuid)){
                if(PlayerData.isPlayerDataCore(uuid)){
                    PlayerData.getPlayerData(uuid).channel = channel;
                }
                try {
                    Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_channel", channel.getName()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setVanish(UUID uuid, boolean isVanish) {
        if(PlayerData.isPlayerDataCore(uuid)){
            PlayerData.getPlayerData(uuid).isVanish = isVanish;
            new ChannelManager().setChannel("BungeeCord").setSubChannel("VANISH").addValues(isVanish).send(Bungee.getInstance(), uuid);
        }
        try {
            Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_vanish", isVanish));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void setFly(UUID uuid, boolean isFly) {
        if(PlayerData.isPlayerDataCore(uuid)){
            PlayerData playerData =  PlayerData.getPlayerData(uuid);
            playerData.isFly = isFly;
            new ChannelManager().setChannel("BungeeCord").setSubChannel("FLY").addValues(isFly).send(Bungee.getInstance(), uuid);
        }
        try {
            Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_fly", isFly));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setBuild(UUID uuid, boolean isBuild) {
        if(PlayerData.isPlayerDataCore(uuid)){
            PlayerData playerData =  PlayerData.getPlayerData(uuid);
            playerData.isBuild = isBuild;
            new ChannelManager().setChannel("BungeeCord").setSubChannel("BUILD").addValues(isBuild).send(Bungee.getInstance(), uuid);
        }
        try {
            Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_build", isBuild));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setVisible(UUID uuid, boolean isVisible) {
        if(PlayerData.isPlayerDataCore(uuid)){
            PlayerData playerData =  PlayerData.getPlayerData(uuid);
            playerData.isVisible = isVisible;
            new ChannelManager().setChannel("BungeeCord").setSubChannel("BUILD").addValues(isVisible).send(Bungee.getInstance(), uuid);
        }
        try {
            Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_visible", isVisible));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Date getDateLast(UUID uuid) {
        Date dateLast = new Date();
        if(uuid != null && Data.isPlayer(uuid)){
            try {
                dateLast = new Date(Database.BUNGEE.getDatabase().getDataUnique("player", "p_uuid = '" + uuid + "'").getTimestamp("p_date_last").getTime());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dateLast;
    }

    public static Date getDateFirst(UUID uuid) {
        Date dateFirst = new Date();
        if(uuid != null && Data.isPlayer(uuid)){
            try {
                dateFirst = new Date(Database.BUNGEE.getDatabase().getDataUnique("player", "p_uuid = '" + uuid + "'").getTimestamp("p_date_first").getTime());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dateFirst;
    }


    public static String getIp(UUID uuid) {
        String ip = "";
        if(uuid != null && Data.isPlayer(uuid)){
            try {
                ip = Database.BUNGEE.getDatabase().getDataUnique("player", "p_uuid = '" + uuid + "'").getString("p_ip");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }




    public static void setLang(UUID uuid, String lang) {
        if(PlayerData.isPlayerDataCore(uuid)){
            PlayerData.getPlayerData(uuid).lang = lang;
        }
        try {
            Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_lang", lang));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setRank(UUID uuid, Rank rank) {
        if(PlayerData.isPlayerDataCore(uuid)){
            PlayerData.getPlayerData(uuid).rank = rank;
            Permissions.permissionRank(uuid, rank.getRank());
        }
        try {
            Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_rank", rank.getRank()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean isChat(UUID uuid) {
        boolean isChat = true;
        if(uuid != null){
            if(PlayerData.isPlayerDataCore(uuid)){
                isChat =  PlayerData.getPlayerData(uuid).isChat;
            } else if(Data.isPlayer(uuid)){
                try {
                    isChat = Database.BUNGEE.getDatabase().getDataUnique("player_setting", "ps_uuid = '" + uuid + "'").getBoolean("ps_chat");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isChat;
    }


    public static void setChat(UUID uuid, boolean state) {
        if(uuid != null && Data.isPlayer(uuid)){
            try {
                Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_chat", state));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static PlayerData getPlayerData(String name) {
        return (PlayerData) PlayerData.getPlayerDataCore(name);
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return (PlayerData) PlayerData.getPlayerDataCore(uuid);
    }

    @Override
    public void sendMessage(String key, Lang.Args... args) {
        Bungee.getInstance().getProxy().getPlayer(super.UUID).sendMessage(new TextComponent(Lang.getMessage(key, PlayerData.getLang(super.UUID), args)));
    }


}
