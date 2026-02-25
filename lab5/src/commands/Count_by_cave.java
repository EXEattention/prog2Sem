package commands;

import java.util.List;

import collections.Dragon;

/**
 * Команда подсчёта драконов по значению поля cave
 * 
 * Выводит количество драконов, в которых количество сокровищ
 * в пещере равно заданному значению.
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Count_by_cave implements Command {
    private List<Dragon> collections;

    public Count_by_cave(List<Dragon> collections) {
        this.collections = collections;
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "count_by_cave";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Вывести количество элементов, значение поля cave которых равно заданному";
    }

    /**
     * Выполняет подсчёт драконов с заданным значением cave
     * 
     * @param args args[0] — значение numberOfTreasures для поиска
     */
    @Override
    public void execute(String... args) {
        if (args.length == 0) {
            System.out.println("Значение cave не может быть пустым");
            return;
        }
        Float deepCave;
        int count = 0;
        try {
            deepCave = Float.valueOf(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Должно быть число");
            return;
        }
        for (Dragon i : collections) {
            if (i.getCave().getNumberOfTreasures() == deepCave) {
                count++;
            }
        }
        System.out.println("Количество найденных элементов: " + count);
    }
}
