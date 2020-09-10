package net.vorps.bungee.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.vorps.api.data.Data;
import net.vorps.api.lang.Lang;

import java.util.Date;
import java.util.UUID;

public class KickSystem {

    @AllArgsConstructor
    public enum TypeBan{
        BAN("PARDON"),MUTE("UNMUTE"),CHANNEL("PARDON_CHANNEL"), KICK("");

        private final @Getter String pardon;
    }

    protected final UUID uuid;
    protected final UUID author;
    protected final String reason;
    protected final TypeBan typeBan;

    public KickSystem(UUID uuid, UUID author, String reason, TypeBan typeBan) {
        this.uuid = uuid;
        this.author = author;
        this.reason = reason;
        this.typeBan = typeBan;
    }

    public String toString(String lang) {
        String message;
        if(this.reason.length() == 0) message = Lang.getMessage("CMD."+this.typeBan.name()+".PLAYER", lang, new Lang.Args(Lang.Parameter.AUTHOR, Data.getNamePlayer(author)));
        else message = Lang.getMessage("CMD."+this.typeBan.name()+".PLAYER.REASON", lang, new Lang.Args(Lang.Parameter.AUTHOR, Data.getNamePlayer(author)), new Lang.Args(Lang.Parameter.REASON, reason));
        return message;
    }

}
