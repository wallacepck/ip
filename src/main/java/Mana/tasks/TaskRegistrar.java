package mana.tasks;

import mana.io.TaskTypeAdapter;

public class TaskRegistrar {
    public static void register() {
        TaskTypeAdapter.register(Deadline.class, "deadline");
        TaskTypeAdapter.register(Event.class, "event");
        TaskTypeAdapter.register(Todo.class, "todo");
    }
}
