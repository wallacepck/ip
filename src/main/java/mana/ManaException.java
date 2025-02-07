package mana;

public class ManaException extends RuntimeException {
    public ManaException() {
        super();
    }

    public ManaException(String msg) {
        super(msg);
    }

    public ManaException(String format, Object... args) {
        super(String.format(format, args));
    }
}
