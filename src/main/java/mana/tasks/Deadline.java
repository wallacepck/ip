package mana.tasks;

import java.time.LocalDateTime;

import mana.util.DateTimeUtil;

public class Deadline extends Task {
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + DateTimeUtil.toStandardFormat(by) + ")";
    }
}
