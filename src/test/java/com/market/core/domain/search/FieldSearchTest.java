package com.market.core.domain.search;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class FieldSearchTest {

    @Test
    void convertedValue() {
        var field = new FieldSearch();

        field.setName("name");
        field.setType(FieldSearch.FieldType.NUMBER);
        field.setValue("123");

        assertThat(field.convertedValue()).isEqualTo(Optional.of(123.0));
    }

    @Test
    void convertedValueWithConversionError() {

        assertThatThrownBy(() -> {
            var field = new FieldSearch();

            field.setName("name");
            field.setType(FieldSearch.FieldType.NUMBER);
            field.setValue("asd");

            field.convertedValue();
        })
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Value asd cant be converted to NUMBER");
    }
}