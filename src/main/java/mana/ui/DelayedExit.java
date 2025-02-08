package mana.ui;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;

/**
 * Helper class to exit the application after a delay,
 */
public class DelayedExit extends TimerTask {
    private static boolean isPendingExit = false;
    private static final Timer timer = new Timer();

    private DelayedExit() {}

    /**
     * Exits the program after {@code miliseconds} amount of time.
     * Uses {@link Timer} to schedule the exit.
     *
     * @param miliseconds delay in miliseconds
     */
    public static void exit(int miliseconds) {
        isPendingExit = true;
        timer.schedule(new DelayedExit(), miliseconds);
    }

    /**
     * Returns if a program exit has been scheduled via {@link DelayedExit#exit(int)}
     * @return if the program is scheduled to exit
     */
    public static boolean isPendingExit() {
        return isPendingExit;
    }

    @Override
    public void run() {
        Platform.exit();
        this.cancel();
        timer.cancel();
    }
}
