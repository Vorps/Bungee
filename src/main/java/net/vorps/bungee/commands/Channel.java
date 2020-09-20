package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandSender;
import net.vorps.bungee.objects.BanSystem;
import net.vorps.bungee.objects.Chat;
import net.vorps.bungee.objects.Member;
import net.vorps.bungee.utils.ChatColor;
import net.vorps.api.lang.Lang;
public class Channel {

    public static void create(CommandSender commandSender, @CommandParameter("name")  String nameChannel, @CommandParameter("label") String labelChat) {
        if (!net.vorps.bungee.objects.Channel.isChannel(nameChannel)) {
            if ((!nameChannel.isEmpty() && nameChannel.length() <= 10) && (!labelChat.isEmpty() && labelChat.length() <= 25)) {
                net.vorps.bungee.objects.Channel channel = net.vorps.bungee.objects.Channel.createChannel(nameChannel, labelChat);
                channel.join(commandSender.getUUID());
                commandSender.sendMessage("CMD.CHANNEL.CREATE.SENDER");
            } else
                commandSender.sendMessage("CMD.CHANNEL.CREATE.INVALID_LABEL");
        } else
            commandSender.sendMessage("CMD.CHANNEL.CREATE.ALREADY_EXIST", new Lang.Args(Lang.Parameter.CHANNEL, nameChannel));
    }

/*
    public static void join(CommandSender sender, String nameChannel) {
        if (net.vorps.bungee.objects.Channel.isChannel(nameChannel)) {
            String banMessage = BanSystem.isBan(sender.getUUID(), BanSystem.TypeBan.CHANNEL);
            if (banMessage != null) {
                sender.sendMessage(banMessage);
            } else {
                net.vorps.bungee.objects.Channel channel = net.vorps.bungee.objects.Channel.getChannel(nameChannel);
                if (!channel.isMember(sender.getUUID(), Member.State.ACCEPTED)) {
                    channel.join(sender.getUUID());
                } else {
                    if (channel.isEnable()) {
                        channel.join(sender.getUUID());
                    } else {
                        //channel.add()
                    }
                }
            }
        } else {

        }
    }*/

}
