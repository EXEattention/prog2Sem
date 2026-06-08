import commands.*;
import managers.*;
import collections.Dragon;

import java.util.Scanner;
import java.util.Stack;

/**
 * Реализует консольное приложение в интерактивном режиме
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Main {
    /**
     * Инициализирует коллекцию, регистрирует команды и запускает
     * 
     * @param args (не используется)
     * @throws Exception при критических ошибках выполнения
     */
    public static void main(String[] args) throws Exception {
        // инициализация менеджера коллекции
        System.out.println(new java.io.File(".").getAbsolutePath());
        CollectionManager manager = new CollectionManager();
        Stack<Dragon> collectionsArray = manager.getCollection();
        LoaderManager startLoad = new LoaderManager(collectionsArray);
        startLoad.load("src/cfg.txt");

        // инициализация обработчика команд
        CommandInvoker prime = new CommandInvoker();
        Scanner scanner = new Scanner(System.in);

        // регистрация команд
        prime.register(new Info(manager));
        prime.register(new Help(prime));
        prime.register(new Show(collectionsArray));
        prime.register(new Add(collectionsArray, scanner));
        prime.register(new Remove_last(manager));
        prime.register(new Remove_by_id(manager));
        prime.register(new Count_by_cave(collectionsArray));
        prime.register(new Print_unique_color(collectionsArray));
        prime.register(new Filter_greater_than_cave(collectionsArray));
        prime.register(new Sort(manager));
        prime.register(new Insert_at(collectionsArray, scanner));
        prime.register(new Clear(collectionsArray));
        prime.register(new Execute_script(collectionsArray, prime, scanner));
        prime.register(new Update(collectionsArray, scanner));
        prime.register(new Save("src/dragForSave.xml", collectionsArray, scanner));
        prime.register(new Exit(scanner));

        // цикл обработки команд
        while (true) {
            if (!scanner.hasNextLine()) {
                System.out.println("Ввод прерван");
                break;
            }

            String input = scanner.nextLine();
            if (input.isEmpty()) {
                continue;
            }
            String[] tokens = input.trim().split(" ");
            try {
                if (tokens.length > 1) {
                    prime.executeWithSecondParametr(tokens[0], tokens[1]);
                } else {
                    prime.execute(tokens[0]);
                }
            } catch (Exception e) {
                System.out.println("Такой команды нет");
            }
        }
    }
}