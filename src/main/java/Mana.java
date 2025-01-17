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

        // Main loop
        while (true) {
            System.out.print("> ");
            String rawInput = reader.nextLine();

            if (rawInput.equals("exit")) {
                break;
            } else {
                System.out.println(rawInput);
            }
        }

        // Exit
        System.out.println("Bye!");
        System.out.println(BAR);
    }
}
