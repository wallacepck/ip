package mana.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import mana.ManaException;

/**
 * Represents a Command with parameters, and a single action on a target object
 * @param <T> The target type
 */
public class Command<T> {
    /**
     * The key that arguments before all keywords is mapped to, e.g. "fish" in {@code delete fish --in Pacific}
     */
    public static final String EMPTY_KEYWORD = "";
    public enum CommandResult {
        OK,
        OK_SILENT,
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

    /**
     * Adds a parameter to this command.
     *
     * @param keyword The keyword for this parameter.
     * @param transformer converts the given argument to a valid argument that this command can use.
     * @return Itself.
     * @throws IllegalArgumentException If the command already has this parameter.
     */
    public Command<T> withParameterTransform(String keyword, Function<List<String>, Object> transformer) {
        if (parameters.containsKey(keyword)) {
            throw new IllegalArgumentException(String.format("Command already has keyword '%s'!", keyword));
        }
        parameters.put(keyword, transformer);
        return this;
    }

    /**
     * Adds a transformer to the default {@link #EMPTY_KEYWORD} for this command.
     *
     * @param transformer converts the given argument to a valid argument that this command can use.
     * @return Itself.
     * @throws IllegalArgumentException If the command already has a transformer for the default keyword.
     */
    public Command<T> withParameterTransform(Function<List<String>, Object> transformer) {
        return this.withParameterTransform(EMPTY_KEYWORD, transformer);
    }

    /**
     * Assigns this command the given action.
     *
     * @param action The action to use on the target object of type {@code T}.
     * @return Itself.
     */
    public Command<T> withAction(BiFunction<T, Map<String, Object>, CommandResult> action) {
        this.action = action;
        return this;
    }

    /**
     * Executes this command given the target and arguments.
     *
     * @param target Target object of action.
     * @param args Parsed argument map.
     * @return {@link CommandResult#OK} if successful, else {@link CommandResult#EXIT} if the program should exit.
     */
    public CommandResult execute(T target, Map<String, List<String>> args) {
        Map<String, Object> transformedArgs = this.transformArguments(args);
        return action.apply(target, transformedArgs);
    }

    /**
     * Transforms the given arguments into the form accepted by this command
     *
     * @return A map containing the transformed arguments. Never null, but may be empty.
     */
    public Map<String, Object> transformArguments(Map<String, List<String>> args) {
        Map<String, Object> transformedArgs = new HashMap<>();
        if (parameters.isEmpty()) {
            return transformedArgs;
        }

        for (Map.Entry<String, List<String>> arg : args.entrySet()) {
            if (parameters.containsKey(arg.getKey())) {
                if (transformedArgs.containsKey(arg.getKey())) {
                    throw new ManaException("Already specified parameter %s as %s!", arg.getKey(), transformedArgs.get(arg.getKey()));
                }
                transformedArgs.put(arg.getKey(), parameters.get(arg.getKey()).apply(arg.getValue()));
            } else {
                throw new ManaException("No such parameter %s", arg.getKey());
            }
        }

        return transformedArgs;
    }
}
