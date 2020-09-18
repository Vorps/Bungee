package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.lang.LangSetting;
import net.vorps.bungee.players.PlayerData;

public class Lang {

    @CommandPermission(value = "sender", console = false)
    public static void lang(CommandSender commandSender, @CommandParameter("LANG") String lang){
        PlayerData.setLang(commandSender.getUUID(), lang);
        commandSender.sendMessage("CMD.LANG.SENDER", new net.vorps.api.lang.Lang.Args(net.vorps.api.lang.Lang.Parameter.VAR, LangSetting.getLangSetting(lang).getNameDisplay()));
    }

    @CommandPermission("player")
    public static void lang(CommandSender commandSender, @CommandParameter("LANG") String lang, @CommandParameter("PLAYER") Player player){
        PlayerData.setLang(player.getUUID(), lang);
        commandSender.sendMessage("CMD.LANG.PLAYER.SENDER", new net.vorps.api.lang.Lang.Args(net.vorps.api.lang.Lang.Parameter.PLAYER, player.getName()), new net.vorps.api.lang.Lang.Args(net.vorps.api.lang.Lang.Parameter.VAR, LangSetting.getLangSetting(lang).getNameDisplay()));
        player.sendMessage("CMD.LANG.PLAYER", new net.vorps.api.lang.Lang.Args(net.vorps.api.lang.Lang.Parameter.PLAYER, commandSender.getName()), new net.vorps.api.lang.Lang.Args(net.vorps.api.lang.Lang.Parameter.VAR, LangSetting.getLangSetting(lang).getNameDisplay()));
    }
}
