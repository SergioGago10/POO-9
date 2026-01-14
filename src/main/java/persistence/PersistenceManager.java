package persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import persistence.StoreDTO;
import upm.CLI;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class PersistenceManager {

    private static final String FILE = "data/store.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void save(StoreDTO store) {   //Guarda en JSON
        try {
            Files.createDirectories(Path.of("data"));
            FileWriter writer = new FileWriter(FILE);
            gson.toJson(store, writer);
            writer.close();
        } catch (Exception e) {
            CLI.printErrorNextLine("Error guardando datos");
        }
    }

    public static StoreDTO load() {   //Cargas en JSON
        try {
            FileReader reader = new FileReader(FILE);
            return gson.fromJson(reader, StoreDTO.class);
        } catch (Exception e) {
            return new StoreDTO(); // si no existe, sistema vacío
        }
    }
}

