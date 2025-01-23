import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
            } else if (words[0].equals("add")) {
                if (words.length == 1) System.out.print("Missing description for task!");
                tasks.add(new Task(String.join(
                        " ",
                        Arrays.copyOfRange(words, 1, words.length)))
                );
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
            System.out.printf("%d.%s %s\n", index++,
                    t.isDone ? "[X]" : "[ ]",
                    t.getTitle());
        }
    }
}
