package mana.command;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mana.ManaException;
import mana.UserInterface;
import mana.storage.TaskListSaveManager;
import mana.tasks.Deadline;
import mana.tasks.Event;
import mana.tasks.Task;
import mana.tasks.Todo;
import mana.util.ImmutablePair;
import mana.util.TaskList;

/**
 * Singleton parser class for all ({@link TaskList}) commands
 */
public class CommandParser {
    private static final Map<String, Command<TaskList>> commandMap = new HashMap<>();

    static {
        commandMap.put("exit", new Command<TaskList>("exit").withAction((t, m) -> Command.CommandResult.EXIT));
        commandMap.put("list", new Command<TaskList>("list").withAction((t, m) -> Command.CommandResult.OK));

        commandMap.put("load", new Command<TaskList>("load")
                .withParameterTransform(CommandTransformers.SINGLE_WORD_TRANSFORMER)
                .withAction((tasklist, args) -> {
                    String filename = (String) args.get(Command.EMPTY_KEYWORD);
                    if (filename == null) {
                        throw new ManaException("File name is empty!");
                    }
                    try {
                        tasklist.replaceWith(TaskListSaveManager.loadFromFile(filename));
                    } catch (IOException e) {
                        throw new ManaException("Cannot load from %s due to %s", filename, e.getLocalizedMessage());
                    }

                    return Command.CommandResult.OK;
                })
        );

        commandMap.put("done", new Command<TaskList>("done")
                .withParameterTransform(CommandTransformers.INDEX_TRANSFORMER)
                .withAction(CommandTransformers.DONE_ACTION.apply(true))
        );
        commandMap.put("undone", new Command<TaskList>("undone")
                .withParameterTransform(CommandTransformers.INDEX_TRANSFORMER)
                .withAction(CommandTransformers.DONE_ACTION.apply(false))
        );

        commandMap.put("todo", new Command<TaskList>("todo")
                .withParameterTransform(CommandTransformers.GREEDY_STRING_JOIN_TRANSFORMER)
                .withAction((tasklist, args) -> {
                    String title = (String) args.get(Command.EMPTY_KEYWORD);
                    if (title == null) {
                        throw new ManaException("Title is empty!");
                    }

                    tasklist.add(new Todo(title));
                    return Command.CommandResult.OK;
                })
        );
        commandMap.put("deadline", new Command<TaskList>("deadline")
                .withParameterTransform(CommandTransformers.GREEDY_STRING_JOIN_TRANSFORMER)
                .withParameterTransform("by", CommandTransformers.DATETIME_TRANSFORMER)
                .withAction((tasklist, args) -> {
                    String title = (String) args.get(Command.EMPTY_KEYWORD);
                    if (title == null) {
                        throw new ManaException("Title is empty!");
                    }
                    LocalDateTime deadlineDate = (LocalDateTime) args.get("by");
                    if (deadlineDate == null) {
                        throw new ManaException("Deadline date is empty!");
                    }

                    tasklist.add(new Deadline(title, deadlineDate)
                    );
                    return Command.CommandResult.OK;
                })
        );
        commandMap.put("event", new Command<TaskList>("event")
                .withParameterTransform(CommandTransformers.GREEDY_STRING_JOIN_TRANSFORMER)
                .withParameterTransform("from", CommandTransformers.DATETIME_TRANSFORMER)
                .withParameterTransform("to", CommandTransformers.DATETIME_TRANSFORMER)
                .withAction((tasklist, args) -> {
                    String title = (String) args.get(Command.EMPTY_KEYWORD);
                    if (title == null) {
                        throw new ManaException("Title is empty!");
                    }
                    LocalDateTime fromDate = (LocalDateTime) args.get("from");
                    if (fromDate == null) {
                        throw new ManaException("Date from is empty!");
                    }
                    LocalDateTime toDate = (LocalDateTime) args.get("to");
                    if (toDate == null) {
                        throw new ManaException("Date to is empty!");
                    }

                    tasklist.add(new Event((String) args.get(Command.EMPTY_KEYWORD), fromDate, toDate)
                    );
                    return Command.CommandResult.OK;
                })
        );

        commandMap.put("delete", new Command<TaskList>("delete")
                .withParameterTransform(CommandTransformers.INDEX_TRANSFORMER)
                .withAction((tasklist, args) -> {
                    Integer index = (Integer) args.get(Command.EMPTY_KEYWORD);
                    if (index == null) {
                        throw new ManaException("Index is empty!");
                    }
                    tasklist.remove(index);
                    return Command.CommandResult.OK;
                })
        );

        commandMap.put("find", new Command<TaskList>("find")
                .withParameterTransform(CommandTransformers.GREEDY_STRING_JOIN_TRANSFORMER)
                .withAction((tasklist, args) -> {
                    String keyphrase = (String) args.get(Command.EMPTY_KEYWORD);
                    if (keyphrase == null) {
                        throw new ManaException("Given text to find is empty!");
                    }

                    List<ImmutablePair<Integer, Task>> found = tasklist.find(keyphrase);
                    StringBuilder builder = new StringBuilder();
                    builder.append("Found the following matches: \n");
                    for (ImmutablePair<Integer, Task> pair : found) {
                        builder.append(String.format("%d.%s\n", pair.first, pair.second));
                    }
                    UserInterface.print(builder.toString());
                    return Command.CommandResult.OK_SILENT;
                })
        );
    }

    /**
     * Parses the word list into an argument map and executes the command on the {@code taskList}.
     *
     * @param taskList The {@link TaskList} the command should operate on.
     * @param words The word array from the command line input.
     * @return See: {@link Command#execute(Object, Map)}.
     */
    public static Command.CommandResult parseAndExecute(TaskList taskList, String[] words) {
        if (words.length == 0) {
            return Command.CommandResult.OK;
        }
        if (commandMap.containsKey(words[0])) {
            Map<String, List<String>> args = new HashMap<>();
            String currentKeyword = Command.EMPTY_KEYWORD;
            for (int i = 1; i < words.length; i++) {
                String s = words[i];
                if (s.startsWith("--")) {
                    currentKeyword = s.substring(2);
                } else {
                    args.computeIfAbsent(currentKeyword, k -> new ArrayList<>()).add(s);
                }
            }

            return commandMap.get(words[0]).execute(taskList, args);
        } else {
            throw new ManaException("No such command: %s", words[0]);
        }
    }
}
