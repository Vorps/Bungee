package net.vorps.bungee.objects;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vorps.api.databases.Database;
import net.vorps.api.databases.DatabaseManager;
import net.vorps.bungee.Bungee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Member {

    public enum State{
        PENDING, ACCEPTED;
    }

    private static final long TIME_TO_REMOVE = 259200000000L;

    private @Getter final UUID uuid;
    private final long date;
    private @Getter State state;
    private @Setter boolean isEnable;

    public Member(UUID uuid, State state, long date){
        this.uuid = uuid;
        this.date  = date;
        this.state = state;
    }

    public Member(ResultSet resultSet, int nb_uuid) throws SQLException{
        this(UUID.fromString(resultSet.getString(nb_uuid)), State.valueOf(resultSet.getString(3)), resultSet.getTimestamp(4).getTime());
    }

    public void accept(UUID sender) {
        try {
            Database.BUNGEE.getDatabase().updateTable("member", "mem_uuid_1 = '" + sender + "' && mem_uuid_2 = '" + this.uuid + "'", new DatabaseManager.Values("mem_state", State.ACCEPTED.name()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void add(UUID uuid, Member member) {
        try {
            Database.BUNGEE.getDatabase().insertTable("member", uuid, member.uuid, member.state.name(), member.date);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(UUID sender) {
        try {
            Database.BUNGEE.getDatabase().delete("member", "men_uuid_1 = '" + sender+ "' || mem_uuid_2 = '" +  this.uuid+ " || men_uuid_1 = '" + this.uuid+ "' || mem_uuid_2 = '" +  sender+ "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        ProxiedPlayer proxiedPlayer = Bungee.getInstance().getProxy().getPlayer(this.uuid);
        if(proxiedPlayer != null) proxiedPlayer.sendMessage(new TextComponent(message));
    }

    public void update(UUID sender){
        switch (this.state){
            case PENDING:
                if(this.date >= System.currentTimeMillis() - Member.TIME_TO_REMOVE) this.remove(sender);
            case ACCEPTED:
                break;
        }
    }
}
