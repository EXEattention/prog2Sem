package client;

import net.protocol.CommandRequest;
import net.protocol.CommandResponse;
import collections.Dragon;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import collections.Color;
import collections.DragonCharacter;
import collections.DragonType;

/**
 * Клиентский модуль использующий неблокирующий DatagramChannel
 */
public class ClientMain {
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 8888;
    private static final int BUFFER_SIZE = 65507;

    public static void main(String[] args) throws Exception {
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.configureBlocking(false);
            channel.bind(null); // ephemeral local port

            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            Scanner scanner = new Scanner(System.in);
            System.out.println(
                    "Клиент запущен. Введите команды (exit — выход). Сервер: " + SERVER_HOST + ":" + SERVER_PORT);

            while (true) {
                System.out.print("-> ");
                if (!scanner.hasNextLine())
                    break;
                String line = scanner.nextLine().trim();
                if (line.isEmpty())
                    continue;

                // simple parsing: command and optional arg
                String[] parts = line.split(" ", 2);
                String cmd = parts[0];

                if (cmd.equalsIgnoreCase("save")) {
                    System.out.println("Команда 'save' недоступна на клиенте");
                    continue;
                }

                Object argument = null;
                if (parts.length > 1) {
                    if (cmd.equalsIgnoreCase("remove_by_id")) {
                        try {
                            argument = Integer.valueOf(parts[1].trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Неверный id");
                            continue;
                        }
                    } else if (cmd.equalsIgnoreCase("count_by_cave")) {
                        try {
                            argument = Float.valueOf(parts[1].trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Неверное значение");
                            continue;
                        }
                    } else {
                        // keep raw argument for other commands
                        argument = parts[1];
                    }
                } else {
                    // interactive add: ask parameters when user typed just 'add'
                    if (cmd.equalsIgnoreCase("add")) {
                        argument = readDragonInteractive(scanner);
                        if (argument == null)
                            continue; // user aborted or invalid
                    }
                }

                CommandRequest request = new CommandRequest(cmd.toLowerCase(), argument);

                // serialize request
                byte[] bytes;
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(request);
                    oos.flush();
                    bytes = baos.toByteArray();
                }

                SocketAddress serverAddr = new InetSocketAddress(SERVER_HOST, SERVER_PORT);
                ByteBuffer buf = ByteBuffer.wrap(bytes);
                channel.send(buf, serverAddr);

                // wait for response with timeout, handle server unavailable
                CommandResponse response = null;
                int attempts = 0;
                while (attempts < 3) {
                    attempts++;
                    selector.select(2000); // 2s
                    Set<SelectionKey> keys = selector.selectedKeys();
                    if (keys.isEmpty()) {
                        System.out.println("No response from server (attempt " + attempts + ")");
                        continue;
                    }
                    Iterator<SelectionKey> it = keys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        if (key.isReadable()) {
                            ByteBuffer rbuf = ByteBuffer.allocate(BUFFER_SIZE);
                            SocketAddress sa = channel.receive(rbuf);
                            if (sa == null)
                                continue;
                            rbuf.flip();
                            byte[] rb = new byte[rbuf.remaining()];
                            rbuf.get(rb);
                            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(rb))) {
                                Object o = ois.readObject();
                                if (o instanceof CommandResponse) {
                                    response = (CommandResponse) o;
                                }
                            }
                        }
                    }
                    if (response != null)
                        break;
                }

                if (response == null) {
                    System.out.println("Server is unavailable. Try later.");
                } else {
                    System.out.println(response.getMessage());
                    List<Dragon> snapshot = response.getCollectionSnapshot();
                    if (snapshot != null) {
                        System.out.println("Снимок коллекции (отсортирован по числу сокровищ в пещере):");
                        snapshot.forEach(d -> System.out.println(d.toString()));
                    }
                }

                if (cmd.equalsIgnoreCase("exit")) {
                    System.out.println("Client exiting");
                    break;
                }
            }
        }
    }

    private static Dragon readDragonInteractive(Scanner scanner) {
        System.out.println("Создание нового дракона. Введите поля или пустую строку для отмены.");
        System.out.print("Имя: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Отмена создания");
            return null;
        }
        Dragon d = new Dragon(name);

        try {
            System.out.print("Координата X (число > -257): ");
            String sx = scanner.nextLine().trim();
            double x = Double.parseDouble(sx);
            d.getCoordinates().setX(x);

            System.out.print("Координата Y (целое > -324): ");
            String sy = scanner.nextLine().trim();
            int y = Integer.parseInt(sy);
            d.getCoordinates().setY(y);

            System.out.print("Возраст (целое >0 или пусто): ");
            String sage = scanner.nextLine().trim();
            if (!sage.isEmpty())
                d.setAge(Long.valueOf(Long.parseLong(sage)));

            // выбор цвета — по имени или по номеру
            Color chosenColor = chooseEnum(scanner, Color.class, "Цвет", true);
            if (chosenColor != null)
                d.setColor(chosenColor);

            // выбор типа
            DragonType chosenType = chooseEnum(scanner, DragonType.class, "Тип", true);
            if (chosenType != null)
                d.setType(chosenType);

            // выбор характера
            DragonCharacter chosenChar = chooseEnum(scanner, DragonCharacter.class, "Характер", true);
            if (chosenChar != null)
                d.setCharacter(chosenChar);

            System.out.print("Число сокровищ в пещере (положительное число): ");
            String c = scanner.nextLine().trim();
            float treasures = Float.parseFloat(c);
            d.getCave().setNumberOfTreasures(treasures);

        } catch (Exception e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
            return null;
        }

        if (!d.validate()) {
            System.out.println("Введённые данные не прошли валидацию");
            return null;
        }
        d.setId(0);
        return d;
    }

    private static <E extends Enum<E>> E chooseEnum(Scanner scanner, Class<E> enumClass, String title,
            boolean allowEmpty) {
        E[] constants = enumClass.getEnumConstants();
        System.out.println(title + ":");
        for (int i = 0; i < constants.length; i++) {
            System.out.println("  " + (i + 1) + ") " + constants[i].name());
        }
        System.out.print("Выберите по номеру или введите имя" + (allowEmpty ? " (пусто — пропустить)" : "") + ": ");
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            if (allowEmpty)
                return null;
            System.out.println("Обязательное поле, попробуйте снова");
            return chooseEnum(scanner, enumClass, title, allowEmpty);
        }

        try {
            int idx = Integer.parseInt(line);
            if (idx >= 1 && idx <= constants.length) {
                return constants[idx - 1];
            } else {
                System.out.println("Неверный номер");
                return chooseEnum(scanner, enumClass, title, allowEmpty);
            }
        } catch (NumberFormatException ex) {
            try {
                return Enum.valueOf(enumClass, line.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Неверное имя. Попробуйте снова.");
                return chooseEnum(scanner, enumClass, title, allowEmpty);
            }
        }
    }
}
