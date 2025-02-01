package mana.util;

import mana.tasks.Task;

import java.util.List;
import java.util.StringJoiner;

public class TaskList {
    private String title;
    List<Task> tasks;
    
    public TaskList(String title, List<Task> tasks) {
        this.title = title;
        this.tasks = tasks;
    }
    
    public void add(Task t) {
        tasks.add(t);
    }
    
    public Task remove(int index) {
        return tasks.remove(index);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        StringJoiner builder = new StringJoiner("\n");
        builder.add(title);
        for (int i = 0; i < tasks.size(); i++) {
            builder.add(String.format("%d.%s\n", i, tasks.get(i).toString()));
        }
        return builder.toString();
    }
}
