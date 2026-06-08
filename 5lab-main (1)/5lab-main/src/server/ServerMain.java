package server;

import managers.CollectionManager;
import managers.LoaderManager;
import managers.FileManager;
import collections.Dragon;
import net.protocol.CommandRequest;
import net.protocol.CommandResponse;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Простая одно-поточная серверная реализация, принимающая датаграммы
 */
public class ServerMain {
    private static final int DEFAULT_PORT = 8888;
    private static final int BUFFER_SIZE = 65507;

    public static void main(String[] args) throws Exception {
        int port = DEFAULT_PORT;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }
        System.out.println("Сервер UDP будет запущен на порту " + port);
        CollectionManager manager = new CollectionManager();
        Stack<Dragon> collection = manager.getCollection();
        LoaderManager loader = new LoaderManager(collection);
        try {
            loader.load("cfg.txt");
        } catch (IOException e) {
            System.out.println("Не удалось загрузить коллекцию: " + e.getMessage());
        }

        // регистрация команд на сервере (чтобы help и другие команды работали как в исходной версии)
        managers.CommandInvoker invoker = new managers.CommandInvoker();
        java.util.Scanner srvScanner = new java.util.Scanner(System.in);
        invoker.register(new commands.Info(manager));
        invoker.register(new commands.Help(invoker));
        invoker.register(new commands.Show(collection));
        invoker.register(new commands.Add(collection, srvScanner));
        invoker.register(new commands.Remove_last(manager));
        invoker.register(new commands.Remove_by_id(manager));
        invoker.register(new commands.Count_by_cave(collection));
        invoker.register(new commands.Print_unique_color(collection));
        invoker.register(new commands.Filter_greater_than_cave(collection));
        invoker.register(new commands.Sort(manager));
        invoker.register(new commands.Insert_at(collection, srvScanner));
        invoker.register(new commands.Clear(collection));
        invoker.register(new commands.Execute_script(collection, invoker, srvScanner));
        invoker.register(new commands.Update(collection, srvScanner));
        invoker.register(new commands.Save("dragForSave.xml", collection, srvScanner));
        invoker.register(new commands.Exit(srvScanner));

        // Попытка привязать датаграммный сокет к порту; если порт занят — информативно завершаем работу
        try (DatagramSocket socket = new DatagramSocket(port)) {
            socket.setSoTimeout(2000);
            byte[] buffer = new byte[BUFFER_SIZE];

            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // неблокирующая проверка консольных команд сервера (одно-поточно)
                if (console.ready()) {
                    String line = console.readLine();
                    if (line != null) {
                        if (line.trim().equalsIgnoreCase("save_server")) {
                            FileManager fm = new FileManager("dragons.xml");
                            fm.saveToFile(collection);
                        } else if (line.trim().equalsIgnoreCase("exit")) {
                            // сохранить и выйти
                            FileManager fm = new FileManager("dragons.xml");
                            fm.saveToFile(collection);
                            System.out.println("Сервер завершает работу");
                            break;
                        }
                    }
                }

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                } catch (java.net.SocketTimeoutException ex) {
                    // таймаут — вернуться в цикл и проверить консоль
                    continue;
                }

                InetAddress clientAddr = packet.getAddress();
                int clientPort = packet.getPort();

                // десериализовать запрос
                CommandRequest request = null;
                try (ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                        ObjectInputStream ois = new ObjectInputStream(bais)) {
                    Object o = ois.readObject();
                    if (o instanceof CommandRequest) {
                        request = (CommandRequest) o;
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка десериализации запроса: " + e.getMessage());
                }

                CommandResponse response;
                if (request == null) {
                    response = new CommandResponse(false, "Неверный запрос");
                } else {
                    response = processRequest(request, collection, manager, invoker);
                }

                // сериализовать ответ
                byte[] respBytes;
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(response);
                    oos.flush();
                    respBytes = baos.toByteArray();
                }

                DatagramPacket respPacket = new DatagramPacket(respBytes, respBytes.length, clientAddr, clientPort);
                socket.send(respPacket);
            }
        } catch (java.net.BindException be) {
            System.out.println("Не удалось привязать порт " + port + ": порт уже используется.");
            System.out.println("Убедитесь, что запущен только один экземпляр сервера, или укажите другой порт в аргументах.");
            return;
        }
    }

    private static CommandResponse processRequest(CommandRequest req, Stack<Dragon> collection,
            CollectionManager manager, managers.CommandInvoker invoker) {
        String cmd = req.getCommandName();
        // capture stdout from command execution
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        try {
            System.setOut(ps);

            // prepare argument for invoker: convert special objects to string JSON when needed
            Object arg = req.getArgument();
            if (arg == null) {
                invoker.execute(cmd);
            } else {
                String argStr;
                if (arg instanceof String) {
                    argStr = (String) arg;
                } else if (arg instanceof Dragon) {
                    argStr = dragonToJson((Dragon) arg);
                } else {
                    argStr = arg.toString();
                }
                invoker.executeWithSecondParametr(cmd, argStr);
            }

            ps.flush();
            String output = baos.toString();

            // if show — also attach collection snapshot sorted by cave treasures
            if ("show".equals(cmd)) {
                List<Dragon> sorted = collection.stream()
                        .sorted(Comparator.comparing(d -> d.getCave().getNumberOfTreasures()))
                        .collect(Collectors.toList());
                return new CommandResponse(true, output.isEmpty() ? "Ok" : output, sorted);
            }

            return new CommandResponse(true, output.isEmpty() ? "Ok" : output);

        } catch (Exception e) {
            return new CommandResponse(false, "Ошибка обработки: " + e.getMessage());
        } finally {
            System.setOut(oldOut);
            try {
                baos.close();
            } catch (IOException ignored) {
            }
        }
    }

    private static String dragonToJson(Dragon d) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"name\":\"").append(escape(d.getName())).append('\"');
        if (d.getAge() != null) sb.append(",\"age\":").append(d.getAge());
        if (d.getColor() != null) sb.append(",\"color\":\"").append(d.getColor().toString()).append('\"');
        if (d.getType() != null) sb.append(",\"type\":\"").append(d.getType().toString()).append('\"');
        if (d.getCharacter() != null) sb.append(",\"character\":\"").append(d.getCharacter().toString()).append('\"');
        if (d.getCoordinates() != null) {
            sb.append(",\"coordinates\":{");
            sb.append("\"x\":").append(d.getCoordinates().getX()).append(',');
            sb.append("\"y\":").append(d.getCoordinates().getY());
            sb.append('}');
        }
        if (d.getCave() != null) {
            sb.append(",\"cave\":{\"numberOfTreasures\":").append(d.getCave().getNumberOfTreasures()).append('}');
        }
        sb.append('}');
        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
