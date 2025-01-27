package mana.io;

import mana.tasks.Task;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TaskTypeAdapter implements JsonSerializer<Task>, JsonDeserializer<Task> {
    private static final Gson gson = new Gson();
    private static final Map<Class<? extends Task>, String> serialisedTypeNames = new HashMap<>();
    private static final Map<String, Class<? extends Task>> deserialisedTypes = new HashMap<>();
    
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
