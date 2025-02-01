package mana.storage;

import mana.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class TaskListSaveManager {
    private static final String[] FILE_PATH = {"data", "tasks.json"};
    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Task.class, new TaskTypeAdapter())
            .setPrettyPrinting()
            .create();
    
    public static List<Task> loadFromFile() throws IOException {
        Path resourcePath = Paths.get(".", FILE_PATH);
        
        if (!Files.exists(resourcePath)) throw new FileNotFoundException(
                String.format("Task data file not found at %s!", resourcePath));

        List<Task> data = null;
        try (BufferedReader reader = Files.newBufferedReader(resourcePath)) {
            data = gson.fromJson(reader, new TypeToken<List<Task>>() {}.getType());    
        }
        
        return data;
    }
    
    public static void saveToFile(List<Task> data) throws IOException {
        Path resourcePath = Paths.get(".", FILE_PATH);

        if (!Files.exists(resourcePath)) {
            Files.createDirectories(resourcePath.getParent());
            Files.createFile(resourcePath);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(resourcePath,
                Charset.defaultCharset(),
                StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(gson.toJson(data));
        }

        return;
    }   
}
