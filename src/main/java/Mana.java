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

        System.out.println(BAR);
        System.out.println("Hello! I'm \n" + logo + "What's up today?");
        System.out.println(BAR);
        System.out.println("Bye!");
        System.out.println(BAR);
    }
}
