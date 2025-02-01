package mana.command;

import mana.ManaException;
import mana.UserInterface;
import mana.tasks.Deadline;
import mana.tasks.Event;
import mana.tasks.Todo;
import mana.util.TaskList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandParser {
    private static final Map<String, Command<TaskList>> commandMap = new HashMap<>();

    static {
        commandMap.put("exit", new Command<TaskList>("exit").withAction((t, m) -> Command.CommandResult.EXIT));
        commandMap.put("list", new Command<TaskList>("list").withAction((t, m) -> Command.CommandResult.OK));
         
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
                    tasklist.add(new Todo((String) args.get(Command.EMPTY_KEYWORD)));
                    return Command.CommandResult.OK;
                })
        );
        commandMap.put("deadline", new Command<TaskList>("deadline")
                .withParameterTransform(CommandTransformers.GREEDY_STRING_JOIN_TRANSFORMER)
                .withParameterTransform("by", CommandTransformers.DATETIME_TRANSFORMER)
                .withAction((tasklist, args) -> {
                    tasklist.add(new Deadline((String) args.get(Command.EMPTY_KEYWORD), 
                            (LocalDateTime) args.get("by"))
                    );
                    return Command.CommandResult.OK;
                })
        );
        commandMap.put("event", new Command<TaskList>("event")
                .withParameterTransform(CommandTransformers.GREEDY_STRING_JOIN_TRANSFORMER)
                .withParameterTransform("from", CommandTransformers.DATETIME_TRANSFORMER)
                .withParameterTransform("to", CommandTransformers.DATETIME_TRANSFORMER)
                .withAction((tasklist, args) -> {
                    tasklist.add(new Event((String) args.get(Command.EMPTY_KEYWORD), 
                            (LocalDateTime) args.get("from"),
                            (LocalDateTime) args.get("to"))
                    );
                    return Command.CommandResult.OK;
                })
        );

        commandMap.put("delete", new Command<TaskList>("delete")
                .withParameterTransform(CommandTransformers.INDEX_TRANSFORMER)
                .withAction((tasklist, args) -> {
                    tasklist.remove((Integer) args.get(Command.EMPTY_KEYWORD));
                    return Command.CommandResult.OK;
                })
        );
    }
    
    public static Command.CommandResult parseAndExecute(TaskList taskList, String[] words) {
        if (words.length == 0) return Command.CommandResult.OK;
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
