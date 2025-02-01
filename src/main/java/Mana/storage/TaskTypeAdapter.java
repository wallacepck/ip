package mana.storage;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import mana.tasks.Task;

/**
 * Gson de/serialiser for subclasses of {@link Task}.
 */
public class TaskTypeAdapter implements JsonSerializer<Task>, JsonDeserializer<Task> {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();
    private static final Map<Class<? extends Task>, String> serialisedTypeNames = new HashMap<>();
    private static final Map<String, Class<? extends Task>> deserialisedTypes = new HashMap<>();

    /**
     * Registers the subclass of {@link Task} to the serialisation name map.
     * <p>
     * If a subclass of {@code Task} is not registered, de/serialisation of the subclass will fail.
     * 
     * @param clazz The subclass of {@link Task}.
     * @param typename The serialised name of {@code clazz}.
     */
    public static void register(Class<? extends Task> clazz, String typename) {
        if (serialisedTypeNames.containsValue(typename)) throw new IllegalArgumentException();
        serialisedTypeNames.put(clazz, typename);
        deserialisedTypes.put(typename, clazz);
    }
    
    @Override
    public JsonElement serialize(Task src, Type typeOfSrc, JsonSerializationContext context) {
        if (!serialisedTypeNames.containsKey(src.getClass())) {
            throw new NullPointerException(String.format("Cannot find serialised name for %s!", src.getClass()));
        }
        JsonObject json = gson.toJsonTree(src).getAsJsonObject();
        json.addProperty("type", serialisedTypeNames.get(src.getClass()));
        return json;
    }

    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String typeName = jsonObject.get("type").getAsString();

        if (deserialisedTypes.containsKey(typeName)){
            Class<? extends Task> clazz = deserialisedTypes.get(typeName);
            return gson.fromJson(json, clazz);
        } else {
            throw new JsonParseException(String.format("Cannot find Task of type %s", typeName), new ClassNotFoundException());
        }
    }
}
