package mana;

import java.io.FileNotFoundException;
import java.io.IOException;

import mana.command.Command;
import mana.command.CommandParser;
import mana.storage.TaskListSaveManager;
import mana.tasks.TaskRegistrar;
import mana.util.TaskList;

public class Mana {
    UserInterface ui;
    TaskList tasks;
    
    public static void main(String[] args) {
        TaskRegistrar.register();
        Mana agent = new Mana();
        agent.run();
    }

    public void run() {
        ui = new UserInterface();
        try {
            tasks = TaskListSaveManager.loadFromFile();
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                UserInterface.loadErr(e);
                return;
            }
        } finally {
            if (tasks == null) tasks = new TaskList();
        }

        boolean repeatInput = false;

        UserInterface.println(tasks.toString());
        // Main loop
        while (true) {
            System.out.print("> ");
            String rawInput = ui.readLine();
            if (rawInput.equals("testmode")) {
                repeatInput = true;
                rawInput = ui.readLine();
            }
            if (repeatInput) System.out.println(rawInput);
            String[] words = rawInput.split(" ");
            if (words.length == 0) continue;

            try {
                Command.CommandResult result = CommandParser.parseAndExecute(tasks, words);
                if (result == Command.CommandResult.EXIT) {
                    break;
                } else {
                    UserInterface.println(tasks.toString());
                }
            } catch (ManaException e) {
                UserInterface.commandErr(e);
            }

            try {
                TaskListSaveManager.saveToFile(tasks);
            } catch (IOException e) {
                UserInterface.println("Oh no! It seems Mana can't save your task list, please contact technical support!");
            }

            UserInterface.printBye();
        }
    }
}
