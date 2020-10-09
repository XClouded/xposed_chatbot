package io.flutter.util;

public final class Preconditions {
    private Preconditions() {
    }

    public static <T> T checkNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }
}
