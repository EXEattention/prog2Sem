package commands;

import java.util.List;
import java.util.Scanner;

import collections.Dragon;
import managers.*;

/**
 * Команда для добавления экземпляра в коллекцию
 * 
 * Запрашивает данные дракона черезе консоль
 * и добавляет созданный объект в коллекцию
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Add implements Command {
    /** Коллекция для добавления экземпляра */
    private List<Dragon> collection;
    /** Менеджер для чтения данных дракона */
    private DragonMeneger reader;

    /**
     * Команда добавления
     * 
     * @param collection в нее будут добавляться экземпляры
     * @param scanner    для чтения ввода
     */
    public Add(List<Dragon> collection, Scanner scanner) {
        this.collection = collection;
        reader = new DragonMeneger(scanner);
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "add";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Добавить элемент в коллекцию";
    }

    /**
     * Добавление экземпляра в коллекцию
     * 
     * Запрашивает ввод всех полей экземпляра
     * Создает и добавляет в коллекцию
     * 
     * @param (не используется)
     */
    @Override
    public void execute(String... args) {
        Dragon dragon = reader.readDragon();
        collection.add(dragon);
        System.out.println("Дракон добавлен");
    }
}
