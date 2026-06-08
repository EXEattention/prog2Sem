package commands;

import java.util.List;
import java.util.Scanner;

import collections.Color;
import collections.Coordinates;
import collections.Dragon;
import collections.DragonCave;
import collections.DragonCharacter;
import collections.DragonType;
import managers.*;

public class Add implements Command {
    private List<Dragon> collection;
    private DragonMeneger reader;

    public Add(List<Dragon> collection, Scanner scanner) {
        this.collection = collection;
        reader = new DragonMeneger(scanner);
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String description() {
        return "Добавить элемент в коллекцию";
    }

    @Override
    public void execute(String... args) {
        Dragon dragon;

        if (args.length > 0 && args[0] != null && args[0].startsWith("{")) {
            dragon = parseJson(args[0]);
            if (dragon == null) {
                System.out.println("Error parse");
                return;
            }
        } else {
            dragon = reader.readDragon();
        }

        if (dragon != null && dragon.validate()) {
            collection.add(dragon);
            System.out.println("EZ");
        } else {
            System.out.println("ERROR validate");
        }
    }

    private Dragon parseJson(String json) {
        try {
            String name = getString(json, "name");
            if (name == null || name.isEmpty()) {
                System.out.println("Отсутствует поле name");
                return null;
            }

            Dragon dragon = new Dragon(name);

            String ageStr = getNumber(json, "age");
            if (ageStr != null) {
                dragon.setAge(Long.parseLong(ageStr));
            }

            String colorStr = getString(json, "color");
            if (colorStr != null) {
                try {
                    dragon.setColor(Color.valueOf(colorStr.toUpperCase()));
                } catch (IllegalArgumentException e) {
                }
            }

            String typeStr = getString(json, "type");
            if (typeStr != null) {
                try {
                    dragon.setType(DragonType.valueOf(typeStr.toUpperCase()));
                } catch (IllegalArgumentException e) {
                }
            }

            String charStr = getString(json, "character");
            if (charStr != null) {
                try {
                    dragon.setCharacter(DragonCharacter.valueOf(charStr.toUpperCase()));
                } catch (IllegalArgumentException e) {
                }
            }

            String coordsJson = getObject(json, "coordinates");
            if (coordsJson != null) {
                Coordinates coords = new Coordinates();
                String xStr = getNumber(coordsJson, "x");
                String yStr = getNumber(coordsJson, "y");
                if (xStr != null)
                    coords.setX(Double.parseDouble(xStr));
                if (yStr != null)
                    coords.setY(Integer.parseInt(yStr));
                dragon.setCoordinates(coords);
            }

            String caveJson = getObject(json, "cave");
            if (caveJson != null) {
                DragonCave cave = new DragonCave();
                String treasuresStr = getNumber(caveJson, "numberOfTreasures");
                if (treasuresStr != null) {
                    cave.setNumberOfTreasures(Float.parseFloat(treasuresStr));
                }
                dragon.setCave(cave);
            }

            return dragon;

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            return null;
        }
    }

    private String getString(String json, String key) {
        String search = "\"" + key + "\"";
        int index = json.indexOf(search);
        if (index == -1)
            return null;

        index = json.indexOf(":", index);
        if (index == -1)
            return null;

        index++;
        while (index < json.length() && json.charAt(index) == ' ')
            index++;

        if (json.charAt(index) == '"') {
            index++;
            int end = json.indexOf("\"", index);
            if (end == -1)
                return null;
            return json.substring(index, end);
        }
        return null;
    }

    private String getNumber(String json, String key) {
        String search = "\"" + key + "\"";
        int index = json.indexOf(search);
        if (index == -1)
            return null;

        index = json.indexOf(":", index);
        if (index == -1)
            return null;

        index++;
        while (index < json.length() && json.charAt(index) == ' ')
            index++;

        if (index >= json.length())
            return null;

        if (json.charAt(index) == '"') {
            index++;
            int end = json.indexOf("\"", index);
            if (end == -1)
                return null;
            return json.substring(index, end);
        } else {
            int end = index;
            while (end < json.length()) {
                char c = json.charAt(end);
                if (c == ',' || c == '}' || c == ' ')
                    break;
                end++;
            }
            return json.substring(index, end);
        }
    }

    private String getObject(String json, String key) {
        String search = "\"" + key + "\"";
        int index = json.indexOf(search);
        if (index == -1)
            return null;

        index = json.indexOf(":", index);
        if (index == -1)
            return null;

        index++;
        while (index < json.length() && json.charAt(index) == ' ')
            index++;

        if (index >= json.length())
            return null;

        if (json.charAt(index) == '{') {
            int cnt = 0;
            int start = index;
            while (index < json.length()) {
                char c = json.charAt(index);
                if (c == '{')
                    cnt++;
                if (c == '}') {
                    cnt--;
                    if (cnt == 0)
                        break;
                }
                index++;
            }
            if (index >= json.length())
                return null;
            return json.substring(start, index + 1);
        }
        return null;
    }
}