package net.vorps.bungee.objects;

import net.vorps.bungee.DataBungee;
import net.vorps.bungee.players.PlayerData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Project Hub Created by Vorps on 01/02/2016 at 01:43.
 */
public class Permissions {

    private final String permission;
    private final String rank;

    public Permissions(ResultSet results) throws SQLException {
        this.permission = results.getString(1);
        this.rank = results.getString(2);
        Permissions.permissionsList.put(this.permission, this);
    }

    public static HashMap<String, Permissions> permissionsList;

    static {
        Permissions.permissionsList = new HashMap<>();
        DataBungee.loadPermission();
    }

    public static void permissionRank(PlayerData playerData) {
        Permissions.permissionsList.values().forEach((Permissions permissions) -> {
            if (playerData.getRank().getRank().equals(permissions.rank))
                playerData.getProxiedPlayer().setPermission(permissions.permission, true);
        });
    }

    public static void clear() {
        Permissions.permissionsList.clear();
    }
}
