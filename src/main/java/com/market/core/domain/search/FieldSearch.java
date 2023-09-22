package com.market.core.domain.search;

import ch.qos.logback.core.CoreConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Sort;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldSearch {
    private String name;
    private String value = CoreConstants.EMPTY_STRING;
    private Sort.Direction sort = Sort.Direction.ASC;
    private SearchOperation operation = SearchOperation.EQUAL;
    private FieldType type = FieldType.TEXT;

    public Optional<?> convertedValue() {
        try {
            if (Strings.isNotBlank(value))
                return Optional.of(type.convertValue(value));
            return Optional.empty();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(String.format("Value %s cant be converted to %s", value, type.name()));
        }
    }

    @Getter
    public enum SearchOperation {
        CONTAINS("cn"), DOES_NOT_CONTAIN("nc"), EQUAL("eq"), NOT_EQUAL("ne"),
        GREATER_THAN("gt"), GREATER_THAN_EQUAL("ge"), LESS_THAN("lt"), LESS_THAN_EQUAL("le");

        private final String simple;

        SearchOperation(String simple) {
            this.simple = simple;
        }
    }

    public enum FieldType {
        NUMBER(Double.class), TEXT(String.class), BOOLEAN((Boolean.class)), LIST(CustomList.class);

        private Class<?> clazz;

        FieldType(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Object convertValue(String value) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            var constructor = clazz.getConstructor(String.class);

            return constructor.newInstance(value);
        }
    }

    public static class CustomList extends ArrayList<Object> {
        public CustomList(String values) {
            this.addAll(Arrays.asList(values.split(",")));
        }
    }
}
