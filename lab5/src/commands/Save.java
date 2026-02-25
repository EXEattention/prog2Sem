package commands;

import java.util.Stack;

import collections.Dragon;
import managers.FileManager;

/**
 * Команда сохранения коллекции в XML формат
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Save implements Command {
    private String wayToFile;
    private Stack<Dragon> collection;

    public Save(String wayToFile, Stack<Dragon> collection) {
        this.wayToFile = wayToFile;
        this.collection = collection;
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "save";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Сохранить в коллекцию";
    }

    /**
     * Выполняет сохранение коллекции в XML через метод saveToFile() в классе
     * FileManager
     */
    @Override
    public void execute(String... args) {
        FileManager file = new FileManager(wayToFile);
        file.saveToFile(collection);
    }
}
