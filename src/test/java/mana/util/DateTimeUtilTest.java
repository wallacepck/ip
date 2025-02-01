package mana.util;

import static mana.util.DateTimeUtil.parseStandardFormat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

public class DateTimeUtilTest {
    @Test
    public void date_valid_success() {
        assertDoesNotThrow(() -> parseStandardFormat("01-01-2025"));
    }

    @Test
    public void date_invalidSpaces_exceptionThrown() {
        assertThrows(DateTimeParseException.class, () -> parseStandardFormat("01 01 2025"));
    }

    @Test
    public void date_invalidFormat_exceptionThrown() {
        assertThrows(DateTimeParseException.class, () -> parseStandardFormat("Jan 1st 2025"));
    }

    @Test
    public void date_incomplete_exceptionThrown() {
        assertThrows(DateTimeParseException.class, () -> parseStandardFormat("2025"));
    }
}
