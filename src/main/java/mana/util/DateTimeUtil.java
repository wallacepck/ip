package mana.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    /**
     * The standard format for datetime input parsing
     */
    public static final String STANDARD_INPUT_FORMAT = "dd-MM-yyyy";

    /**
     * Parses {@code string} into a {@link LocalDateTime} using the {@link #STANDARD_INPUT_FORMAT}.
     *
     * @param string Input string.
     * @return Parsed {@link LocalDateTime}.
     */
    public static LocalDateTime parseStandardFormat(String string) {
        return LocalDate.parse(string, DateTimeFormatter.ofPattern(STANDARD_INPUT_FORMAT)).atStartOfDay();
    }

    /**
     * Prints {@code dateTime} into a standard pretty format.
     *
     * @param dateTime date time to print.
     * @return Pretty formatted print of {@code dateTime}.
     */
    public static String toStandardFormat(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("ccc dd MMM yyyy"));
    }
}
