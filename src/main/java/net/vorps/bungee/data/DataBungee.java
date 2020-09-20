package net.vorps.bungee.data;

import net.vorps.api.data.DataCore;
import net.vorps.api.data.DataReload;
import net.vorps.api.databases.Database;
import net.vorps.bungee.objects.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Project Bungee Created by Vorps on 07/02/2017 at 18:40.
 */
public class DataBungee extends DataCore {

    @DataReload
    public static void loadServerType() {
        ServersType.clear();
        try {
            ResultSet resultSet = Database.BUNGEE.getDatabase().getData("server_type");
            while (resultSet != null && resultSet.next()){
                new ServersType(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @DataReload
    public static void loadServer() {
        Servers.clear();
        try {
            ResultSet resultSet = Database.BUNGEE.getDatabase().getData("server");
            while (resultSet != null && resultSet.next()){
                new Servers(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @DataReload
    public static void loadCommands() {
        Commands.clear();
        try {
            ResultSet resultSet =  Database.BUNGEE.getDatabase().getData("command");
            while (resultSet != null && resultSet.next()) new Commands(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @DataReload
    public static void loadPermission() {
        Permissions.clear();
        ResultSet resultSet;
        try {
            resultSet =  Database.BUNGEE.getDatabase().getData("permission_rank");
            while (resultSet != null && resultSet.next()) new Permissions(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @DataReload
    public static void loadChat() {
        Chat.clear();
        try {
            ResultSet resultSet = Database.BUNGEE.getDatabase().getData("chat");
            while (resultSet != null && resultSet.next()) new Chat(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @DataReload
    public static void loadBanSystem() {
        try {
            ResultSet resultSet =  Database.BUNGEE.getDatabase().getData("ban_system");
            while (resultSet != null && resultSet.next()) new BanSystem(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @DataReload
    public static void loadChannel() {
        Channel.clear();
        ResultSet resultSet;
        try {
            resultSet = Database.BUNGEE.getDatabase().getData("channel");
            while (resultSet != null && resultSet.next()) new Channel(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
