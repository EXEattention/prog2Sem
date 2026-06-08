package managers;

import java.util.List;
import collections.Dragon;
import java.io.FileInputStream;
import java.io.IOException;

public class LoaderManager {
    private List<Dragon> collections;
    private String path = "";
    
    public LoaderManager(List<Dragon> collections) {
        this.collections = collections;
    }

    public void load(String... args) throws IOException {
        String cfgPath = "cfg.txt";
        if (args != null && args.length > 0 && args[0] != null && !args[0].isEmpty()) {
            cfgPath = args[0];
        }
        try (FileInputStream file = new FileInputStream(cfgPath)) {
            int i;
            StringBuilder sb = new StringBuilder();
            while ((i = file.read()) != -1) {
                sb.append((char) i);
            }
            path = sb.toString();
        }
        
        FileManager pepaShnele = new FileManager(path);
        collections.addAll(pepaShnele.loadFromFile());
    }
}