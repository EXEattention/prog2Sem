package managers;

import java.time.LocalDateTime;
import java.util.Stack;

import collections.Dragon;

/**
 * Необязательный менеджер. Создан для info
 * 
 * @author Кияшко.A.М 505196 P3118
 */
public class CollectionManager {
    private Stack<Dragon> collection;
    private LocalDateTime date;

    public CollectionManager() {
        collection = new Stack<>();
        date = LocalDateTime.now();
    }

    public Stack<Dragon> getCollection() {
        return collection;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getType() {
        return collection.getClass().getSimpleName();
    }

    public int getSize() {
        return collection.size();
    }
}
