public class ManaException extends RuntimeException {
    public ManaException(String msg) {
        super(msg);
    }

    public ManaException(String format, Object... args) {
        super(String.format(format, args));
    }
}
