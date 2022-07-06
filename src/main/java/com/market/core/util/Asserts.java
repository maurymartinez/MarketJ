package com.market.core.util;

import java.util.Objects;

public class Asserts {
    public static void assertNonNull(Object o, String message) {
        if (Objects.isNull(o))
            throw new IllegalStateException(message);
    }

    public static void assertNonNullOrEmpty(String s, String message) {
        assertNonNull(s, message);
        if (s.isEmpty())
            throw new IllegalStateException(message);
    }
}
