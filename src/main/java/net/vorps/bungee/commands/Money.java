package net.vorps.bungee.commands;

import net.vorps.api.commands.CommandParameter;
import net.vorps.api.commands.CommandPermission;
import net.vorps.api.commands.CommandSender;
import net.vorps.api.commands.Player;
import net.vorps.api.lang.Lang;
import net.vorps.bungee.players.PlayerData;

public class Money {

    @CommandPermission(value = "sender", console = false)
    public static void set(CommandSender commandSender, @CommandParameter("money") String nameMoney, @CommandParameter("value") Double value){
        Money.setMoney(commandSender, nameMoney, value);
    }

    @CommandPermission(value = "sender", console = false)
    public static void add(CommandSender commandSender, @CommandParameter("money") String nameMoney, @CommandParameter("value") Double value){
        Money.setMoney(commandSender, nameMoney, PlayerData.getMoney(commandSender.getUUID(), nameMoney)+value);
    }

    @CommandPermission(value = "sender", console = false)
    public static void remove(CommandSender commandSender, @CommandParameter("money") String nameMoney, @CommandParameter("value") Double value){
        Money.setMoney(commandSender, nameMoney, PlayerData.getMoney(commandSender.getUUID(), nameMoney)-value);
    }

    @CommandPermission("player")
    public static void set(CommandSender commandSender, @CommandParameter("money") String nameMoney, @CommandParameter("player") Player player, @CommandParameter("value") Double value){
        Money.setMoney(commandSender, player, nameMoney,  value);
    }

    @CommandPermission("player")
    public static void add(CommandSender commandSender, @CommandParameter("money") String nameMoney, @CommandParameter("player") Player player, @CommandParameter("value") Double value){
        Money.setMoney(commandSender, player, nameMoney,  PlayerData.getMoney(player.getUUID(), nameMoney)+value);

    }

    @CommandPermission("player")
    public static void remove(CommandSender commandSender, @CommandParameter("money") String nameMoney, @CommandParameter("player") Player player, @CommandParameter("value") Double value){
        Money.setMoney(commandSender, player, nameMoney,  PlayerData.getMoney(player.getUUID(), nameMoney)-value);
    }

    @CommandPermission(value = "sender", console = false)
    public static void gets(CommandSender commandSender){
        commandSender.sendMessage("CMD.MONEY.GET.SENDER");
        for(String nameMoney : net.vorps.api.objects.Money.getMoneys()){
            commandSender.sendMessage("MONEY.GET", new Lang.Args(Lang.Parameter.MONEY, nameMoney), new Lang.Args(Lang.Parameter.VAR, Double.toString(PlayerData.getMoney(commandSender.getUUID(), nameMoney))));
        }
    }

    @CommandPermission("sender")
    public static void get(CommandSender commandSender, @CommandParameter("money") String nameMoney){
        commandSender.sendMessage("CMD.MONEY.GET.SENDER");
        commandSender.sendMessage("MONEY.GET", new Lang.Args(Lang.Parameter.MONEY, nameMoney), new Lang.Args(Lang.Parameter.VAR, Double.toString(PlayerData.getMoney(commandSender.getUUID(), nameMoney))));
    }

    @CommandPermission("player")
    public static void gets(CommandSender commandSender, @CommandParameter("player") Player player){
        commandSender.sendMessage("CMD.MONEY.GET.PLAYER", new Lang.Args(Lang.Parameter.PLAYER, player.getName()));
        for(String nameMoney : net.vorps.api.objects.Money.getMoneys()){
            commandSender.sendMessage("MONEY.GET", new Lang.Args(Lang.Parameter.MONEY, nameMoney), new Lang.Args(Lang.Parameter.VAR, Double.toString(PlayerData.getMoney(player.getUUID(), nameMoney))));
        }
    }

    @CommandPermission("player")
    public static void get(CommandSender commandSender, @CommandParameter("money") String nameMoney, @CommandParameter("player") Player player){
        commandSender.sendMessage("CMD.MONEY.GET.PLAYER", new Lang.Args(Lang.Parameter.PLAYER, player.getName()));
        commandSender.sendMessage("MONEY.GET", new Lang.Args(Lang.Parameter.MONEY, nameMoney), new Lang.Args(Lang.Parameter.VAR, Double.toString(PlayerData.getMoney(player.getUUID(), nameMoney))));
    }

    private static void setMoney(CommandSender commandSender, Player player, String nameMoney, double futureValue){
        if(futureValue > 0){
            PlayerData.setMoney(player.getUUID(), nameMoney, futureValue);
            Money.get(commandSender, nameMoney, player);
            player.sendMessage("CMD.MONEY.SET.PLAYER", new Lang.Args(Lang.Parameter.PLAYER, commandSender.getName()));
            player.sendMessage("CMD.MONEY.GET.SENDER");
            player.sendMessage("MONEY.GET", new Lang.Args(Lang.Parameter.MONEY, nameMoney), new Lang.Args(Lang.Parameter.VAR, Double.toString(PlayerData.getMoney(player.getUUID(), nameMoney))));
        } else commandSender.sendMessage("CMD.MONEY.PLAYER.ERROR", new Lang.Args(Lang.Parameter.PLAYER, player.getName()), new Lang.Args(Lang.Parameter.VAR, Double.toString(futureValue)));
    }

    private static void setMoney(CommandSender commandSender, String nameMoney, double futureValue){
        if(futureValue > 0){
            PlayerData.setMoney(commandSender.getUUID(), nameMoney, futureValue);
            Money.get(commandSender, nameMoney);
        } else commandSender.sendMessage("CMD.MONEY.ERROR", new Lang.Args(Lang.Parameter.VAR, Double.toString(futureValue)));
    }
}
