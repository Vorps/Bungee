package net.vorps.bungee.objects;

import lombok.Getter;
import net.vorps.api.databases.Database;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.DataBungee;
import net.vorps.bungee.players.PlayerData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Project Bungee Created by Vorps on 02/03/2017 at 15:56.
 */
public class Channel extends MemberSystem{

    private @Getter final String name;
    private @Getter final String label;
    private @Getter final String lore;
    private @Getter final String messageJoin;
    private @Getter final String messageLeave;
    private @Getter final long date;

    private Channel(UUID uuid, boolean isPublic, String name, String label) {
        this(uuid, isPublic, name, label,  "BUNGEE.CMD.CHAT.CHANNEL.MESSSAGE_LORE", "BUNGEE.CMD.CHAT.CHANNEL.MESSSAGE_JOIN", "BUNGEE.CMD.CHAT.CHANNEL.MESSSAGE_LEAVE", System.currentTimeMillis());
    }

    private Channel(UUID uuid, boolean isPublic, String name, String label, String lore, String messageJoin, String messageLeave, long date) {
        super(uuid, isPublic);
        this.name = name;
        this.label = label;
        this.lore = lore;
        this.messageJoin = messageJoin;
        this.messageLeave = messageLeave;
        this.date = date;
        Channel.channelList.put(this.name, this);
    }

    public Channel(ResultSet resultSet) throws SQLException {
        this(UUID.fromString(resultSet.getString("party_uuid")), resultSet.getBoolean("party_is_enable"), resultSet.getString("party_name"), resultSet.getString("party_label"), resultSet.getString("party_lore"), resultSet.getString("party_message_join"), resultSet.getString("party_message_leave"), resultSet.getTimestamp("party_date").getTime());
    }

    public void join(UUID uuid) {
        if(uuid != null){
            PlayerData.getChannel(uuid).disable(uuid);
            PlayerData.setChannel(uuid, this);
            this.enable(uuid);
        }
    }

    private void disable(UUID uuid){

    }

    private void enable(UUID uuid) {
        PlayerData playerData = PlayerData.getPlayerData(uuid);
        playerData.sendMessage(this.messageJoin, new Lang.Args(Lang.Parameter.CHANNEL, this.name));
        /*if (this.isAdmin(player.getUniqueId()))
            this.members.forEach((Member p) -> ProxyServer.getInstance().getPlayer(p.).sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ENABLE.ADMIN", PlayerData.getPlayerData(Data.getNamePlayer(p)).getLang(), new Lang.Args(Lang.Parameter.PLAYER, PlayerData.getPlayerData(player.getName()).toString())))));
        else if (Rank.getRank(playerData.getRank()).isVisibleRank())
            this.players.forEach((String p) -> ProxyServer.getInstance().getPlayer(Data.getNamePlayer(p)).sendMessage(new TextComponent(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ENABLE.GRADE", PlayerData.getPlayerData(Data.getNamePlayer(p)).getLang(), new Lang.Args(Lang.Parameter.PLAYER, PlayerData.getPlayerData(player.getName()).toString()))))));
        this.players.add(Data.getUUIDPlayer(player.getName()).toString());
*/    }


    //public void removeMember(ProxiedPlayer player) {
        /*PlayerData playerData = PlayerData.getPlayerData(player.getName());
        player.sendMessage(new TextComponent(Lang.isLang(this.messageLeave) ? Lang.getMessage(this.messageLeave, playerData.getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name)) : this.messageLeave));
        this.players.remove(Data.getUUIDPlayer(player.getName()).toString());
        if (this.admin.contains(Data.getUUIDPlayer(player.getName()).toString()))
            this.players.forEach((String p) -> ProxyServer.getInstance().getPlayer(Data.getNamePlayer(p)).sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.DISABLE.ADMIN", PlayerData.getPlayerData(Data.getNamePlayer(p)).getLang(), new Lang.Args(Lang.Parameter.PLAYER, PlayerData.getPlayerData(player.getName()).toString())))));
        else if (Rank.getRank(playerData.getRank()).isVisibleRank())
            this.players.forEach((String p) -> ProxyServer.getInstance().getPlayer(Data.getNamePlayer(p)).sendMessage(new TextComponent(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.DISABLE.GRADE", PlayerData.getPlayerData(Data.getNamePlayer(p)).getLang(), new Lang.Args(Lang.Parameter.PLAYER, PlayerData.getPlayerData(player.getName()).toString()))))));
    */
    //}


