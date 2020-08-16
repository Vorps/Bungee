package net.vorps.bungee.objects;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vorps.api.Exceptions.SqlException;
import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.api.databases.DatabaseManager;
import net.vorps.api.lang.Lang;
import net.vorps.api.utils.Settings;
import net.vorps.api.utils.StringBuilder;
import net.vorps.bungee.DataBungee;
import net.vorps.bungee.players.PlayerData;
import net.vorps.bungee.utils.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Project Bungee Created by Vorps on 02/03/2017 at 15:56.
 */
public class Channel {

    private @Getter final String name;
    private @Getter String label;
    private @Getter String lore;
    private @Getter String messageJoin;
    private @Getter String messageLeave;
    private @Getter ArrayList<String> admin;
    private @Getter ArrayList<String> member;
    private @Getter boolean pub;
    private @Getter ArrayList<String> visibility;
    private @Getter ArrayList<String> players;
    private @Getter Date date;


    public boolean isAdmin(UUID uuid) {
        return this.admin.contains(uuid.toString());
    }

    public boolean isPrimitive() {
        return this.name.equals(Channel.FRIENDS) || this.name.equals(Channel.ALL) || this.name.equals(Channel.PARTY) || this.name.equals(Channel.ALERT);
    }

    public boolean isMember(UUID uuid) {
        return this.member.contains(uuid.toString());
    }

    public Channel(String name, String label, String player, String server, boolean pub) {
        this.name = name;
        this.label = label;
        this.lore = "BUNGEE.CMD.CHAT.CHANNEL.MESSSAGE_LORE";
        this.messageJoin = "BUNGEE.CMD.CHAT.CHANNEL.MESSSAGE_JOIN";
        this.messageLeave = "BUNGEE.CMD.CHAT.CHANNEL.MESSSAGE_LEAVE";
        this.admin = new ArrayList<>();
        this.admin.add(Data.getUUIDPlayer(player).toString());
        this.member = new ArrayList<>();
        this.member.add(Data.getUUIDPlayer(player).toString());
        this.pub = pub;
        this.visibility = new ArrayList<>();
        this.visibility.add(server);
        this.players = new ArrayList<>();
        this.date = new Date(System.currentTimeMillis());
        Channel.channelList.put(this.name, this);
    }

    public Channel(ResultSet resultSet) throws SQLException {
        this.name = resultSet.getString( 1);
        this.label = resultSet.getString( 2);
        this.lore = resultSet.getString( 3);
        this.messageJoin = resultSet.getString( 4);
        this.messageLeave = resultSet.getString( 5);
        this.admin = StringBuilder.convert(new StringBuilder(resultSet.getString( 6), ",").getArgs());
        this.member = StringBuilder.convert(new StringBuilder(resultSet.getString( 7), ",").getArgs());
        this.pub = resultSet.getBoolean( 8);
        this.visibility = StringBuilder.convert(new StringBuilder(resultSet.getString( 9), ",").getArgs());
        this.date = resultSet.getTimestamp( 10);
        this.players = new ArrayList<>();
        Channel.channelList.put(this.name, this);
    }

