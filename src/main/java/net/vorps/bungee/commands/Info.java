package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.data.Data;
import net.vorps.bungee.objects.BanSystem;
import net.vorps.bungee.players.PlayerData;
import net.vorps.api.lang.Lang;

import java.util.UUID;

public class Info {

    @CommandPermission(value = "sender", console = false)
    public static void info(CommandSender commandSender){
        Info.info(commandSender, new Player() {
            @Override
            public void sendMessage(String s, Lang.Args... args) {
                commandSender.sendMessage(s, args);
            }

            @Override
            public String getName() {
                return commandSender.getName();
            }

            @Override
            public UUID getUUID() {
                return commandSender.getUUID();
            }
        });
    }

    @CommandPermission("player")
    public static void info(CommandSender commandSender, @CommandParameter("player") Player player){
        Info.infoFunction(commandSender, player);
    }

    private static void infoFunction(CommandSender commandSender, Player player){
        commandSender.sendMessage("§e------- §a"+player.getName()+"§e -------");
        if(PlayerData.isPlayerDataCore(player.getUUID())) commandSender.sendMessage("CMD.INFO.ONLINE");
        else {
            commandSender.sendMessage("CMD.INFO.OFFLINE");
            commandSender.sendMessage("CMD.INFO.DATE_LAST", new Lang.Args(Lang.Parameter.VAR, Data.FORMAT_DAY_MONTH_YEAR_HOUR_MINUTE_SECOND.format(PlayerData.getDateLast(player.getUUID()))));
        }
        commandSender.sendMessage("CMD.INFO.DATE_FIRST", new Lang.Args(Lang.Parameter.VAR, Data.FORMAT_DAY_MONTH_YEAR_HOUR_MINUTE_SECOND.format(PlayerData.getDateFirst(player.getUUID()))));
        if(commandSender.getUUID() == player.getUUID() || commandSender.hasPermission("info.admin")) commandSender.sendMessage("CMD.INFO.IP", new Lang.Args(Lang.Parameter.VAR, PlayerData.getIp(player.getUUID())));

        commandSender.sendMessage("CMD.INFO.RANK", new Lang.Args(Lang.Parameter.VAR, PlayerData.getRank(player.getUUID()).toString()));
        Money.gets(commandSender, player);

        /*Friends friends = new Friends(player);
        String friendsMessage = this.getMessage(new StringBuilder(Lang.getMessage("BUNGEE.CMD.INFO.SENDER.FRIENDS", super.getLang(), new Lang.Args(Lang.Parameter.VAR, ""+friends.getFriends().size()))), friends.getFriends().keySet(), friends).toString();
        if(!friends.getFriends().isEmpty()) super.getSender().sendMessage(new TextComponent(friendsMessage.substring(0, friendsMessage.length()-3)+"§e]"));
        Party party = new Party(player);
        if(party.isState()){
            String partyMessage = this.getMessage(new StringBuilder(Lang.getMessage("BUNGEE.CMD.INFO.SENDER.PARTY", super.getLang(), new Lang.Args(Lang.Parameter.NAME, party.getName()), new Lang.Args(Lang.Parameter.VAR, ""+party.getMembers().size()))), friends.getFriends().keySet(), friends).toString();
            super.getSender().sendMessage(new TextComponent(partyMessage.substring(0, partyMessage.length()-3)+"§e]"));
        }*/
        String mute = BanSystem.isBan(player.getUUID(), BanSystem.TypeBan.MUTE);
        if(mute != null)
            commandSender.sendMessage(mute);
        String ban = BanSystem.isBan(player.getUUID(), BanSystem.TypeBan.BAN);
        if(ban != null)
            commandSender.sendMessage(ban);

    }
}
