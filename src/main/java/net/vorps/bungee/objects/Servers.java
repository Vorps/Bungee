package net.vorps.bungee.objects;

import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;
import net.vorps.api.databases.Database;
import net.vorps.api.message.ServerState;
import net.vorps.api.utils.StringBuilder;
import net.vorps.bungee.Bungee;
import net.vorps.bungee.DataBungee;
import net.vorps.dispatcher.Server;

import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Project Bungee Created by Vorps on 10/02/2017 at 03:49.
 */
public class Servers extends Server {


    private @Getter ServerInfo serverInfo;

    public Servers(ResultSet result) throws SQLException {
        super(result.getString(1), result.getInt(2));
    }

    public Servers(String name, int port){
        super(name, port);
        try {
            Database.BUNGEE.getDatabase().insertTable("server", name, port, ServerState.WAITING.name());
        } catch (SQLException e){
            e.printStackTrace();
        }
        Servers.registerServer(this);
    }

    @Override
    public void removeServer(){
        super.removeServer();
        try {
            Database.BUNGEE.getDatabase().delete("server", "s_name = '" + this.name + "'");
        } catch (SQLException e){
            e.printStackTrace();
        }
        Server.servers.remove(this.name);
    }

    static {
        Server.servers = new HashMap<>();
        DataBungee.loadServer();
    }

    public static void registerServer() {
        Bungee.getInstance().getProxy().getConfig().getServers().clear();
        for (Server server : Servers.servers.values())
            registerServer((Servers) server);
    }

    private static void registerServer(Servers server){
        server.serverInfo = Bungee.getInstance().getProxy().constructServerInfo(server.getName(), new InetSocketAddress("localhost", server.getPort()), "", false);
        Bungee.getInstance().getProxy().getConfig().getServers().put(server.getName(), server.serverInfo);
    }

    public static boolean isTypeServer(String server, String type) {
        return (new StringBuilder(server, "_").getArgs()[0]).equals(type);
    }

    public static void clear() {
        Servers.servers.clear();
    }

}
