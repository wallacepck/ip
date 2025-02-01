package mana.command;

import mana.ManaException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Command<T> {
    public static final String EMPTY_KEYWORD = "";
    public enum CommandResult {
        OK,
        ERROR,
        EXIT
    }
    public final String name;
    private final Map<String, Function<List<String>, Object>> parameters;
    private BiFunction<T, Map<String, Object>, CommandResult> action;
    
    public Command(String name) {
        this.name = name;
        this.parameters = new HashMap<>();
    }
    
    public Command<T> withParameterTransform(String keyword, Function<List<String>, Object> transformer) {
        if (parameters.containsKey(keyword)) {
            throw new IllegalArgumentException(String.format("Command already has keyword '%s'!", keyword));
        }
        parameters.put(keyword, transformer);
        return this;
    }

    public Command<T> withParameterTransform(Function<List<String>, Object> transformer) {
        return this.withParameterTransform(EMPTY_KEYWORD, transformer);
    }
    
    public Command<T> withAction(BiFunction<T, Map<String, Object>, CommandResult> action) {
        this.action = action;
        return this;
    }
    
    public CommandResult execute(T target, Map<String, List<String>> args) {
        Map<String, Object> transformedArgs = new HashMap<>();
        if (!parameters.isEmpty()) {
            for (Map.Entry<String, List<String>> arg : args.entrySet()) {
                if (parameters.containsKey(arg.getKey())) {
                    transformedArgs.put(arg.getKey(), parameters.get(arg.getKey()).apply(arg.getValue()));
                } else {
                    throw new ManaException("No such keyword %s", arg.getKey());
                }
            }
        }

        return action.apply(target, transformedArgs);
    }
}
