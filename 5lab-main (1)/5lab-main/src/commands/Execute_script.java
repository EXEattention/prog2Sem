package commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import collections.Dragon;
import managers.*;

public class Execute_script implements Command {
    private List<Dragon> collections;
    CommandInvoker prime;
    Scanner scanner;
    private static Set<String> executingScripts = new HashSet<>();

    public Execute_script(List<Dragon> collections, CommandInvoker prime, Scanner scanner) {
        this.collections = collections;
        this.prime = prime;
        this.scanner = scanner;
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String description() {
        return "Считать и исполнить скрипт из указанного файла";
    }

    @Override
    public void execute(String... args) {
        if (args.length == 0) {
            System.out.println("Укажите имя файла");
            return;
        }

        String fileName = args[0];

        if (executingScripts.contains(fileName)) {
            System.out.println("рекурсия елки палки");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            executingScripts.add(fileName);
            String line;
            int lineNum = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;

                int firstSpace = line.indexOf(' ');
                String commandName;
                String argument = null;
                
                if (firstSpace == -1) {
                    commandName = line;
                } else {
                    commandName = line.substring(0, firstSpace);
                    argument = line.substring(firstSpace + 1); 
                }
                
                try {
                    if (argument != null && !argument.isEmpty()) {
                        prime.executeWithSecondParametr(commandName, argument);
                    } else {
                        prime.execute(commandName);
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка в строке " + lineNum + ": " + line);
                }
            }

            executingScripts.remove(fileName);
            System.out.println("Скрипт выполнен: " + fileName);

        } catch (IOException e) {
            System.out.println("Файл не найден: " + fileName);
        }
    }
}