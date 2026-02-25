package commands;

import managers.CollectionManager;

import java.util.List;
import collections.Dragon;

/**
 * Команда для удаления экземпляра по id
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Remove_by_id implements Command {
    private List<Dragon> collection;

    public Remove_by_id(CollectionManager manager) {
        collection = manager.getCollection();
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "remove_by_id";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Удалить элемент из коллекции по его id";
    }

    /**
     * Выполняет поиск по id среди экземпляров
     * если находит то удаляет элемент
     * 
     * @param args[0] - id экземпляра
     */
    @Override
    public void execute(String... args) {
        Dragon deleteIdDragon = null;
        Integer id;

        if (args.length != 1) {
            System.out.println("Укажите единственный id в качестве параметра");
            return;
        }

        try {
            id = Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Укажите число в качестве id");
            return;
        }

        for (Dragon i : collection) {
            if (i.getId() == id) {
                deleteIdDragon = i;
                break;
            }
        }
        if (deleteIdDragon != null) {
            collection.remove(deleteIdDragon);
            System.out.println("Удален элемент с id " + id);
        } else {
            System.out.println("элемент с id " + id + " не найден");
        }
    }
}
