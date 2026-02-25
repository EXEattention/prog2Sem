package managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import collections.Color;
import collections.Coordinates;
import collections.Dragon;
import collections.DragonCave;
import collections.DragonCharacter;
import collections.DragonType;
import exceptions.*;

/**
 * Менеджер для работы с XML файлами.
 *
 * Обеспечивает загрузку и сохранение коллекции экземляров Dragon в XML
 * Использует BufferedReader и FileWriter
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class FileManager {
    /** файл для чтения/записи */
    private String fileName;

    /**
     * Создает менеджер файлов
     * 
     * @param fileName путь к XML файлу
     */
    public FileManager(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Загружает коллекцию экземпляров из XML файла
     * 
     * Если файл не найден или поврежден, то возврашается пустая коллекция
     * 
     * @return Stack экземляров уникальных по id
     */
    public Stack<Dragon> loadFromFile() {
        Stack<Dragon> dragons = new Stack<>();
        Set<Integer> zxc = new HashSet<>();
        Stack<Dragon> setDragons = new Stack<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            String xml = "";
            while ((line = br.readLine()) != null) {
                xml += line;
            }
            // System.out.println(xml);
            dragons = parseXML(xml);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден, загружена пустая коллекция");
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла, загружена пустая коллекция");
        }
        for (Dragon i : dragons) {
            try {
                checkDublicateID(i.getId(), zxc);
                zxc.add(i.getId());
                setDragons.push(i);
            } catch (DublicateIdException e) {
                System.out.println(e.getMessage());
            }
        }
        return setDragons;
    }

    /**
     * Проверяет уникальность id экземпляра
     * 
     * @param id  индентификатор (у каждого уникальный)
     * @param zxc множество встреченных id
     * @throws DublicateIdException в случае существования id в множестве
     */
    private void checkDublicateID(int id, Set<Integer> zxc) throws DublicateIdException {
        if (zxc.contains(id)) {
            throw new DublicateIdException(id);
        }
    }

    /**
     * Парсит XML строку и возвращает коллекцию экземпляров
     * 
     * Разбивает XML по тегу dragon и парсит каждый блок отдельно
     * Безуспешно запарщенные экземпляры пропускаются с ошибкой
     * 
     * @param xml строка со всем XML
     * @return Stack с распарсенными экземплярами
     */
    private Stack<Dragon> parseXML(String xml) {
        Stack<Dragon> dragons = new Stack<>();
        String[] blocks = xml.split("<dragon>");
        for (int i = 1; i < blocks.length; i++) {
            try {
                Dragon dragon = parseDragon(blocks[i]);
                validateDragon(dragon);
                dragons.push(dragon);

            } catch (InvalidDragonException e) {
                System.out.println(e.getMessage());
            } catch (InvalidXMLException e) {
                System.out.println(e.getMessage());
            }
        }
        return dragons;
    }

    /**
     * Проверяет валидность экземпляров
     * 
     * @param dragon для проверки
     * @throws InvalidDragonException если null или не прошел валидацию
     */
    private void validateDragon(Dragon dragon) throws InvalidDragonException {
        if (dragon == null) {
            throw new InvalidDragonException("Экземпляр не созадн");
        }
        if (!dragon.validate()) {
            throw new InvalidDragonException("Экземпляр не прошел валидацию");
        }
    }

    /**
     * Парсит блок XML
     * Извлекает все поля дракона из XML блока
     * 
     * @param block строка с XML блоком, где содержтся 1 экземпляр
     * @return созданный объект Dragon
     * @throws InvalidXMLException если отсутсвует name
     */
    private Dragon parseDragon(String block) throws InvalidXMLException {
        String name = extractValue(block, "name");
        if (name == null || name.isEmpty()) {
            throw new InvalidXMLException("Отсутсвует name");
        }
        Dragon dragon = new Dragon(name);

        String id = extractValue(block, "id");
        if (id != null && !id.isEmpty()) {
            try {
                dragon.setId(Integer.valueOf(id));
            } catch (NumberFormatException e) {
            }

        }

        String coordinateBlock = extractValue(block, "coordinates");
        if (coordinateBlock != null) {
            Coordinates coordinates = parseCoordinates(coordinateBlock);
            dragon.setCoordinates(coordinates);
        }

        String age = extractValue(block, "age");
        if (age != null && !age.isEmpty()) {
            try {
                dragon.setAge(Long.valueOf(age));
            } catch (NumberFormatException e) {
            }
        }

        String color = extractValue(block, "color");
        if (color != null && !color.isEmpty()) {
            try {
                dragon.setColor(Color.valueOf(color.toUpperCase()));
            } catch (IllegalArgumentException e) {
            }
        }
        String creationDate = extractValue(block, "creationDate");
        if (creationDate != null && !creationDate.isEmpty()) {
            try {
                dragon.setCreationDate(LocalDate.parse(creationDate));
            } catch (DateTimeParseException e) {
            }
        }

        String type = extractValue(block, "type");
        if (type != null && !type.isEmpty()) {
            try {
                dragon.setType(DragonType.valueOf(type.toUpperCase()));
            } catch (IllegalArgumentException e) {
            }
        }

        String character = extractValue(block, "character");
        if (character != null && !character.isEmpty()) {
            try {
                dragon.setCharacter(DragonCharacter.valueOf(character.toUpperCase()));
            } catch (IllegalArgumentException e) {
            }
        }

        String cave = extractValue(block, "cave");
        if (cave != null) {
            DragonCave caveDeep = parseCave(cave);
            dragon.setCave(caveDeep);
        }
        return dragon;

    }

    /**
     * Парсит блок cave из XML
     * 
     * @param block строка с XML блоком cave
     * @return объект DragonCave с заполненными полями
     */
    private DragonCave parseCave(String block) {
        DragonCave cave = new DragonCave();

        String caveParse = extractValue(block, "numberOfTreasures");
        if (caveParse != null && !caveParse.isEmpty()) {
            try {
                cave.setNumberOfTreasures(Float.valueOf(caveParse));
            } catch (NumberFormatException e) {
            }
        }
        return cave;
    }

    /**
     * Парсит блок coordinates из XML
     * 
     * @param block строка с XML блоком координат
     * @return объект Coordinates с заполненными полями
     */
    private Coordinates parseCoordinates(String block) {
        Coordinates coordinates = new Coordinates();

        String x = extractValue(block, "x");
        if (x != null && !x.isEmpty()) {
            try {
                coordinates.setX(Double.valueOf(x));
            } catch (NumberFormatException e) {
            }
        }

        String y = extractValue(block, "y");
        if (y != null && !y.isEmpty()) {
            try {
                coordinates.setY(Integer.valueOf(y));
            } catch (NumberFormatException e) {
            }
        }
        return coordinates;
    }

    /**
     * Извлекает атрибутные значения между тегами
     * 
     * @param xml строка с XML
     * @param tag имя тега
     * @return значение между тегами(или null если тега нет)
     */
    private String extractValue(String xml, String tag) {
        String openTag = "<" + tag + ">";
        String closeTag = "</" + tag + ">";

        int start = xml.indexOf(openTag);
        if (start == -1) {
            return null;
        }

        start += openTag.length();
        int end = xml.indexOf(closeTag, start);

        if (end == -1) {
            return null;
        }
        String res = xml.substring(start, end);
        return res.trim();
    }

    /**
     * Сохраняет коллекцию в XML файл
     * Переписывает файл полностью
     * 
     * @param dragons коллекция экземпляров для сохранения
     */
    public void saveToFile(Stack<Dragon> dragons) {
        try (FileWriter write = new FileWriter(fileName)) {
            write.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            write.write("<dragons>\n");

            for (Dragon dragon : dragons) {
                writeDragon(write, dragon);
            }
            write.write("</dragons>\n");
            System.out.println("Коллекция сохранена в " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
        }
    }

    /**
     * Записывает одного дракона в XML формате
     * 
     * @param writer FileWriter для записи
     * @param dragon экземпляр для записи
     * @throws IOException в случае ошибки записи
     */
    private void writeDragon(FileWriter writer, Dragon dragon) throws IOException {
        writer.write("    <dragon>\n");
        writer.write("        <id>" + dragon.getId() + "</id>\n");
        writer.write("        <name>" + dragon.getName() + "</name>\n");

        writer.write("        <coordinates>\n");
        writer.write("            <x>" + dragon.getCoordinates().getX() + "</x>\n");
        writer.write("            <y>" + dragon.getCoordinates().getY() + "</y>\n");
        writer.write("        </coordinates>\n");

        writer.write("        <creationDate>" + dragon.getCreationDate() + "</creationDate>\n");
        if (dragon.getAge() != null) {
            writer.write("        <age>" + dragon.getAge() + "</age>\n");
        }

        if (dragon.getColor() != null) {
            writer.write("        <color>" + dragon.getColor() + "</color>\n");
        }

        if (dragon.getType() != null) {
            writer.write("        <type>" + dragon.getType() + "</type>\n");
        }

        if (dragon.getCharacter() != null) {
            writer.write("        <character>" + dragon.getCharacter() + "</character>\n");
        }

        writer.write("        <cave>\n");
        writer.write(
                "            <numberOfTreasures>" + dragon.getCave().getNumberOfTreasures() + "</numberOfTreasures>\n");
        writer.write("        </cave>\n");

        writer.write("    </dragon>\n");
    }

    /**
     * возвращает имя файла
     * 
     * @return путь к XML файлу
     */
    public String getFilename() {
        return fileName;
    }
}
