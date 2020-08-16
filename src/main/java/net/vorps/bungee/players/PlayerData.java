package net.vorps.bungee.players;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vorps.api.Exceptions.SqlException;
import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.api.databases.DatabaseManager;
import net.vorps.api.objects.Rank;
import net.vorps.bungee.DataBungee;
import net.vorps.bungee.objects.Channel;
import net.vorps.bungee.objects.Permissions;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Project Bungee Created by Vorps on 07/02/2017 at 17:01.
 */
public class PlayerData extends net.vorps.api.players.PlayerData {

    private @Getter final String playerIp;
    private @Setter String server;
    private @Getter Channel channel;

    private @Getter @Setter Rank rank;
    private @Getter @Setter String whisper;
    private @Getter @Setter boolean chat;
    private @Getter @Setter boolean fly;
    private @Getter @Setter boolean show;
    private @Getter @Setter boolean vanish;


    public PlayerData(UUID uuid, String playerIp) {
        super(uuid, ProxyServer.getInstance().getPlayer(uuid).getName());
        this.playerIp = playerIp;
        this.rank = PlayerData.getRank(uuid);
        Permissions.permissionRank(this);
        this.showNotification();

    }

    public ProxiedPlayer getProxiedPlayer() {
        return ProxyServer.getInstance().getPlayer(super.getUuid());
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
            } else {
                if (!result.getString(3).equals(playerIp))
                    valuesArrayList.add(new DatabaseManager.Values("p_ip", playerIp));
                if (!result.getString(2).equals(player.getName()))
                    valuesArrayList.add(new DatabaseManager.Values("p_name", player.getName()));
                valuesArrayList.add(new DatabaseManager.Values("p_date_last", new Date(System.currentTimeMillis())));
            }
            Database.BUNGEE.getDatabase().updateTable("player", "p_uuid = '" + player.getUniqueId() + "'", valuesArrayList.toArray(new DatabaseManager.Values[0]));
            DataBungee.getListPlayerString().put(player.getName(), player.getUniqueId());
            DataBungee.getListPlayerUUID().put(player.getUniqueId(), player.getName());
        } catch (SQLException | SqlException err) {
            err.printStackTrace();
            return false;
        }
        return true;
    }

    public void removePlayerData() {
        super.removePlayerData();
        PlayerData.setChat(super.getUuid(), this.chat);
        PlayerData.setServer(super.getUuid(), this.server);
    }

    private void showNotification() {
        ArrayList<String> notification = new ArrayList<>();
        try {
            ResultSet resultSet = Database.BUNGEE.getDatabase().getData("notification", "n_uuid = '" + uuid.toString() + "'");
            while (resultSet.next()) notification.add(resultSet.getString("n_message"));
            Database.BUNGEE.getDatabase().delete("notification", "n_uuid = '" + uuid.toString() + "'");
        } catch (SQLException e) {
            //
        } catch (SqlException e) {
            e.printStackTrace();
        }
        this.getProxiedPlayer().sendMessage(new TextComponent("§a✴--------------------------------------------------✴"));
        notification.forEach(message -> this.getProxiedPlayer().sendMessage(new TextComponent(message)));
    }

    public static void setNickName(UUID uuid, String nickname) {
        try {
            Database.BUNGEE.getDatabase().updateTable("player_setting", "ps_uuid = '" + uuid + "'", new DatabaseManager.Values("ps_nickname", nickname));
        } catch (SqlException e) {
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
}
