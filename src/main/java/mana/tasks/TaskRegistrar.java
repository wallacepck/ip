package mana.tasks;

import mana.storage.TaskTypeAdapter;

/**
 * Registry for task serialisation
 */
public class TaskRegistrar {
    static {
        TaskTypeAdapter.register(Deadline.class, "deadline");
        TaskTypeAdapter.register(Event.class, "event");
        TaskTypeAdapter.register(Todo.class, "todo");
    }

    /**
     * Initialises the respective serialised names of concrete task types
     */
    public static void register() {}
}
