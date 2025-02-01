package mana.storage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Adapted from <a href="https://stackoverflow.com/questions/70412805/what-does-this-error-mean-java-lang-reflect-inaccessibleobjectexception-unable">...</a>
 */
public class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
        if (localDate == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(localDate.format(DateTimeFormatter.ISO_DATE_TIME));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return LocalDateTime.parse(jsonReader.nextString(), DateTimeFormatter.ISO_DATE_TIME);
    }
}
