package managers;

import commands.*;

import java.util.HashMap;
import java.util.Map;

/**
 * CommandInvoker отвечает за выполнение команд
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class CommandInvoker {
    private final Map<String, Command> commandMap = new HashMap<>();

    public void register(Command command) {
        commandMap.put(command.getName(), command);
    }

    public void execute(String name) {
        commandMap.get(name).execute();
    }

    public void executeWithSecondParametr(String name, String element) {
        commandMap.get(name).execute(element);
    }

    public Map<String, Command> getCommandMap() {
        return commandMap;
    }
}