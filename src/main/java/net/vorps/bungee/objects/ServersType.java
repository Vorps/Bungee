package net.vorps.bungee.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServersType extends net.vorps.dispatcher.ServerType {

    public ServersType(ResultSet result) throws SQLException {
        super(result.getString(1), result.getInt(2), result.getInt(3), result.getInt(4));
    }

    public static void clear() {
        ServersType.serversType.clear();
    }


}
