package mana.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mana.tasks.Task;
import mana.util.TaskList;

public class TaskListSaveManager {
    /**
     * Location of save file.
     */
    private static final String DEFAULT_FILENAME = "tasks.json";
    private static final String SAVE_DIRECTORY = "data";
    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Task.class, new TaskTypeAdapter())
            .setPrettyPrinting()
            .create();

    /**
     * Attempts to load a {@link TaskList} at {@link #SAVE_DIRECTORY}/{@code filename}.
     *
     * @return The TaskList loaded from file.
     * @throws IOException if an error occurred while reading the file.
     */
    public static TaskList loadFromFile(String filename) throws IOException {
        Path resourcePath = Paths.get(".", SAVE_DIRECTORY, filename);

        if (!Files.exists(resourcePath)) {
            throw new FileNotFoundException(
                    String.format("Task data file not found at %s!", resourcePath));
        }

        TaskList data = null;
        try (BufferedReader reader = Files.newBufferedReader(resourcePath)) {
            data = gson.fromJson(reader, new TypeToken<TaskList>() {}.getType());
        }

        return data;
    }

    /**
     * Calls {@link #loadFromFile(String)} with {@link #DEFAULT_FILENAME}
     */
    public static TaskList loadFromFile() throws IOException {
        return loadFromFile(DEFAULT_FILENAME);
    }

    /**
     * Saves the {@code taskList} to {@link #SAVE_DIRECTORY}/{@code filename}.
     *
     * @param taskList The TaskList to save.
     * @throws IOException if an error occurred while writing the file.
     */
    public static void saveToFile(TaskList taskList, String filename) throws IOException {
        Path resourcePath = Paths.get(".", SAVE_DIRECTORY, filename);

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

    /**
     * Calls {@link #saveToFile(TaskList, String)} with {@link #DEFAULT_FILENAME}
     */
    public static void saveToFile(TaskList taskList) throws IOException {
        saveToFile(taskList, DEFAULT_FILENAME);
    }
}
