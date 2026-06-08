package commands;

import java.util.List;

import collections.Dragon;

/**
 * Команда для очистки коллекции
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Clear implements Command {
    private List<Dragon> collection;

    public Clear(List<Dragon> collection) {
        this.collection = collection;
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "clear";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Очистить коллеккцию";
    }

    /**
     * Удаление экземпляров в коллекции
     * 
     * @param (не используется)
     */
    @Override
    public void execute(String... args) {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пустая");
            return;
        }
        int size = collection.size();
        while (!collection.isEmpty()) {
            collection.remove(collection.size() - 1);
        }
        System.out.println("Удалено элементов: " + size);
    }
}
