package com.market.core.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssertsTest {

    @Test
    void whenAssertNonNullWithNullValueShouldThrowIllegalStateException() {
        assertThatThrownBy(() -> Asserts.assertNonNull(null, "Some message"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Some message");
    }

    @Test
    void whenAssertNonNullWithNoNullValueShouldNoThrowIllegalStateException() {
        Asserts.assertNonNull(new Object(), "Some message");
    }

    @Test
    void whenAssertNonNullOrEmptyWithEmptyValueShouldThrowIllegalStateException() {
        assertThatThrownBy(() -> Asserts.assertNonNullOrEmpty("", "Some message"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Some message");
    }

    @Test
    void whenAssertNonNullOrEmptyWithNullValueShouldThrowIllegalStateException() {
        assertThatThrownBy(() -> Asserts.assertNonNullOrEmpty("", "Some message"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Some message");
    }

    @Test
    void whenAssertNonNullOrEmptyWithNoNullNoEmptyValueShouldNoThrowIllegalStateException() {
        Asserts.assertNonNullOrEmpty("some value", "Some message");
    }

    @Test
    void whenAssertIfNotWithTrueShouldThrowIllegalStateException() {
        assertThatThrownBy(() -> Asserts.assertIfNot(Boolean.TRUE, "Some message"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Some message");
    }

    @Test
    void whenAssertIfNotWithFalseValueShouldNoThrowIllegalStateException() {
        Asserts.assertIfNot(Boolean.FALSE, "Some message");
    }
}
