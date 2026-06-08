package commands;

import collections.Color;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import collections.Dragon;

/**
 * Команда вывода уникальных цветов
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Print_unique_color implements Command {
    private List<Dragon> collections;

    public Print_unique_color(List<Dragon> collections) {
        this.collections = collections;
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "print_unique_color";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Вывести уникальные значения поля color всех элементов в коллекции";
    }

    /**
     * Выполняет перебор экземпляров и добавляет их в Set
     * после перебора выводит Set
     * 
     * @paranm args - (ниче не делает)
     */
    @Override
    public void execute(String... args) {
        Set<Color> arr = new HashSet<>();
        if (collections.isEmpty()) {
            System.out.println("Коллекция пустая");
            return;
        }
        for (Dragon i : collections) {
            arr.add(i.getColor());
        }
        System.out.println(arr);
    }
}
