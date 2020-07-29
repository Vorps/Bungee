package net.vorps.bungee;

import net.vorps.api.Exceptions.SqlException;
import net.vorps.api.data.Data;
import net.vorps.bungee.objects.*;
import net.vorps.dispatcher.ServerType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Project Bungee Created by Vorps on 07/02/2017 at 18:40.
 */
public class DataBungee extends Data {

    public static final String PATH = System.getProperty("user.home");
    public static final String DS = System.getProperty("file.separator");

    {
        Data.loadListPlayer();
    }

    public static void loadServerType() {
        ServersType.clear();
        try {
            ResultSet resultSet = Data.database.getData("server_type");
            while (resultSet.next()){
                new ServersType(resultSet);
            }
        } catch (SqlException | SQLException e) {
            e.printStackTrace();
        }
    }


    public static void loadServer() {
        Servers.clear();
        try {
            ResultSet resultSet = Data.database.getData("server");
            while (resultSet.next()){
                new Servers(resultSet);
            }
        } catch (SqlException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadChat() {
        Chat.clear();
        try {
            ResultSet resultSet = Data.database.getData("chat");
            while (resultSet.next()) new Chat(resultSet);
        } catch (SqlException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadCommands() {
        Commands.clear();
        try {
            ResultSet resultSet =  Data.database.getData("command");
            while (resultSet.next()) new Commands(resultSet);
        } catch (SqlException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadMute() {
        Mute.clear();
        try {
            ResultSet resultSet = Data.database.getData("mute");
            while (resultSet.next()) new Mute(resultSet);
        } catch (SqlException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadPermission() {
        Permissions.clear();
        ResultSet results;
        try {
            results =  Data.database.getData("permission_rank");
            while (results.next()) new Permissions(results);
        } catch (SQLException e) {
            //
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }


    public static void loadChannel() {
        Channel.clear();
        ResultSet results;
        try {
            results =  Data.database.getData("channel");
            while (results.next()) new Channel(results);
        } catch (SQLException e) {
            //
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }

    public static void loadBan() {
        Ban.clear();
        try {
            ResultSet resultSet =  Data.database.getData("ban");
            while (resultSet.next()) new Ban(resultSet);
        } catch (SqlException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadBanChannel() {
        BanChannel.clear();
        try {
            ResultSet resultSet =  Data.database.getData("ban_channel");
            while (resultSet.next()) new BanChannel(resultSet);
        } catch (SqlException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadSetting() {
        ResultSet results;
        try {
            results = Data.database.getData("setting");

            while (results.next()) new net.vorps.api.utils.Settings(results);
        } catch (SQLException e) {
            //
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }



    /*public static ArrayList<String> getPlayer(String... namePlayerList) {
        ArrayList<String> namePlayerList1 = new ArrayList<>(Arrays.asList(namePlayerList));
        namePlayerList1.add("CONSOLE");
        ArrayList<String> player = new ArrayList<>();
        boolean state;
        boolean state1 = true;
        for (String playerName : DataBungee.getListPlayerString().keySet()) {
            state = !namePlayerList1.contains(playerName);

            if (state) for (String player1 : namePlayerList1)
                if (DataBungee.isUUID(player1) && namePlayerList1.contains(DataBungee.getUUIDPlayer(playerName).toString()))
                    state1 = false;
            if (state && state1) player.add(playerName);
        }
        return player;
    }*/

}
