package com.market.core.domain.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Arrays.stream;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldSearch {
    private String name;
    private Optional<String> value = Optional.empty();
    private Sort.Direction sort = Sort.Direction.ASC;
    private SearchOperation operation = SearchOperation.EQUAL;
    private FieldType type = FieldType.TEXT;

    public FieldSearch(String name) {
        this.name = name;
    }

    public FieldSearch(String name, String value) {
        this.name = name;
        this.value = Optional.ofNullable(value);
    }

    public Optional<?> getConvertedValue() {
        try {
            if (value.isPresent())
                return Optional.of(type.convertValue(value.get()));
            return value;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
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

        public SearchOperation ifNotExist(String simple) {
            return stream(FieldSearch.SearchOperation.values())
                    .filter(value -> value.getSimple().equals(simple))
                    .findFirst().orElse(this);
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
