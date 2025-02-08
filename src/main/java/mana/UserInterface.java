package mana;

import java.io.IOException;

import mana.ui.GuiController;

public class UserInterface {
    private static GuiController gui;

    public UserInterface() {
        if (gui == null) {
            gui = GuiController.getInstance();
        }
        printGreet();
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

        print("Hello! Its me, \n" + logo + "Here is your task list,");
    }

    /**
     * Sends a farewell message to the user.
     */
    public static void printBye() {
        print("Bye!");
    }

    /**
     * Prints {@code s} in a dialog box.
     *
     * @param s The string to print.
     */
    public static void print(String s) {
        gui.addRespondDialog(s);
    }


    /**
     * Prints an error message to the user describing the file load error {@code e}.
     *
     * @param e The error occurred during file load.
     */
    public static void loadErr(IOException e) {
        UserInterface.print("""
        Oh no! Mana could not open your save file :(
        If you still wish to start the program, move the file elsewhere!
        Here is the detailed error log for technical support
        """ + e.getLocalizedMessage()
        );
    }

    /**
     * Prints an error message to the user describing the command error {@code e}.
     *
     * @param e The error occurred during command parsing or execution.
     */
    public static void commandErr(ManaException e) {
        print("Failed to execute command: " + e.getMessage());
    }

    /**
     * add user dialog box to dialog container
     */
    public void addUserDialog() {
        gui.addUserDialog();
    }
}
