package net.protocol;

import java.io.Serializable;
import java.util.List;
import collections.Dragon;

public class CommandResponse implements Serializable {
    private boolean success;
    private String message;
    private List<Dragon> collectionSnapshot;

    public CommandResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public CommandResponse(boolean success, String message, List<Dragon> collectionSnapshot) {
        this.success = success;
        this.message = message;
        this.collectionSnapshot = collectionSnapshot;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Dragon> getCollectionSnapshot() {
        return collectionSnapshot;
    }
}
