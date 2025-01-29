package mana.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final String STANDARD_INPUT_FORMAT = "dd-MM-yyyy";
    
    public static LocalDateTime parseStandardFormat(String string) {
        return LocalDate.parse(string, DateTimeFormatter.ofPattern(STANDARD_INPUT_FORMAT)).atStartOfDay();
    }
    
    public static String toStandardFormat(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("ccc dd MMM yyyy"));
    }
}
