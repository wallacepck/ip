package mana.command;

import static mana.command.CommandTransformers.DATETIME_TRANSFORMER;
import static mana.command.CommandTransformers.INDEX_TRANSFORMER;
import static mana.util.DateTimeUtil.STANDARD_INPUT_FORMAT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CommandTransformersTest {
    @Test
    public void index_number_success() {
        List<String> words = new ArrayList<>();
        words.add("5");
        assertEquals(INDEX_TRANSFORMER.apply(words), 5);
    }

    @Test
    public void index_notANumber_exceptionThrown() {
        List<String> words = new ArrayList<>();
        words.add("five");
        try {
            INDEX_TRANSFORMER.apply(words);
            fail();
        } catch (Exception e) {
            assertEquals("Invalid number five", e.getMessage());
        }
    }

    @Test
    public void index_mixed_exceptionThrown() {
        List<String> words = new ArrayList<>();
        words.add("-9^k");
        try {
            INDEX_TRANSFORMER.apply(words);
            fail();
        } catch (Exception e) {
            assertEquals("Invalid number -9^k", e.getMessage());
        }
    }

    @Test
    public void date_valid_success() {
        List<String> words = new ArrayList<>();
        words.add("01-01-2025");
        assertEquals(DATETIME_TRANSFORMER.apply(words), 
                LocalDate.parse("01-01-2025", DateTimeFormatter.ofPattern(STANDARD_INPUT_FORMAT)).atStartOfDay());
    }

    @Test
    public void date_invalidSpaces_exceptionThrown() {
        List<String> words = new ArrayList<>();
        words.add("01 01 2025");
        try {
            DATETIME_TRANSFORMER.apply(words);
            fail();
        } catch (Exception e) {
            assertEquals("Malformed date format! Your date format should be dd-MM-yyyy", e.getMessage());
        }
    }

    @Test
    public void date_invalidFormat_exceptionThrown() {
        List<String> words = new ArrayList<>();
        words.add("Jan 1st 2025");
        try {
            DATETIME_TRANSFORMER.apply(words);
            fail();
        } catch (Exception e) {
            assertEquals("Malformed date format! Your date format should be dd-MM-yyyy", e.getMessage());
        }
    }

    @Test
    public void date_incomplete_exceptionThrown() {
        List<String> words = new ArrayList<>();
        words.add("2025");
        try {
            DATETIME_TRANSFORMER.apply(words);
            fail();
        } catch (Exception e) {
            assertEquals("Malformed date format! Your date format should be dd-MM-yyyy", e.getMessage());
        }
    }
}
