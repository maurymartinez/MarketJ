package com.market.core.util;

public class Arrays {
    public static <T> T getPositionOr(T[] array, int position, T defaultValue) {
        if (position - 1 < array.length)
            return array[position];

        return defaultValue;
    }
}
