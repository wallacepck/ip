package mana.tasks;

import java.time.LocalDateTime;

import mana.util.DateTimeUtil;

/**
 * Not to be confused with events used in observer design pattern
 */
public class Event extends Task {
    protected LocalDateTime startDate;
    protected LocalDateTime endDate;

    public Event(String title, LocalDateTime start, LocalDateTime end) {
        super(title);
        this.startDate = start;
        this.endDate = end;
    }

    @Override
    public String toString() {
        return String.format("%s%s (from: %s to: %s)",
                "[E]",
                super.toString(),
                DateTimeUtil.toStandardFormat(startDate),
                DateTimeUtil.toStandardFormat(endDate));
    }
}
