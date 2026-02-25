package commands;

import managers.*;

/**
 * Команда справка
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Help implements Command {
    private CommandInvoker invoker;

    public Help(CommandInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "help";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Справка по доступным командам";
    }

    /**
     * Выполняет проход по всем командам и оформляет кабанчиком конкантенацию
     * getName() и description() команды
     */
    @Override
    public void execute(String... args) {
        for (Command entry : invoker.getCommandMap().values()) {
            System.out.println(entry.getName() + " - " + entry.description());
        }
    }

}
