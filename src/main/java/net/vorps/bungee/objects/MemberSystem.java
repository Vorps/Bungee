package net.vorps.bungee.objects;

import lombok.Getter;
import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.players.PlayerData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MemberSystem {

    protected final UUID uuid;
    protected @Getter boolean isEnable;
    private @Getter final HashMap<UUID, Member> members_uuid_1;
    private @Getter final HashMap<UUID, Member> members_uuid_2;

    public MemberSystem(UUID uuid, boolean isEnable){
        this.uuid = uuid;
        this.isEnable = isEnable;
        this.members_uuid_1 = new HashMap<>();
        this.members_uuid_2 = new HashMap<>();
        this.getMember_uuid_1().forEach(e -> this.members_uuid_1.put(e.getUuid(), e));
        this.getMember_uuid_2().forEach(e -> this.members_uuid_2.put(e.getUuid(), e));
        this.update();
    }

    private ArrayList<Member> getMember_uuid_1(){
        ArrayList<Member> members_uuid_1 = new ArrayList<>();
        ResultSet results;
        try {
            results = Database.BUNGEE.getDatabase().getData("member", "mem_uuid_1 = '"+ this.uuid +"'");
            while(results.next()) members_uuid_1.add(new Member(results,2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members_uuid_1;
    }

    private ArrayList<Member> getMember_uuid_2(){
        ArrayList<Member> members_uuid_2 = new ArrayList<>();
        ResultSet results;
        try {
            results = Database.BUNGEE.getDatabase().getData("member", "mem_uuid_2 = '"+ this.uuid +"'");
            while(results.next()) members_uuid_2.add(new Member(results,1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members_uuid_2;
    }

    private ArrayList<Member> getMember(){
       return Stream.concat(this.members_uuid_1.values().stream(), this.members_uuid_2.values().stream()).collect(Collectors.toCollection(ArrayList::new));
    }

    public void update(){
        for(Member member : this.getMember()) member.update(this.uuid);
    }

    public void removeAll(){
        for(Member member : this.getMember()) member.remove(this.uuid);
    }

    public void sendMessage(String message){
        for(Member member : this.getMember()) member.sendMessage(message);
    }

    public boolean isMember_uuid_1(UUID uuid, Member.State state){
        return this.members_uuid_1.containsKey(uuid) && this.members_uuid_1.get(uuid).getState() == state;
    }

    public boolean isMember_uuid_2(UUID uuid, Member.State state){
        return this.members_uuid_2.containsKey(uuid) && this.members_uuid_2.get(uuid).getState() == state;
    }

    public boolean isMember(UUID uuid, Member.State state){
        return this.isMember_uuid_1(uuid, state) || isMember_uuid_2(uuid, state);
    }

    public String addMember(UUID friend){
        this.update();
        String message;
        if(!this.uuid.equals(friend)){
            if(!this.isMember(friend, Member.State.ACCEPTED)){
                if(!this.isMember_uuid_1(friend, Member.State.PENDING)){
                    if(!this.isMember_uuid_2(friend, Member.State.PENDING)){
                        if(this.isEnable){
                            if(this.getMember().size() < this.getMaxMember()){
                                Member member = new Member(friend, Member.State.PENDING, System.currentTimeMillis());
                                this.members_uuid_1.put(friend, member);
                                Member.add(this.uuid, member);
                                message = this.add(friend);
                            } else {
                                message = Lang.getMessage("CMD.FRIENDS.ADD.MAX_MEMBER", PlayerData.getLang(this.uuid));
                            }
                        } else message = Lang.getMessage("CMD.FRIENDS.ADD.NO_ENABLE", PlayerData.getLang(this.uuid), new Lang.Args(Lang.Parameter.PLAYER, Data.getNamePlayer(uuid)));
                    } else {
                        message = "";
                    }
                } else message = Lang.getMessage("CMD.FRIENDS.ADD.ALREADY_INVITE", PlayerData.getLang(this.uuid), new Lang.Args(Lang.Parameter.PLAYER, Data.getNamePlayer(uuid)));
            } else message = Lang.getMessage("CMD.FRIENDS.ADD.PLAYER_IS_ALREADY_FRIEND", PlayerData.getLang(this.uuid), new Lang.Args(Lang.Parameter.PLAYER, Data.getNamePlayer(uuid)));
        } else message = Lang.getMessage("CMD.FRIENDS.ADD.CANT_ADD_YOURSELF", PlayerData.getLang(this.uuid));
        return message;
    }

    public abstract int getMaxMember();
    public abstract String add(UUID friend);
}
