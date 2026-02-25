package commands;

import java.util.List;

import collections.Dragon;
import managers.FileManager;

/**
 * Команда загрузки экземпляров в коллекцию из файла
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Execute_script implements Command {
    private List<Dragon> collections;

    public Execute_script(List<Dragon> collections) {
        this.collections = collections;
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "execute_script";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Считать и исполнить скрипт из указанного файла";
    }

    /**
     * Выполняет запус loadFromFile() метода из FileManager
     * 
     * @param args args[0] - Имя/путь к файлу
     */
    @Override
    public void execute(String... args) {
        if (args.length == 0) {
            System.out.println("Имя файла не может быть пустым");
            return;
        }
        FileManager pepaShnele = new FileManager(args[0]);
        collections.addAll(pepaShnele.loadFromFile());
    }
}
