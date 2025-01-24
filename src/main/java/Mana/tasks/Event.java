package Mana.tasks;

/**
 * Not to be confused with events used in observer design pattern
 */
public class Event extends Task {
    protected String startDate;
    protected String endDate;

    public Event(String title, String start, String end) {
        super(title);
        this.startDate = start;
        this.endDate = end;
    }

    @Override
    public String toString() {
        return String.format("%s%s (from: %s to: %s)",
                "[E]",
                super.toString(),
                startDate,
                endDate);
    }
}
