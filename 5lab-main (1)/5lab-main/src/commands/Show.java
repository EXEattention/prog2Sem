package commands;

import java.util.List;

import collections.Dragon;

/**
 * Команда для отображения экземпляров коллекции
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Show implements Command {
    private List<Dragon> collections;

    public Show(List<Dragon> collections) {
        this.collections = collections;
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "show";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Вывести элементы коллекции";
    }

    /**
     * Выполняет проход по всем экземплярам и выводит их
     */
    @Override
    public void execute(String... args) {
        for (Dragon i : collections) {
            System.out.println(i.toString());
        }
    }
}
