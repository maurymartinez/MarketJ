package com.market.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Asserts {
    public static void assertNonNull(Object o, String message) {
        assertIfNot(Objects.isNull(o), message);
    }

    public static void assertNonNullOrEmpty(String s, String message) {
        assertNonNull(s, message);
        if (s.isEmpty())
            throw new IllegalStateException(message);
    }

    public static void assertIfNot(boolean condition, String message) {
        if (condition)
            throw new IllegalStateException(message);
    }
}
