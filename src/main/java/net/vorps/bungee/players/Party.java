package net.vorps.bungee.players;

import lombok.Getter;
import lombok.Setter;
import net.vorps.api.Exceptions.SqlException;
import net.vorps.api.data.Data;
import net.vorps.api.data.DataCore;
import net.vorps.api.databases.Database;
import net.vorps.api.databases.DatabaseManager;
import net.vorps.api.players.PlayerData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Project Bungee Created by Vorps on 24/02/2016 at 03:34.
 */
public class Party {

    private String player;
    private @Getter
    boolean state;
    private @Getter
    @Setter
    boolean enable;
    private @Getter
    String name;
    private @Getter
    String owner;
    private @Getter
    HashMap<String, Long> members;
    private @Getter
    ArrayList<String> pendingMembersList;
    private @Getter
    ArrayList<String> requestMembersList;
    private @Getter
    String message;
    private @Getter
    long date;

    {
        this.members = new HashMap<>();
        this.pendingMembersList = new ArrayList<>();
        this.requestMembersList = new ArrayList<>();
    }

    public Party(String player) {
        this.player = player;
        this.function();
        this.enable = PlayerData.isParty(Data.getUUIDPlayer(player));
    }

    public void function() {
        try {
            ResultSet result = Database.BUNGEE.getDatabase().getData("party", "party_member = '" + Data.getUUIDPlayer(this.player) + "' || party_leader = '" + Data.getUUIDPlayer(this.player) + "'");
            if (result.next()) {
                this.name = result.getString(1);
                this.owner = result.getString(5);
                this.message = result.getString(6);
                this.date = result.getTimestamp(4).getTime();
                do {
                    switch (result.getInt(3)) {
                        case 0:
                            if (this.player.equals(this.owner)) {
                                if (result.getTimestamp(7).getTime() <= System.currentTimeMillis() - 259200000000L) {
                                    this.pendingMembersList.add(result.getString(2));
                                } else {
                                    Database.BUNGEE.getDatabase().delete("party", "party_name = '" + this.name + "' && party_member = '" + result.getString(2) + "'");
                                }
                            } else {
                                if (result.getTimestamp(7).getTime() <= System.currentTimeMillis() - 259200000000L) {
                                    this.requestMembersList.add(result.getString(5));
                                } else {
                                    Database.BUNGEE.getDatabase().delete("party", "party_name = '" + this.name + "' && party_member = '" + result.getString(2) + "'");
                                }
                            }
                            break;
                        case 1:
                            this.members.put(result.getString(2), result.getTimestamp(7).getTime());
                            break;
                        default:
                            break;
                    }
                } while (result.next());
            }
            if (!this.members.isEmpty()) {
                this.state = true;
            }
        } catch (SQLException e) {
            //
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        this.members.clear();
        this.pendingMembersList.clear();
        this.requestMembersList.clear();
        this.function();
    }

    public void onDisable() {
        PlayerData.setParty(Data.getUUIDPlayer(this.player), this.enable);
    }

    public void lore(String lore) {
        try {
            Database.BUNGEE.getDatabase().updateTable("party", "party_leader = '" + Data.getUUIDPlayer(this.player) + "'", new DatabaseManager.Values("party_lore", lore));
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }

    public void rename(String name) {
        try {
            Database.BUNGEE.getDatabase().updateTable("party", "party_leader = '" + Data.getUUIDPlayer(this.player) + "'", new DatabaseManager.Values("party_name", name));
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }

    public void leave() {
        if (this.owner.equals(Data.getUUIDPlayer(this.player).toString())) {
            try {
                Database.BUNGEE.getDatabase().delete("party", "party_leader = '" + this.owner + "'");
            } catch (SqlException e) {
                e.printStackTrace();
            }
        } else this.rename(this.player);
    }


    public void remove(String member) {
        try {
            Database.BUNGEE.getDatabase().delete("party", "party_member = '" + Data.getUUIDPlayer(member) + "'");
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }

    public void accept(String leader) {
        try {
            Database.BUNGEE.getDatabase().updateTable("party", "party_leader = '" + Data.getUUIDPlayer(leader) + "' &&  party_member = '" + Data.getUUIDPlayer(this.player) + "'", new DatabaseManager.Values("party_state", 1), new DatabaseManager.Values("party_date_member", new Date(System.currentTimeMillis())));
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }

    public void add(String member, String nameParty) {
        try {
            Database.BUNGEE.getDatabase().insertTable("party", nameParty, member, 0, new Timestamp(System.currentTimeMillis()), Data.getUUIDPlayer(this.player), null, new Timestamp(System.currentTimeMillis()));
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }
}