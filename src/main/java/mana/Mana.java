package mana;

import mana.storage.TaskListSaveManager;
import mana.tasks.Deadline;
import mana.tasks.Event;
import mana.tasks.Task;
import mana.tasks.TaskRegistrar;
import mana.tasks.Todo;
import mana.util.DateTimeUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Mana {
    private static final String BAR = "____________________________________________________________";

    public static void main(String[] args) {
        TaskRegistrar.register();
        
        String logo = """
                  __  __                  \s
                 |  \\/  |                 \s
                 | \\  / | __ _ _ __   __ _\s
                 | |\\/| |/ _` | '_ \\ / _` |
                 | |  | | (_| | | | | (_| |
                 |_|  |_|\\__,_|_| |_|\\__,_|\
                 \n
                """;
        Scanner reader = new Scanner(System.in);

        System.out.println(BAR);
        System.out.println("Hello! Its me, \n" + logo + "What's up?");

        List<Task> tasks = null;
        try {
            tasks = TaskListSaveManager.loadFromFile();
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                System.out.println("Oh no! It seems your save file is corrupted :(");
                System.out.println("If you still wish to start the program, backup the file elsewhere!");
                return;
            }
        } finally {
            if (tasks == null) tasks = new ArrayList<>();
        }

        boolean repeatInput = false;

        // Main loop
        while (true) {
            System.out.print("> ");
            String rawInput = reader.nextLine();
            if (rawInput.equals("testmode")) {
                repeatInput = true;
                rawInput = reader.nextLine();
            }
            if (repeatInput) System.out.println(rawInput);
            String[] words = rawInput.split(" ");
            if (words.length == 0) continue;

            try {
                if (rawInput.equals("exit")) {
                    break;
                } else if (rawInput.equals("list")) {
                    printList("Tasks:", tasks);
                } else if (words[0].equals("done")) {
                    if (words.length == 1) throw new ManaException("Missing task specifier!");
                    try {
                        tasks.get(Integer.parseInt(words[1]) - 1).setDone(true);
                        printList("Tasks:", tasks);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        throw new ManaException("No task at index %s%n", words[1]);
                    }
                } else if (words[0].equals("undone")) {
                    if (words.length == 1) throw new ManaException("Missing task specifier!");
                    try {
                        tasks.get(Integer.parseInt(words[1]) - 1).setDone(false);
                        printList("Tasks:", tasks);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        throw new ManaException("No task at index %s%n", words[1]);
                    }
                } else if (words[0].equals("todo")) {
                    if (words.length == 1) throw new ManaException("Missing description for task!");
                    tasks.add(new Todo(String.join(
                            " ",
                            Arrays.copyOfRange(words, 1, words.length)))
                    );
                    printList("Tasks:", tasks);
                } else if (words[0].equals("deadline")) {
                    if (words.length == 1) throw new ManaException("Missing description for task!");

                    StringJoiner accum = new StringJoiner(" ");
                    String title = null;
                    String by;
                    for (int i = 1; i < words.length; i++) {
                        String s = words[i];

                        if (s.equals("--by")) {
                            title = accum.toString();
                            accum = new StringJoiner(" ");
                            continue;
                        }

                        accum.add(s);
                    }
                    by = accum.toString();
                    if (title == null) {
                        throw new ManaException("Missing deadline for task!");
                    }
                    try {
                        tasks.add(new Deadline(title, DateTimeUtil.parseStandardFormat(by)));
                    } catch (DateTimeParseException e) {
                        throw new ManaException("Malformed date format! Your date format should be %s", DateTimeUtil.STANDARD_INPUT_FORMAT);
                    }
                    printList("Tasks:", tasks);
                } else if (words[0].equals("event")) {
                    if (words.length == 1) throw new ManaException("Missing description for task!");

                    StringJoiner accum = new StringJoiner(" ");
                    String title = null;
                    String start = null;
                    String end;
                    for (int i = 1; i < words.length; i++) {
                        String s = words[i];

                        if (s.equals("--from")) {
                            title = accum.toString();
                            accum = new StringJoiner(" ");
                            continue;
                        } else if (s.equals("--to")) {
                            start = accum.toString();
                            accum = new StringJoiner(" ");
                            continue;
                        }

                        accum.add(s);
                    }
                    end = accum.toString();
                    if (title == null) {
                        throw new ManaException("Missing start date for task!");
                    } else if (start == null) {
                        throw new ManaException("Missing end date for task!");
                    }

                    try {
                        tasks.add(new Event(title, DateTimeUtil.parseStandardFormat(start), DateTimeUtil.parseStandardFormat(end)));
                    } catch (DateTimeParseException e) {
                        throw new ManaException("Malformed date format! Your date format should be %s", DateTimeUtil.STANDARD_INPUT_FORMAT);
                    }
                    
                    printList("Tasks:", tasks);
                } else if (words[0].equals("delete")) {
                    if (words.length == 1) throw new ManaException("Missing task specifier!");
                    try {
                        tasks.remove(Integer.parseInt(words[1]) - 1);
                        printList("Tasks:", tasks);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        throw new ManaException("No task at index %s%n", words[1]);
                    }
                }  else {
                    throw new ManaException("No such command: %s", rawInput);
                }
            } catch (ManaException e) {
                System.out.println(e.getMessage());
            }

            try {
                TaskListSaveManager.saveToFile(tasks);
            } catch (IOException e) {
                System.out.println("Oh no! It seems Mana can't save your task list, please contact technical support!");
            }
        }

        // Exit
        System.out.println("Bye!");
        System.out.println(BAR);
    }

    static void printList(String title, List<Task> tasks) {
        System.out.println(title);
        int index = 1;
        for (Task t : tasks) {
            System.out.printf("%d.%s\n", index++, t.toString());
        }
    }
}
