package net.vorps.bungee.objects;

import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.api.databases.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Project Bungee Created by Vorps on 24/02/2016 at 03:34.
 */
public class Party extends MemberSystem{

    private String name;
    private String lore;
    private String message_join;
    private String message_leave;
    private long date;

    public Party(UUID uuid, boolean isEnable, String name, String lore, String message_join, String message_leave, long date) {
        super(uuid, isEnable);
        this.name = name;
        this.lore = lore;
        this.message_join = message_join;
        this.message_leave = message_leave;
        this.date = date;
    }

    public Party(ResultSet resultSet) throws SQLException{
        this(UUID.fromString(resultSet.getString("party_uuid")), resultSet.getBoolean("party_is_enable"), resultSet.getString("party_name"), resultSet.getString("party_lore"), resultSet.getString("party_message_join"), resultSet.getString("party_message_leave"), resultSet.getLong("party_date"));
    }

    public void setLore(String lore) {
        try {
            Database.BUNGEE.getDatabase().updateTable("party", "party_uuid = '" + this.uuid + "'", new DatabaseManager.Values("party_lore", lore));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rename(String name) {
        try {
            Database.BUNGEE.getDatabase().updateTable("party", "party_uuid = '" + super.uuid + "'", new DatabaseManager.Values("party_name", name));
            this.name = name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void leave(){
        try {
            Database.BUNGEE.getDatabase().delete("party", "party_uuid" + this.uuid + "'");
            // todo
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getMaxMember() {
        return 0;
    }

    @Override
    public String add(UUID friend) {
        return null;
    }
}