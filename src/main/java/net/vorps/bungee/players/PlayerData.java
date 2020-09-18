package net.vorps.bungee.players;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vorps.api.commands.Player;
import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.api.databases.DatabaseManager;
import net.vorps.api.lang.Lang;
import net.vorps.api.objects.Rank;
import net.vorps.bungee.Bungee;
import net.vorps.bungee.DataBungee;
import net.vorps.bungee.channel.ChannelManager;
import net.vorps.bungee.objects.*;
import net.vorps.dispatcher.Server;
import org.w3c.dom.Text;

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
    private final Friends friends;
    private boolean isChat;
    private @Setter @Getter UUID whisper_uuid;
    private boolean isVanish;
    private boolean isFly;

    public PlayerData(UUID uuid) {
        super(uuid, ProxyServer.getInstance().getPlayer(uuid).getName());
        this.channel = PlayerData.getChannel(uuid);
        this.friends = PlayerData.getFriends(uuid);
        this.isChat = PlayerData.isChat(uuid);
        this.isVanish = PlayerData.isVanish(uuid);
        Permissions.permissionRank(super.UUID);
        this.showNotification();
    }

    public static boolean updatePlayerDataDataBase(ProxiedPlayer player, String playerIp) {
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
            return false;
        }
        return true;
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



    public static boolean isVanish(UUID uuid) {
        boolean isVanish = true;
        if(PlayerData.isPlayerDataCore(uuid)){
            isVanish =  PlayerData.getPlayerData(uuid).isVanish;
        } else if(Data.isPlayer(uuid)){
            try {
                isVanish = Database.BUNGEE.getDatabase().getDataUnique("player_setting", "ps_uuid = '" + uuid + "'").getBoolean("ps_vanish");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isVanish;
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

    public static boolean isFly(UUID uuid) {
        boolean isFly = false;
        if(PlayerData.isPlayerDataCore(uuid)){
            isFly =  PlayerData.getPlayerData(uuid).isFly;
        } else if(Data.isPlayer(uuid)){
            try {
                isFly = Database.BUNGEE.getDatabase().getDataUnique("player_setting", "ps_uuid = '" + uuid + "'").getBoolean("ps_fly");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isFly;
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



    public static void setRank(UUID uuid, String rank) {
        try {
            Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_rank", rank));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return this.rank.toString() + " " + this.name;
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
