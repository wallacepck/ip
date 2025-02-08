package mana;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import mana.command.Command;
import mana.command.CommandParser;
import mana.storage.TaskListSaveManager;
import mana.tasks.TaskRegistrar;
import mana.ui.DelayedExit;
import mana.ui.GuiController;
import mana.ui.ManaApplication;
import mana.util.TaskList;

public class Mana {
    private UserInterface ui;
    private TaskList tasks;

    public static void main(String[] args) {
        TaskRegistrar.register();
        Application.launch(ManaApplication.class, args);
    }

    public Mana() {
        GuiController.getInstance().setMana(this);
        ui = new UserInterface();

        try {
            tasks = TaskListSaveManager.loadFromFile();
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                UserInterface.loadErr(e);
                Platform.exit();
            }
        } finally {
            if (tasks == null) {
                tasks = new TaskList();
            }
        }

        UserInterface.print(tasks.toString());
    }

    /**
     * Start of logical processing of user input
     */
    public void handleUserInput(String rawInput) {
        if (DelayedExit.isPendingExit()) {
            return;
        }

        String[] words = rawInput.split(" ");
        if (words.length == 0) {
            return;
        }
        ui.addUserDialog();

        try {
            Command.CommandResult result = CommandParser.parseAndExecute(tasks, words);
            if (result == Command.CommandResult.EXIT) {
                UserInterface.printBye();
                DelayedExit.exit(3000);
            } else if (result != Command.CommandResult.OK_SILENT) {
                UserInterface.print(tasks.toString());
            }
        } catch (ManaException e) {
            UserInterface.commandErr(e);
        }

        try {
            TaskListSaveManager.saveToFile(tasks);
        } catch (IOException e) {
            UserInterface.print("Oh no! It seems Mana can't save your task list, "
                    + "please contact technical support!"
            );
        }
    }
}
