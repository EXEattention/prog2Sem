package collections;

import java.time.LocalDate;

/**
 * Реаализация экземпляра коллекции
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class Dragon implements Valid, Comparable<Dragon> {
    private static int idCounter = 1;
    private Integer id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого
                        // поля должно быть уникальным, Значение этого поля должно генерироваться
                        // автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private java.time.LocalDate creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
                                              // автоматически
    private Long age; // Значение поля должно быть больше 0, Поле может быть null
    private Color color; // Поле не может быть null
    private DragonType type; // Поле не может быть null
    private DragonCharacter character; // Поле может быть null
    private DragonCave cave; // Поле не может быть null

    public Dragon(String name) {
        creationDate = LocalDate.now();
        this.name = name;
        id = idCounter++;
        this.coordinates = new Coordinates();
        this.cave = new DragonCave();
    }

    // гэттеры

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Long getAge() {
        return age;
    }

    public Color getColor() {
        return color;
    }

    public DragonType getType() {
        return type;
    }

    public DragonCharacter getCharacter() {
        return character;
    }

    public DragonCave getCave() {
        return cave;
    }

    // сеттеры
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public void setCreationDate(java.time.LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setType(DragonType type) {
        this.type = type;
    }

    public void setCharacter(DragonCharacter character) {
        this.character = character;
    }

    public void setCave(DragonCave cave) {
        this.cave = cave;
    }

    @Override
    public int compareTo(Dragon ret) {
        return this.name.compareTo(ret.name);
    }

    /**
     * фунция для проверки валидности экземпляра
     */
    @Override
    public boolean validate() {
        if (name.length() == 0 || name == null) {
            return false;
        }
        if (coordinates == null) {
            return false;
        }
        if (age != null && age <= 0) {
            return false;
        }
        if (color == null) {
            return false;
        }
        if (type == null) {
            return false;
        }
        if (cave == null) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Dragon{" +
                "id=" + String.valueOf(id) + " name=\'" + name + "\', " +
                "coordinates=\'x=" + coordinates.getX() + " y=" + coordinates.getY() +
                "\', creationDate='" + creationDate + "\', " + "age='" + age + "\', "
                + "color='" + color + "\', type=" + type.toString() + ", character='" + character
                + "\', cave='" + cave.getNumberOfTreasures() + "\'}";
    }
}
