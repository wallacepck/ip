package mana;

import java.io.IOException;
import java.util.Scanner;

public class UserInterface {
    private static final String BAR = "____________________________________________________________";
    private final Scanner reader;
    
    public UserInterface() {
        this.reader = new Scanner(System.in);
        printGreet();
    }

    /**
     * Reads a line from {@code Scanner} input {@link #reader}.
     * 
     * @return the next line from {@link #reader}.
     */
    public String readLine() {
        return reader.nextLine();
    }

    /**
     * Greets the user.
     */
    public void printGreet() {
        String logo = """
                  __  __                  \s
                 |  \\/  |                 \s
                 | \\  / | __ _ _ __   __ _\s
                 | |\\/| |/ _` | '_ \\ / _` |
                 | |  | | (_| | | | | (_| |
                 |_|  |_|\\__,_|_| |_|\\__,_|\
                 \n
                """;

        printBar();
        System.out.println("Hello! Its me, \n" + logo + "Here is your task list,");
    }

    /**
     * Sends a farewell message to the user.
     */
    public static void printBye() {
        System.out.println("Bye!");
        printBar();
    }

    /**
     * Prints {@code s} and advances to the next line.
     * 
     * @param s The string to print.
     */
    public static void println(String s) {
        System.out.println(s);
    }

    /**
     * Prints {@code s}.
     *
     * @param s The string to print.
     */
    public static void print(String s) {
        System.out.print(s);
    }


    /**
     * Prints an error message to the user describing the file load error {@code e}.
     *
     * @param e The error occurred during file load.
     */
    public static void loadErr(IOException e) {
        UserInterface.println("Oh no! Mana could not open your save file :(");
        UserInterface.println("If you still wish to start the program, move the file elsewhere!");
        UserInterface.println("Here is the detailed error log for technical support:");
        UserInterface.println(e.getLocalizedMessage());
    }

    /**
     * Prints an error message to the user describing the command error {@code e}.
     *
     * @param e The error occurred during command parsing or execution.
     */
    public static void commandErr(ManaException e) {
        System.out.println("Failed to execute command: " + e.getMessage());
    }

    /**
     * Prints a standard issue horizontal {@link #BAR bar}.
     */
    public static void printBar() {
        System.out.println(BAR);
    }
}