    /*public static Channel getChannel(String[] args, String sender) {
        Channel channel = PlayerData.getPlayerData(sender).getChannel();
        if (args.length > 1 && Channel.isChannel(args[1])) channel = Channel.getChannel(args[1]);
        return channel;
    }

    public void join(ProxiedPlayer player, String servers, boolean state) {
        if (this.visibility.contains(Servers.getTypeServer(servers))) {
            if (BanSystem.isBan(player.getName(), BanSystem.TypeBan.CHANNEL) != null) {
                PlayerData playerData = PlayerData.getPlayerData(player.getName());
                if (!this.players.contains(Data.getUUIDPlayer(player.getName()).toString())) {
                    if ((this.pub || this.member.contains(player.getUniqueId().toString()) && (!this.name.equals(Channel.PARTY) || playerData.getParty().isState()) && (!this.name.equals(Channel.FRIENDS) || !playerData.getFriends().getFriends().isEmpty()))) {
                        if (state) playerData.getChannel().disable(player);
                        playerData.setChannel(this);
                        this.enable(player);
                    } else
                        player.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.JOIN.ERROR_1", playerData.getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
                } else
                    player.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.JOIN.ERROR_0", playerData.getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
            }
        } else
            player.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.JOIN.ERROR_2", PlayerData.getPlayerData(player.getName()).getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
    }

    public void disable(ProxiedPlayer player) {
        PlayerData playerData = PlayerData.getPlayerData(player.getName());
        player.sendMessage(new TextComponent(Lang.isLang(this.messageLeave) ? Lang.getMessage(this.messageLeave, playerData.getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name)) : this.messageLeave));
        this.players.remove(Data.getUUIDPlayer(player.getName()).toString());
        if (this.admin.contains(Data.getUUIDPlayer(player.getName()).toString()))
            this.players.forEach((String p) -> ProxyServer.getInstance().getPlayer(Data.getNamePlayer(p)).sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.DISABLE.ADMIN", PlayerData.getPlayerData(Data.getNamePlayer(p)).getLang(), new Lang.Args(Lang.Parameter.PLAYER, PlayerData.getPlayerData(player.getName()).toString())))));
        else if (Rank.getRank(playerData.getRank()).isVisibleRank())
            this.players.forEach((String p) -> ProxyServer.getInstance().getPlayer(Data.getNamePlayer(p)).sendMessage(new TextComponent(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.DISABLE.GRADE", PlayerData.getPlayerData(Data.getNamePlayer(p)).getLang(), new Lang.Args(Lang.Parameter.PLAYER, PlayerData.getPlayerData(player.getName()).toString()))))));
    }

    private void enable(ProxiedPlayer player) {
        PlayerData playerData = PlayerData.getPlayerData(player.getName());
        player.sendMessage(new TextComponent(Lang.isLang(this.messageJoin) ? Lang.getMessage(this.messageJoin, playerData.getLang(), new Lang.Args(Lang.Parameter.CHANNEL, this.name)) : this.messageJoin));
        if (this.admin.contains(Data.getUUIDPlayer(player.getName()).toString()))
            this.players.forEach((String p) -> ProxyServer.getInstance().getPlayer(Data.getNamePlayer(p)).sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ENABLE.ADMIN", PlayerData.getPlayerData(Data.getNamePlayer(p)).getLang(), new Lang.Args(Lang.Parameter.PLAYER, PlayerData.getPlayerData(player.getName()).toString())))));
        else if (Rank.getRank(playerData.getRank()).isVisibleRank())
            this.players.forEach((String p) -> ProxyServer.getInstance().getPlayer(Data.getNamePlayer(p)).sendMessage(new TextComponent(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ENABLE.GRADE", PlayerData.getPlayerData(Data.getNamePlayer(p)).getLang(), new Lang.Args(Lang.Parameter.PLAYER, PlayerData.getPlayerData(player.getName()).toString()))))));
        this.players.add(Data.getUUIDPlayer(player.getName()).toString());
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

    public static void create(CommandSender sender, String lang, String[] args) {
        String channel = args[1];
        String label = args[2];
        if (!Channel.isChannel(channel)) {
            String channel1 = Chat.getMessage(channel, "&");
            String label1 = Chat.getMessage(label);
            ChatColor.chatColor(sender, label1, "server.chat.color");
            if ((!channel1.isEmpty() && label.length() <= 10) && (!label1.isEmpty() && label1.length() <= 25)) {
                Channel channel2 = new Channel(channel1, label1, sender.getName(), (sender instanceof ProxiedPlayer ? Servers.getTypeServer((((ProxiedPlayer) sender).getServer().getInfo().getName())) : Channel.SERVER), false);
                try {
                    Database.BUNGEE.getDatabase().insertTable("channel", channel2.name, channel2.label, channel2.lore, channel2.messageJoin, channel2.messageLeave, new StringBuilder(channel2.admin.toArray(new String[channel2.admin.size()]), ",", 0).getString(), new StringBuilder(channel2.member.toArray(new String[channel2.member.size()]), ",", 0).getString(), channel2.pub, new StringBuilder(channel2.visibility.toArray(new String[channel2.visibility.size()]), ",", 0).getString(), channel2.date);
                } catch (SqlException e) {
                    e.printStackTrace();
                }
                if (sender instanceof ProxiedPlayer)
                    channel2.join((ProxiedPlayer) sender, ((ProxiedPlayer) sender).getServer().getInfo().getName(), true);
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.CREATE.SENDER", lang)));
            } else
                sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.CREATE.ERROR_1", lang)));
        } else
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.CREATE.ERROR_0", lang, new Lang.Args(Lang.Parameter.CHANNEL, channel))));
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
    private static @Getter
    HashMap<String, Channel> channelList;


    public static void clear() {
        Channel.channelList.clear();
    }

    private static final String PRIMITIVE[][] = new String[][]{{"ALL", "", "HUB"}, {"ALERT", "§4ALERT", "HUB"}, {"PARTY", "§bParty", "HUB"}, {"FRIENDS", "§bFriends", "HUB"}};
    public static final String ALL = "ALL";
    public static final String ALERT = "ALERT";
    public static final String PARTY = "PARTY";
    public static final String FRIENDS = "FRIENDS";

    private static final String SERVER = "HUB";

    static {
        Channel.channelList = new HashMap<>();
        DataBungee.loadChannel();
        Channel.setup();
    }

    public static void setup() {
        for (String[] primitive : PRIMITIVE)
            new Channel(primitive[0], primitive[1], Settings.getConsole(), primitive[2], true);
    }

    public static boolean isChannel(String channel) {
        return Channel.channelList.containsKey(channel);
    }

    public static Channel getChannel(String channel) {
        return Channel.channelList.get(channel);
    }

    private boolean isAdmin(CommandSender sender, String lang) {
        boolean state = this.isAdmin(Data.getUUIDPlayer(sender.getName()));
        if (!state)
            sender.sendMessage(new TextComponent(Lang.getMessage("BUNGEE.CMD.CHAT.CHANNEL.ERROR.ADMIN", lang, new Lang.Args(Lang.Parameter.CHANNEL, this.name))));
        return state;
    }

    public static void onDisable() {
        for (Channel channel : Channel.channelList.values()) {
            try {
                Database.BUNGEE.getDatabase().updateTable("channel", "cn_name = '" + channel.name + "'", new DatabaseManager.Values("cn_label", channel.label), new DatabaseManager.Values("cn_lore", channel.lore), new DatabaseManager.Values("cn_message_join", channel.messageJoin), new DatabaseManager.Values("cn_message_leave", channel.messageLeave), new DatabaseManager.Values("cn_admin", new StringBuilder(channel.admin.toArray(new String[channel.admin.size()]), ",", 0).getString()), new DatabaseManager.Values("cn_member", new StringBuilder(channel.member.toArray(new String[channel.member.size()]), ",", 0).getString()), new DatabaseManager.Values("cn_public", channel.pub), new DatabaseManager.Values("cn_visibility", new StringBuilder(channel.visibility.toArray(new String[channel.visibility.size()]), ",", 0).getString()));
            } catch (SqlException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean hasAdmin(UUID uuid) {
        boolean state = false;
        for (Channel channel : Channel.channelList.values()) {
            if (channel.isAdmin(uuid)) {
                state = true;
                break;
            }
        }
        return state;
    }
}
