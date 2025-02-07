package mana.command;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import mana.ManaException;
import mana.command.Command.CommandResult;
import mana.util.DateTimeUtil;
import mana.util.TaskList;

/**
 * Transformers transform arguments into valid Command parameters
 */
public class CommandTransformers {
    public static final Function<List<String>, Object> INDEX_TRANSFORMER = (words) -> {
        if (words.size() != 1) {
            throw new ManaException("Missing index!");
        }
        try {
            return Integer.parseInt(words.get(0));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new ManaException("Invalid number %s", words.get(0));
        }
    };

    public static final Function<List<String>, Object> GREEDY_STRING_JOIN_TRANSFORMER = (words) -> {
        if (words.isEmpty()) {
            throw new ManaException("Argument is empty!");
        }
        return String.join(" ", words);
    };

    public static final Function<List<String>, Object> DATETIME_TRANSFORMER = (words) -> {
        try {
            if (words.size() != 1) {
                throw new ManaException();
            }
            return DateTimeUtil.parseStandardFormat(words.get(0));
        } catch (DateTimeParseException | ManaException e) {
            throw new ManaException(
                    "Malformed date format! Your date format should be %s",
                    DateTimeUtil.STANDARD_INPUT_FORMAT
            );
        }
    };

    public static final Function<Boolean, BiFunction<TaskList, Map<String, Object>, CommandResult>>
            DONE_ACTION = done -> (taskList, args) -> {
                try {
                    Integer i = (Integer) args.get(Command.EMPTY_KEYWORD);
                    taskList.setDone(i, done);
                } catch (IndexOutOfBoundsException e) {
                    throw new ManaException("No task at index %s", args.get(Command.EMPTY_KEYWORD));
                }

                return CommandResult.OK;
            };
}
