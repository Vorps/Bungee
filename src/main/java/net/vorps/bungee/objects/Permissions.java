package net.vorps.bungee.objects;

import net.vorps.bungee.Bungee;
import net.vorps.bungee.DataBungee;
import net.vorps.bungee.commands.Rank;
import net.vorps.bungee.players.PlayerData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

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

    public static void permissionRank(UUID uuid, String rank) {
        Permissions.permissionsList.values().forEach((Permissions permissions) -> {
            Bungee.getInstance().getProxy().getPlayer(uuid).setPermission(permissions.permission, rank.equals(permissions.rank));
        });
    }

    public static void clear() {
        Permissions.permissionsList.clear();
    }
}
