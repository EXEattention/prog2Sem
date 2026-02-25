package commands;

import java.util.Collections;
import java.util.List;

import collections.Dragon;
import managers.CollectionManager;

/**
 * Команда сортировки
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Sort implements Command {
    private List<Dragon> collection;

    public Sort(CollectionManager manager) {
        collection = manager.getCollection();
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "sort";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Отсартировать элементы в коллекции по имени";
    }

    /**
     * Выполняет сортировку
     */
    @Override
    public void execute(String... args) {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пустая");
            return;
        }
        Collections.sort(collection);
        System.out.println("Коллекция отсортирована");
        return;
    }
}
