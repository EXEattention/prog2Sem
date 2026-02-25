package commands;

import java.util.List;
import managers.CollectionManager;
import collections.Dragon;

/**
 * Команда для удаления последнего экземпляра в коллекции
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Remove_last implements Command {
    private List<Dragon> collection;

    public Remove_last(CollectionManager manager) {
        collection = manager.getCollection();
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "remove_last";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Удалить последний элемент коллекции";
    }

    /**
     * Выполняет удаление последнего экземпляра
     */
    @Override
    public void execute(String... args) {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пустая");
            return;
        }
        collection.remove(collection.size() - 1);
        System.out.println("Последнний элемент удален");
        return;
    }
}
