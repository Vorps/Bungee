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
        ResultSet resultsAlias = Database.BUNGEE.getDatabase().getData("alias", "a_command = '" + this.command + "'");
        while (resultsAlias.next()) this.alias.add(resultsAlias.getString(2));
        Commands.commandList.put(this.command, this);
    }

    private static @Getter
    HashMap<String, Commands> commandList = new HashMap<>();

    static {
        Commands.commandList = new HashMap<>();
        DataBungee.loadCommands();
    }

    public static void clear() {
        Commands.commandList.clear();
    }

    public static String getCommand(String message) {
        if(Commands.commandList.containsKey(message)) return null;
        for(Commands command : Commands.commandList.values()) if(command.alias.contains(message)) return command.command;
        return null;
    }

}
