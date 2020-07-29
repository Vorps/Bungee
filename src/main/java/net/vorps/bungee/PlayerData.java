package net.vorps.bungee;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vorps.api.Exceptions.SqlException;
import net.vorps.api.data.Data;
import net.vorps.api.databases.DatabaseManager;
import net.vorps.api.objects.Rank;

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


    private @Getter @Setter String rank;
    private @Getter @Setter String whisper;
    private @Getter @Setter boolean chat;
    private @Getter @Setter boolean fly;
    private @Getter @Setter boolean show;
    private @Getter @Setter boolean vanish;


    public PlayerData(UUID uuid, String playerIp) {
        super(uuid, ProxyServer.getInstance().getPlayer(uuid).getName());
        this.playerIp = playerIp;
    }

    public ProxiedPlayer getProxiedPlayer() {
        return ProxyServer.getInstance().getPlayer(super.getUuid());
    }


    public static boolean updatePlayerDataDataBase(ProxiedPlayer player, String playerIp) {
        try {
            List<DatabaseManager.Values> valuesArrayList = new ArrayList<>();
            valuesArrayList.add(new DatabaseManager.Values("p_online", true));
            ResultSet result = Data.database.getData("player", "p_uuid = '" + player.getUniqueId() + "'");
            if (!result.next()) {
                CallableStatement call = Data.database.getConnection().prepareCall("{call ps_create_user(?, ?, ?)}");
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
            Data.database.updateTable("player", "p_uuid = '" + player.getUniqueId() + "'", valuesArrayList.toArray(new DatabaseManager.Values[0]));
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

    @Override
    public String toString() {
        return Rank.getRank(this.rank).toString() + " " + this.name;
    }

    public static PlayerData getPlayerData(String name) {
        return (PlayerData) PlayerData.getPlayerDataCore(name);
    }
}