    /*public static Channel getChannel(String[] args, String sender) {
        Channel channel = PlayerData.getPlayerData(sender).getChannel();
        if (args.length > 1 && Channel.isChannel(args[1])) channel = Channel.getChannel(args[1]);
        return channel;
    }







    public void delete(CommandSender sender, String lang) {
        if (this.isAdmin(sender, lang)) {
            if (!this.isPrimitive()) {
                sender.sendMessage(new TextComponent(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.DELETE.PLAYER", lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name)))));
                if (this.pub) {
                    this.players.forEach((String player) -> ProxyServer.getInstance().getPlayer(UUID.fromString(player)).sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.DELETE.PLAYER", PlayerData.getPlayerData(player).getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name)))));
                } else {
                    for (String member : this.member) {
                        if (!member.equals(sender.getName())) {
                            if (PlayerData.isPlayerdata(Data.getNamePlayer(member))) {
                                if (!Data.getNamePlayer(member).equals(sender.getName())) {
                                    ProxyServer.getInstance().getPlayer(Data.getNamePlayer(member)).sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.DELETE.PLAYER", PlayerData.getPlayerData(Data.getNamePlayer(member)).getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
                                }
                            } else
                                net.vorps.bungee.API.player.PlayerData.addNotification(UUID.fromString(member), Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.DELETE.PLAYER", net.vorps.bungee.API.player.PlayerData.getLang(UUID.fromString(member)), new Lang.Args(Lang.Parameter.CHANNEL, this.name)), net.vorps.bungee.API.player.PlayerData.Type.BUNGEE);
                        }
                    }
                }
                this.players.forEach((String player) -> Channel.getChannel(Channel.ALL).join(ProxyServer.getInstance().getPlayer(UUID.fromString(player)), ProxyServer.getInstance().getPlayer(Data.getNamePlayer(player)).getServer().getInfo().getName(), false));
                try {
                    Database.BUNGEE.getDatabase().delete("channel", "cn_name = '" + this.name + "'");
                } catch (SqlException e) {
                    e.printStackTrace();
                }
                Channel.channelList.remove(this.name);
            } else
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.DELETE.ERROR_0", lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
        }
    }

    public void info(CommandSender sender, String lang) {
        if (!this.isPrimitive()) {
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.NAME", lang, new Lang.Args(Lang.Parameter.VAR, this.name))));
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.LABEL", lang, new Lang.Args(Lang.Parameter.VAR, ChatColor.chatColor(this.label)))));
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.LORE", lang, new Lang.Args(Lang.Parameter.VAR, Lang.isLang(this.lore) ? Lang.getMessage(this.lore, lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name), new Lang.Args(Lang.Parameter.MESSAGE, Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO." + (this.pub ? "PUBLIC" : "PRIVATE"), lang))) : ChatColor.chatColor(this.lore)))));
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.JOIN", lang, new Lang.Args(Lang.Parameter.MESSAGE, (Lang.isLang(this.messageJoin) ? Lang.getMessage(this.messageJoin, lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name)) : this.messageJoin)))));
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.LEAVE", lang, new Lang.Args(Lang.Parameter.MESSAGE, (Lang.isLang(this.messageJoin) ? Lang.getMessage(this.messageLeave, lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name)) : this.messageLeave)))));
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.DATE", lang, new Lang.Args(Lang.Parameter.VAR, Data.FORMAT_1.format(this.date)))));
            String messagePlayer = "";
            if (this.players.size() <= 5) {
                for (String player : this.players) {
                    messagePlayer += "§a" + Data.getNamePlayer(player) + "§e,";
                }
                if (!messagePlayer.isEmpty()) messagePlayer = messagePlayer.substring(0, messagePlayer.length() - 3);
            }
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.PLAYER", lang, new Lang.Args(Lang.Parameter.VAR, "" + players.size()), new Lang.Args(Lang.Parameter.MESSAGE, messagePlayer))));
            String adminMessage = "";
            if (this.admin.size() <= 5) {
                adminMessage = "§e[";
                for (String admin : this.admin) adminMessage += "§a" + Data.getNamePlayer(admin) + "§e,";
                adminMessage = adminMessage.substring(0, adminMessage.length() - 1) + "]";
            }
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.ADMIN", lang, new Lang.Args(Lang.Parameter.VAR, "" + this.admin.size()), new Lang.Args(Lang.Parameter.MESSAGE, adminMessage))));
            if (this.pub)
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.PUBLIC", lang)));
            else if (!this.member.isEmpty()) {
                String memberMessage = "";
                if (this.member.size() <= 5) {
                    memberMessage = "§e[";
                    for (String member : this.member) memberMessage += "§a" + Data.getNamePlayer(member) + "§e,";
                    memberMessage = memberMessage.substring(0, memberMessage.length() - 1) + "]";
                }
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.MEMBER", lang, new Lang.Args(Lang.Parameter.VAR, "" + this.member.size()), new Lang.Args(Lang.Parameter.MESSAGE, memberMessage))));
            }
            String serverMessage = "";
            if (this.visibility.size() <= 5) {
                serverMessage = "§e[";
                for (String server : this.visibility) serverMessage += "§a" + server + "§e,";
                serverMessage = serverMessage.substring(0, serverMessage.length() - 1) + "]";
            }
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.VISIBILITY", lang, new Lang.Args(Lang.Parameter.VAR, "" + this.visibility.size()), new Lang.Args(Lang.Parameter.MESSAGE, serverMessage))));
        }
    }

    public void kick(CommandSender sender, String lang, String player, String[] args, int start) {
        if (this.isAdmin(sender, lang)) {
            if (!sender.getName().equals(player)) {
                if (this.players.contains(Data.getUUIDPlayer(player).toString())) {
                    Channel.getChannel(Channel.ALL).join(ProxyServer.getInstance().getPlayer(player), Channel.SERVER, true);
                    BanMuteUtils kick = new BanMuteUtils(args, start);
                    ProxyServer.getInstance().getPlayer(player).sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.BAN_MUTE.PLAYER.SHOW", PlayerData.getPlayerData(player).getLang(), new Lang.Args(Lang.Parameter.MESSAGE, Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.KICK.PLAYER", PlayerData.getPlayerData(player).getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name), new Lang.Args(Lang.Parameter.PLAYER, sender.getName())) + (kick.getReason() != null ? Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.KICK.PLAYER.REASON", PlayerData.getPlayerData(player).getLang(), new Lang.Args(Lang.Parameter.MESSAGE, kick.getReason())) : "§c.")))));
                    sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.BAN_MUTE.SENDER", lang, new Lang.Args(Lang.Parameter.PLAYER, player), new Lang.Args(Lang.Parameter.MESSAGE, Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.KICK.PLAYER", PlayerData.getPlayerData(player).getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name), new Lang.Args(Lang.Parameter.PLAYER, sender.getName())) + (kick.getReason() != null ? Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.KICK.PLAYER.REASON", PlayerData.getPlayerData(player).getLang(), new Lang.Args(Lang.Parameter.MESSAGE, kick.getReason())) : "§c.")))));
                } else
                    sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.KICK.ERROR_0", lang, new Lang.Args(Lang.Parameter.PLAYER, player))));
            } else
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.KICK.ERROR.CANT_KICK_YOURSELF", lang)));
        }
    }

    public void invite(CommandSender sender, String lang, String player) {
        if (this.isAdmin(sender, lang)) {
            if (!this.pub && !this.member.contains(Data.getUUIDPlayer(player).toString())) {
                this.member.add(Data.getUUIDPlayer(player).toString());
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INVITE.SENDER", lang, new Lang.Args(Lang.Parameter.PLAYER, player), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
                if (PlayerData.isPlayerdata(player))
                    ProxyServer.getInstance().getPlayer(player).sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INVITE.PLAYER", PlayerData.getPlayerData(player).getLang(), new Lang.Args(Lang.Parameter.PLAYER, sender.getName()), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
                else
                    net.vorps.bungee.API.player.PlayerData.addNotification(Data.getUUIDPlayer(player), Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INVITE.PLAYER", net.vorps.bungee.API.player.PlayerData.getLang(Data.getUUIDPlayer(player)), new Lang.Args(Lang.Parameter.PLAYER, sender.getName()), new Lang.Args(Lang.Parameter.CHANNEL, this.name)), net.vorps.bungee.API.player.PlayerData.Type.BUNGEE);
            } else
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INVITE.ERROR_0", lang, new Lang.Args(Lang.Parameter.PLAYER, player), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
        }
    }

    public void remove(CommandSender sender, String lang, String player) {
        if (this.isAdmin(sender, lang)) {
            if (!sender.getName().equals(player)) {
                if (!this.pub && this.member.contains(Data.getUUIDPlayer(player).toString())) {
                    sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVE.SENDER", lang, new Lang.Args(Lang.Parameter.PLAYER, player), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
                    if (PlayerData.isPlayerdata(player)) {
                        Channel.getChannel(Channel.ALL).join(ProxyServer.getInstance().getPlayer(player), Channel.SERVER, true);
                        ProxyServer.getInstance().getPlayer(player).sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVE.SENDER", PlayerData.getPlayerData(player).getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
                    } else
                        net.vorps.bungee.API.player.PlayerData.addNotification(Data.getUUIDPlayer(player), Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVE.SENDER", net.vorps.bungee.API.player.PlayerData.getLang(Data.getUUIDPlayer(player)), new Lang.Args(Lang.Parameter.CHANNEL, this.name)), net.vorps.bungee.API.player.PlayerData.Type.BUNGEE);
                    this.member.remove(Data.getUUIDPlayer(player).toString());
                    if (this.admin.contains(Data.getUUIDPlayer(player).toString()))
                        this.admin.remove(Data.getUUIDPlayer(player).toString());
                } else
                    sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVE.ERROR_0", lang, new Lang.Args(Lang.Parameter.PLAYER, player), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
            } else
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVE.ERROR_1", lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
        }
    }

    public void addAdmin(CommandSender sender, String lang, String player) {
        if (this.isAdmin(sender, lang)) {
            if (this.pub || this.member.contains(Data.getUUIDPlayer(player).toString())) {
                if (!this.admin.contains(Data.getUUIDPlayer(player).toString())) {
                    this.admin.add(Data.getUUIDPlayer(player).toString());
                    sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ADDADMIN.SENDER", lang, new Lang.Args(Lang.Parameter.PLAYER, player), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
                    if (PlayerData.isPlayerdata(player))
                        ProxyServer.getInstance().getPlayer(player).sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ADDADMIN.PLAYER", PlayerData.getPlayerData(player).getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
                    else
                        net.vorps.bungee.API.player.PlayerData.addNotification(Data.getUUIDPlayer(player), Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ADDADMIN.PLAYER", PlayerData.getLang(Data.getUUIDPlayer(player)), new Lang.Args(Lang.Parameter.CHANNEL, this.name)), net.vorps.bungee.API.player.PlayerData.Type.BUNGEE);
                } else
                    sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ADDADMIN.ERROR_1", lang, new Lang.Args(Lang.Parameter.PLAYER, player), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
            } else
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ADDADMIN.ERROR_0", lang, new Lang.Args(Lang.Parameter.PLAYER, player), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
        }
    }

    public void removeAdmin(CommandSender sender, String lang, String player) {
        if (this.isAdmin(sender, lang)) {
            if (this.admin.contains(Data.getUUIDPlayer(player).toString())) {
                if (!sender.getName().equals(player)) {
                    this.admin.remove(Data.getUUIDPlayer(player).toString());
                    sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVEADMIN.SENDER", lang, new Lang.Args(Lang.Parameter.PLAYER, player), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
                    if (PlayerData.isPlayerdata(player))
                        ProxyServer.getInstance().getPlayer(player).sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVEADMIN.PLAYER", PlayerData.getPlayerData(player).getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
                    else
                        net.vorps.bungee.API.player.PlayerData.addNotification(Data.getUUIDPlayer(player), Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVEADMIN.PLAYER", PlayerData.getLang(Data.getUUIDPlayer(player)), new Lang.Args(Lang.Parameter.CHANNEL, this.name)), net.vorps.bungee.API.player.PlayerData.Type.BUNGEE);
                } else
                    sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVEADMIN.ERROR_1", lang, new Lang.Args(Lang.Parameter.PLAYER, player), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
            } else
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVEADMIN.ERROR_0", lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
        }
    }

    public void addServer(CommandSender sender, String lang, String server) {
        if (this.isAdmin(sender, lang)) {
            if (!this.visibility.contains(server)) {
                this.visibility.add(server);
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ADDSERVER.SENDER", lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name), new Lang.Args(Lang.Parameter.SERVER, server))));
            } else
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ADDSERVER.ERROR_0", lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name), new Lang.Args(Lang.Parameter.SERVER, server))));
        }
    }

    public void removeServer(CommandSender sender, String lang, String server) {
        if (this.isAdmin(sender, lang)) {
            if (this.visibility.size() >= 1) {
                if (this.visibility.contains(server)) {
                    this.visibility.remove(server);
                    sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVESERVER.SENDER", lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name), new Lang.Args(Lang.Parameter.SERVER, server))));
                } else
                    sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVESERVER.ERROR_0", lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name), new Lang.Args(Lang.Parameter.SERVER, server))));
            } else
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.REMOVESERVER.ERROR_1", lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
        }
    }


    public void config(CommandSender sender, String lang, String[] args) {
        System.out.println("config");
        String action;
        int start;
        if (Channel.isChannel(args[1])) {
            action = args[2];
            start = 3;
        } else {
            action = args[1];
            start = 2;
        }
        String message = Chat.getMessage(new StringBuilder(args, " ", start).withColor(true).getString(sender, "server.chat.channel.color"));
        if (!message.isEmpty()) {
            if (action.equalsIgnoreCase("label") && args.length == start + 1) {
                this.label = message;
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.LABEL", lang, new Lang.Args(Lang.Parameter.VAR, this.label))));
            } else if (action.equalsIgnoreCase("join")) {
                this.messageJoin = message;
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.JOIN", lang, new Lang.Args(Lang.Parameter.MESSAGE, this.messageJoin))));
            } else if (action.equalsIgnoreCase("leave")) {
                this.messageLeave = message;
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.LEAVE", lang, new Lang.Args(Lang.Parameter.MESSAGE, this.messageLeave))));
            } else if (action.equalsIgnoreCase("lore")) {
                this.lore = message;
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.LORE", lang, new Lang.Args(Lang.Parameter.VAR, this.lore))));
            } else if (action.equalsIgnoreCase("public") && args.length == start + 1) {
                this.pub = args[start].equalsIgnoreCase("true");
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO." + (this.pub ? "PUBLIC" : "PRIVATE"), lang)));
            }
        } else sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.INFO.ERROR_0", lang)));
    }
*/
    private static @Getter HashMap<String, Channel> channelList;


