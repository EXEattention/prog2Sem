package commands;

import java.util.List;

import collections.Dragon;

/**
 * Команда поиска cave >= заданному значению
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Filter_greater_than_cave implements Command {
    private List<Dragon> collections;

    public Filter_greater_than_cave(List<Dragon> collections) {
        this.collections = collections;
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "filter_greater_than_cave";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Вывести элементы, значение поля cave которых больше заданного";
    }

    /**
     * Выполняет поиск и вывод значений cave >= заданному
     */
    @Override
    public void execute(String... args) {
        if (args.length == 0) {
            System.out.println("Значение cave не может быть пустым");
            return;
        }
        Float deepCave;
        try {
            deepCave = Float.valueOf(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Должно быть число");
            return;
        }
        for (Dragon i : collections) {
            if (i.getCave().getNumberOfTreasures() > deepCave) {
                System.out.println(i.toString());
            }
        }

    }
}
