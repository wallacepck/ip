package mana.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mana.tasks.Task;

/**
 * Wrapper class for a list of {@link Task}
 */
public class TaskList {
    private String title;
    List<Task> tasks;

    public TaskList() {
        this("Tasks", new ArrayList<>());
    }
    
    public TaskList(String title, List<Task> tasks) {
        this.title = title;
        this.tasks = tasks;
    }
    
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Removes the task at {@code index}.
     * 
     * @param index The index of the task to remove.
     * @return The Task previously at this index.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Wrapper function for {@link Task#setDone(boolean)}.
     * 
     * @param index Index of task to set.
     * @param done Whether the task is done.
     */
    public void setDone(int index, boolean done) {
        tasks.get(index).setDone(done);
    }

    public List<ImmutablePair<Integer, Task>> find(String substring) {
        List<ImmutablePair<Integer, Task>> matches = new ArrayList<>();
        Pattern p = Pattern.compile(substring);
        for (int i = 0; i < tasks.size(); i++) {
            boolean match = false;
            String title = tasks.get(i).getTitle();
            if (title.length() < 32) {
                match = title.indexOf(substring) > 0;
            } else {
                Matcher m = p.matcher(title);
                match = m.find();
            }
            if (match) matches.add(ImmutablePair.of(i, tasks.get(i)));
        }
        return matches;
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
        builder.add(title + ": ");
        for (int i = 0; i < tasks.size(); i++) {
            builder.add(String.format("%d.%s", i, tasks.get(i).toString()));
        }
        return builder.toString();
    }
}
