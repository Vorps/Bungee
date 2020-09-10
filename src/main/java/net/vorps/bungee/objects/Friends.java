package net.vorps.bungee.objects;

import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.api.lang.Lang;
import net.vorps.api.objects.InteractMessage;
import net.vorps.bungee.Bungee;
import net.vorps.bungee.players.PlayerData;

import java.util.UUID;

/**
 * Project Bungee Created by Vorps on 24/02/2016 at 03:34.
 */
public class Friends extends MemberSystem{

    public Friends(UUID uuid, boolean isEnable) {
        super(uuid, isEnable);
    }

    @Override
    public int getMaxMember() {
        return PlayerData.getRank(this.uuid).getNumbersFriends();
    }

    @Override
    public String add(UUID friend) {
        String lang = PlayerData.getLang(friend);
        PlayerData.sendMessage(friend, "CMD.FRIENDS.ADD.PLAYER", new Lang.Args(Lang.Parameter.PLAYER, Data.getNamePlayer(this.uuid)));
        if(PlayerData.isPlayerDataCore(friend)){
            Bungee.getInstance().getProxy().getPlayer(friend).sendMessage((InteractMessage.getInteractMessage("CMD.FRIENDS.ADD").get("CMD.FRIENDS.ADD;PLAYER:"+this.uuid, lang)));
        }
        return Lang.getMessage("CMD.FRIENDS.ADD.SENDER", PlayerData.getLang(this.uuid), new Lang.Args(Lang.Parameter.PLAYER, Data.getNamePlayer(friend)));
    }
}