    public static void clear() {
        Channel.channelList.clear();
    }

    static {
        Channel.channelList = new HashMap<>();
        DataBungee.loadChannel();
    }

    public static Channel createChannel(String name, String label){
        Channel channel  = new Channel(UUID.randomUUID(), false, name, label);
         try {
            Database.BUNGEE.getDatabase().insertTable("channel", channel.uuid, channel.name, channel.isEnable, channel.label, channel.lore, channel.messageJoin, channel.messageLeave, channel.date);
        } catch (SQLException e) {
            e.printStackTrace();
        }
         return channel;
    }


    public static boolean isChannel(String channel) {
        return Channel.channelList.containsKey(channel);
    }

    public static Channel getChannel(String channel) {
        return Channel.channelList.get(channel);
    }

    @Override
    public int getMaxMember() {
        return 0;
    }

    @Override
    public String add(UUID friend) {
        return null;
    }

   /* private boolean isAdmin(CommandSender sender, String lang) {
        boolean state = this.isAdmin(Data.getUUIDPlayer(sender.getName()));
        if (!state)
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ERROR.ADMIN", lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
        return state;
    }*/

    /*public static void onDisable() {
        for (Channel channel : Channel.channelList.values()) {
            try {
                Database.BUNGEE.getDatabase().updateTable("channel", "cn_name = '" + channel.name + "'", new DatabaseManager.Values("cn_label", channel.label), new DatabaseManager.Values("cn_lore", channel.lore), new DatabaseManager.Values("cn_message_join", channel.messageJoin), new DatabaseManager.Values("cn_message_leave", channel.messageLeave), new DatabaseManager.Values("cn_admin", new StringBuilder(channel.admin.toArray(new String[channel.admin.size()]), ",", 0).getString()), new DatabaseManager.Values("cn_member", new StringBuilder(channel.member.toArray(new String[channel.member.size()]), ",", 0).getString()), new DatabaseManager.Values("cn_public", channel.pub), new DatabaseManager.Values("cn_visibility", new StringBuilder(channel.visibility.toArray(new String[channel.visibility.size()]), ",", 0).getString()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/

/*    public static boolean hasAdmin(UUID uuid) {
        boolean state = false;
        for (Channel channel : Channel.channelList.values()) {
            if (channel.isAdmin(uuid)) {
                state = true;
                break;
            }
        }
        return state;
    }*/
}
