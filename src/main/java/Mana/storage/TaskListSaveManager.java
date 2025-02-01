package mana.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import mana.tasks.Task;
import mana.util.TaskList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TaskListSaveManager {
    /**
     * Location of save file.
     */
    private static final String[] FILE_PATH = {"data", "tasks.json"};
    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Task.class, new TaskTypeAdapter())
            .setPrettyPrinting()
            .create();

    /**
     * Attempts to load a {@link TaskList} from the {@link #FILE_PATH}.
     * 
     * @return The TaskList loaded from file.
     * @throws IOException if an error occurred while reading the file. 
     */
    public static TaskList loadFromFile() throws IOException {
        Path resourcePath = Paths.get(".", FILE_PATH);
        
        if (!Files.exists(resourcePath)) throw new FileNotFoundException(
                String.format("Task data file not found at %s!", resourcePath));

        TaskList data = null;
        try (BufferedReader reader = Files.newBufferedReader(resourcePath)) {
            data = gson.fromJson(reader, new TypeToken<TaskList>() {}.getType());    
        }
        
        return data;
    }

    /**
     * Saves the {@code taskList} to {@link #FILE_PATH}.
     * 
     * @param taskList The TaskList to save.
     * @throws IOException if an error occurred while writing the file.
     */
    public static void saveToFile(TaskList taskList) throws IOException {
        Path resourcePath = Paths.get(".", FILE_PATH);

        if (!Files.exists(resourcePath)) {
            Files.createDirectories(resourcePath.getParent());
            Files.createFile(resourcePath);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(resourcePath,
                Charset.defaultCharset(),
                StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(gson.toJson(taskList));
        }
    }   
}
