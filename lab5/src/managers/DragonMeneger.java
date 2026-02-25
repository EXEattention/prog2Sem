package managers;

import java.util.Arrays;
import java.util.Scanner;
import collections.*;

/**
 * Менеджер для чтенния данных экземпляра из консоли
 * Обеспечивает ввод всех полей с валидацией
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class DragonMeneger {
    private Scanner scanner;

    /**
     * Создает менджер для чтения данных экземпляра
     * 
     * @param scanner для чтения из консоли
     */
    public DragonMeneger(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Читает все данные экземпляра из консоли
     * 
     * Последовательно запрашивает все поля
     * Каждое поле валидируется
     * 
     * @return полностью заполненный экземпляр Dragon
     */
    public Dragon readDragon() {
        String name = readName();
        Dragon dragon = new Dragon(name);

        dragon.setCoordinates(readCoordinates());
        dragon.setAge(readAge());
        dragon.setColor(readColor());
        dragon.setType(readType());
        dragon.setCharacter(readCharacter());
        dragon.setCave(readCave());

        return dragon;
    }

    /**
     * Читает имя экземпляра
     * Имя не может быть пустым
     * 
     * @return имя экземпляра
     */
    private String readName() {
        while (true) {
            System.out.print("Введите имя: ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                return name;
            }
            System.out.println("Имя не может быть null");
        }
    }

    /**
     * Читает координаты дракона
     * 
     * x должен быть больше -257, Y должэен быть больше -324
     * При некорректном ввооде запрашивает повторно
     * 
     * @return обхект coordinates с заполненными полями
     */
    private Coordinates readCoordinates() {
        Coordinates coordinates = new Coordinates();

        while (true) {
            System.out.println("Введите X: ");
            try {
                double x = Double.valueOf(scanner.nextLine());
                if (x > -257.0) {
                    coordinates.setX(x);
                    break;
                }
                System.out.println("Необходимо ввести >-257");
            } catch (NumberFormatException e) {
                System.out.println("Необходимо ввести число");
            }
        }

        while (true) {
            System.out.println("Введите Y: ");
            try {
                Integer y = Integer.valueOf(scanner.nextLine());
                if (y > -257.0) {
                    coordinates.setY(y);
                    break;
                }
                System.out.println("Необходимо ввести >-324");
            } catch (NumberFormatException e) {
                System.out.println("Необходимо ввести число");
            }
        }
        return coordinates;
    }

    /**
     * Читает возраст дракона
     * 
     * Возраст должен быть больше 0. Пустой ввод это null
     * 
     * @return возраст дракона или null при пустом вводе
     */
    private Long readAge() {
        Long age;
        while (true) {
            System.out.println("Введите возраст: ");
            String s = scanner.nextLine();
            if (s.isEmpty()) {
                return null;
            }
            try {
                age = Long.valueOf(s);
                if (age > 0) {
                    return age;
                }
                System.out.println("Необходимо ввести >0");
            } catch (NumberFormatException e) {
                System.out.println("Необходимо ввести число");
            }

        }
    }

    /**
     * Читает цвет экземпляра
     * 
     * @return значение enum Color
     */
    private Color readColor() {
        while (true) {
            System.out.println(Arrays.toString(Color.values()));
            System.out.println("Введите цвет: ");
            try {
                String s = scanner.nextLine().toUpperCase();
                return Color.valueOf(s);
            } catch (IllegalArgumentException e) {
                System.out.println("Такого цвета нет");
            }
        }
    }

    /**
     * Читает тип экземпляра
     * 
     * @return значение enum DragonType
     */
    private DragonType readType() {

        while (true) {
            System.out.println(Arrays.toString(DragonType.values()));
            System.out.println("Введите тип: ");
            try {
                String s = scanner.nextLine().toUpperCase();
                return DragonType.valueOf(s);
            } catch (IllegalArgumentException e) {
                System.out.println("Такого типа нет");
            }
        }
    }

    /**
     * Читает характер экземпляра
     * 
     * @return значение enum DragonCharacter
     */
    private DragonCharacter readCharacter() {
        while (true) {
            System.out.println(Arrays.toString(DragonCharacter.values()));
            System.out.println("Введите характер: ");
            try {
                String s = scanner.nextLine().toUpperCase();
                return DragonCharacter.valueOf(s);
            } catch (IllegalArgumentException e) {
                System.out.println("Такого характера нет");
            }
        }
    }

    /**
     * Читает данные об пещере дракона
     * 
     * @return объект DragonCave с заполненным полем numberOfTreasures
     */
    private DragonCave readCave() {
        DragonCave cave = new DragonCave();
        while (true) {
            System.out.println("Введите кол-во сокровищ в пещере: ");
            try {
                Float deepCave = Float.valueOf(scanner.nextLine());
                if (deepCave > 0) {
                    cave.setNumberOfTreasures(deepCave);
                    return cave;
                }
                System.out.println("Необходимо ввести >0");
            } catch (NumberFormatException e) {
                System.out.println("Необходимо ввести числот");
            }
        }
    }
}
