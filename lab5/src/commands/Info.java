package commands;

import managers.*;

/**
 * Команда Вывода информации о коллекции
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Info implements Command {
    private CollectionManager manager;

    public Info(CollectionManager manager) {
        this.manager = manager;
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "info";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Информация о коллекции";
    }

    /**
     * выполняет вывод информации
     */
    @Override
    public void execute(String... args) {
        System.out.println("Тип коллекции: " + manager.getType());
        System.out.println("Дата создания: " + manager.getDate());
        System.out.println("Количество элементов : " + manager.getSize());
    }
}