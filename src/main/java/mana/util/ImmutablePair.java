package mana.util;

public class ImmutablePair<T, U> {
    public final T first;
    public final U second;
    private ImmutablePair(T t, U u) {
        first = t;
        second = u;
    }

    public static <T, U> ImmutablePair<T, U> of(T t, U u) {
        return new ImmutablePair<>(t, u);
    }

    @Override
    public String toString() {
        return String.format("Pair < %s , %s >", first, second);
    }
}
