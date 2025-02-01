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
    
    public String readLine() {
        return reader.nextLine();
    }
    
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
    
    public static void printBye() {
        System.out.println("Bye!");
        printBar();
    }
    
    public static void println(String s) {
        System.out.println(s);
    }

    public static void print(String s) {
        System.out.print(s);
    }

    public static void loadErr(IOException e) {
        UserInterface.println("Oh no! Mana could not open your save file :(");
        UserInterface.println("If you still wish to start the program, move the file elsewhere!");
        UserInterface.println("Here is the detailed error log for technical support:");
        UserInterface.println(e.getLocalizedMessage());
    }
    
    public static void commandErr(ManaException e) {
        System.out.println("Failed to execute command: " + e.getMessage());
    }

    public static void printBar() {
        System.out.println(BAR);
    }
}
