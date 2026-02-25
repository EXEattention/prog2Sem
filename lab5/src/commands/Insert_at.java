package commands;

import java.util.List;
import java.util.Scanner;

import collections.Dragon;
import managers.DragonMeneger;

/**
 * Команда вставки экземпляра в заданный index
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Insert_at implements Command {
    private List<Dragon> collections;
    private DragonMeneger reader;

    public Insert_at(List<Dragon> collections, Scanner scanner) {
        this.collections = collections;
        reader = new DragonMeneger(scanner);
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "insert_at";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Добавить новый элемент в заданную позицию";
    }

    /**
     * Выполняет запрос на поля у пользователя и заменяет новосозданный экземпляр
     * на тот который был под индексом
     * 
     * @param args[0] - index
     */
    @Override
    public void execute(String... args) {
        int index;
        try {
            index = Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Необходимо ввести число");
            return;
        }
        if (index < 0 || index > collections.size()) {
            System.out.println("Индекс должен быть от 0 до " + collections.size());
            return;
        }
        Dragon dragon = reader.readDragon();
        collections.add(index, dragon);
        System.out.println("Успешно добавлено на index: " + index);
    }
}
