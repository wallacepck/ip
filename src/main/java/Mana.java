import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.Todo;

import java.util.*;

public class Mana {
    private static final String BAR = "____________________________________________________________";

    public static void main(String[] args) {
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

        List<Task> tasks = new ArrayList<>();

        // Main loop
        while (true) {
            System.out.print("> ");
            String rawInput = reader.nextLine();
            String[] words = rawInput.split(" ");
            if (words.length == 0) continue;

            if (rawInput.equals("exit")) {
                break;
            } else if (rawInput.equals("list")) {
                printList("Tasks:", tasks);
                System.out.println("Tasks:");
            } else if (words[0].equals("done")) {
                if (words.length == 1) System.out.print("Missing task specifier!");
                try {
                    tasks.get(Integer.parseInt(words[1]) - 1).setDone(true);
                    printList("Tasks:", tasks);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.printf("No task at index %s%n", words[1]);
                }
            } else if (words[0].equals("undone")) {
                if (words.length == 1) System.out.print("Missing task specifier!");
                try {
                    tasks.get(Integer.parseInt(words[1]) - 1).setDone(false);
                    printList("Tasks:", tasks);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.printf("No task at index %s%n", words[1]);
                }
            } else if (words[0].equals("todo")) {
                if (words.length == 1) System.out.print("Missing description for task!");
                tasks.add(new Todo(String.join(
                        " ",
                        Arrays.copyOfRange(words, 1, words.length)))
                );
                printList("Tasks:", tasks);
            } else if (words[0].equals("deadline")) {
                if (words.length == 1) System.out.print("Missing description for task!");

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
                if (title == null || accum.length() == 0) return;

                tasks.add(new Deadline(title, by));
                printList("Tasks:", tasks);
            } else if (words[0].equals("event")) {
                if (words.length == 1) System.out.print("Missing description for task!");

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
                if (title == null || start == null || accum.length() == 0) return;

                tasks.add(new Event(title, start, end));
                printList("Tasks:", tasks);
            } else {
                System.out.printf("No such command: %s\n", rawInput);
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
