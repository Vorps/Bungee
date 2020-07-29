package net.vorps.bungee.objects;

import lombok.Getter;
import net.vorps.api.Exceptions.SqlException;
import net.vorps.api.data.Data;
import net.vorps.api.databases.Database;
import net.vorps.bungee.DataBungee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Project Hub Created by Vorps on 01/02/2016 at 01:44.
 */
public class Commands {

    private @Getter String command;
    private @Getter ArrayList<String> alias;

    public Commands(ResultSet result) throws SQLException, SqlException {
        this.alias = new ArrayList<>();
        this.command = result.getString(1);
        ResultSet results;
        results = Database.BUNGEE.getDatabase().getData("alias", "a_command = '" + this.command + "'");
        while (results.next()) this.alias.add(result.getString("a_name"));
        Commands.commandList.put(this.command, this);
    }

    private static @Getter
    HashMap<String, Commands> commandList = new HashMap<>();

    static {
        Commands.commandList = new HashMap<>();
        DataBungee.loadCommands();
    }

    public static boolean isExistCommand(String nameCommand) {
        return Commands.commandList.containsKey(nameCommand);
    }

    public static boolean isExistAlias(String alias) {
        boolean state = false;
        for (Commands aliasCommand : Commands.commandList.values())
            if (aliasCommand.alias.contains(alias)) {
                state = true;
                break;
            }
        return state;
    }

    public static String getCommandAlias(String alias) {
        String command = alias;
        for (Commands aliasCommand : Commands.commandList.values())
            if (aliasCommand.alias.contains(alias)) {
                command = aliasCommand.command;
                break;
            }
        return command;
    }

    public static void clear() {
        Commands.commandList.clear();
    }

    public static String getCommand(String message) {
        String alias = null;
        String command = null;
        int i = 1;
        for (; i < message.length() && message.toLowerCase().charAt(i) != ' '; i++) ;
        if (i > 1) alias = message.toLowerCase().substring(1, i);
        if (alias != null && Commands.isExistAlias(alias))
            command = "/" + Commands.getCommandAlias(alias) + message.substring(i);
        else if (Commands.isExistCommand(alias)) command = message;
        return command;
    }

}
