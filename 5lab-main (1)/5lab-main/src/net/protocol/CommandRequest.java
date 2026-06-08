package net.protocol;

import java.io.Serializable;

public class CommandRequest implements Serializable {
    private String commandName;
    private Object argument;

    public CommandRequest(String commandName, Object argument) {
        this.commandName = commandName;
        this.argument = argument;
    }

    public String getCommandName() {
        return commandName;
    }

    public Object getArgument() {
        return argument;
    }
}
