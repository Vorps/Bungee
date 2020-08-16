package net.vorps.bungee;

import net.vorps.api.Exceptions.SqlException;
import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.api.databases.DatabaseManager;
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

    public static void loadServerType() {
        ServersType.clear();
        try {
            ResultSet resultSet = Database.BUNGEE.getDatabase().getData("server_type");
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
            ResultSet resultSet = Database.BUNGEE.getDatabase().getData("server");
            while (resultSet.next()){
                new Servers(resultSet);
            }
        } catch (SqlException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadCommands() {
        Commands.clear();
        try {
            ResultSet resultSet =  Database.BUNGEE.getDatabase().getData("command");
            while (resultSet.next()) new Commands(resultSet);
        } catch (SqlException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadPermission() {
        Permissions.clear();
        ResultSet results;
        try {
            results =  Database.BUNGEE.getDatabase().getData("permission_rank");
            while (results.next()) new Permissions(results);
        } catch (SQLException e) {
            //
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }

    public static void loadChat() {
        Chat.clear();
        try {
            ResultSet resultSet = Database.BUNGEE.getDatabase().getData("chat");
            while (resultSet.next()) new Chat(resultSet);
        } catch (SqlException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadBanSystem() {
        try {
            ResultSet resultSet =  Database.BUNGEE.getDatabase().getData("ban_system");
            while (resultSet.next()) new BanSystem(resultSet);
        } catch (SqlException | SQLException e) {
            e.printStackTrace();
        }
    }


    public static void loadChannel() {
        Channel.clear();
        ResultSet results;
        try {
            results =   Database.BUNGEE.getDatabase().getData("channel");
            while (results.next()) new Channel(results);
        } catch (SQLException e) {
            //
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }


}
