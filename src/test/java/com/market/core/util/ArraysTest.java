package com.market.core.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ArraysTest {

    @Test
    void getPositionOr() {
        var testArray = new Integer[]{
                1, 2, 3, 4, 5
        };

        assertThat(Arrays.getPositionOr(testArray, 2, 0)).isEqualTo(3);
        assertThat(Arrays.getPositionOr(testArray, 7, 80)).isEqualTo(80);
    }
}