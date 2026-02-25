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
        CollectionManager manager = new CollectionManager();
        Stack<Dragon> collectionsArray = manager.getCollection();

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
        prime.register(new Execute_script(collectionsArray));
        prime.register(new Update(collectionsArray, scanner));
        prime.register(new Save("src/dragForSave.xml", collectionsArray));
        prime.register(new Exit(scanner));

        String[] tokens;

        // цикл обработки команд
        while (true) {
            tokens = scanner.nextLine().split(" ");
            if (tokens.length > 2) {
                String first = tokens[0];
                String all = "";
                for (int i = 1; i < tokens.length; i++) {
                    all += tokens[i];
                }
                tokens = new String[] { first, all };
            }
            switch (tokens[0]) {
                case "exit":
                    prime.execute("exit");
                    break;
                case "info":
                    prime.execute("info");
                    break;
                case "help":
                    prime.execute("help");
                    break;
                case "show":
                    prime.execute("show");
                    break;
                case "clear":
                    prime.execute("clear");
                    break;
                case "update":
                    prime.executeWithSecondParametr("update", tokens[1]);
                    break;
                case "print_unique_color":
                    prime.execute("print_unique_color");
                    break;
                case "add":
                    prime.execute("add");
                    break;
                case "save":
                    prime.execute("save");
                    break;
                case "remove_last":
                    prime.execute("remove_last");
                    break;
                case "remove_by_id":
                    prime.executeWithSecondParametr("remove_by_id", tokens[1]);
                    break;
                case "execute_script":
                    prime.executeWithSecondParametr("execute_script", tokens[1]);
                    break;
                case "filter_greater_than_cave":
                    prime.executeWithSecondParametr("filter_greater_than_cave", tokens[1]);
                    break;
                case "sort":
                    prime.execute("sort");
                    break;
                case "insert_at":
                    prime.executeWithSecondParametr("insert_at", tokens[1]);
                    break;
                case "count_by_cave":
                    prime.executeWithSecondParametr("count_by_cave", tokens[1]);
                    break;
                default:
                    System.out.println(
                            "Команда " + String.join(" ", tokens) + " не найдена. Воспользуйтесь справкой help");
            }
        }
    }
}