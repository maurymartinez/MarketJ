package com.market.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Arrays {
    public static <T> T getPositionOr(T[] array, int position, T defaultValue) {
        if (position - 1 < array.length)
            return array[position];

        return defaultValue;
    }
}
