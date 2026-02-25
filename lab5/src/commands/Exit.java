package commands;

import java.util.Scanner;

/**
 * Команда завершения программы
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Exit implements Command {
    private Scanner scanner;

    public Exit(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Возвращает имя команды
     */
    @Override
    public String getName() {
        return "exit";
    }

    /**
     * Возвращает описание для help
     */
    @Override
    public String description() {
        return "Завершить программу";
    }

    /**
     * вЫполняет завершение программы
     * 
     * @param args (ничего не делает)
     */
    @Override
    public void execute(String... args) {
        System.out.println("Завершение программы");
        scanner.close();
        System.exit(0);
    }
}
