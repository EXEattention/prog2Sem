package commands;

import java.util.List;
import java.util.Scanner;

import collections.Dragon;
import managers.DragonMeneger;

/**
 * Команда обновления полей экземпляра в коллекции
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Update implements Command {
    private List<Dragon> collections;
    private DragonMeneger reader;

    public Update(List<Dragon> collections, Scanner scanner) {
        this.collections = collections;
        reader = new DragonMeneger(scanner);
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "update";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Обновить значение элемента коллекции, id которого равен заданному";
    }

    /**
     * Выполняет поиск экзмепляра по id
     * запрашивает ввод полей через DragonManager
     * заменяет новый экземпляр на стрый
     * 
     * @param args[0] - id
     */
    @Override
    public void execute(String... args) {
        int id;
        try {
            id = Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Необходимо ввести число");
            return;
        }
        int cnt = 0;
        for (Dragon i : collections) {
            if (i.getId() == id) {
                Dragon dragon = reader.readDragon();
                dragon.setId(id);
                collections.set(cnt, dragon);
                System.out.println("Успех элмент по id " + id + " заменен");
                return;
            }
            cnt++;
        }
        System.out.println("не нашлось id " + id);
    }
}
