package com.market.store.infrastructure;

import com.market.util.ClassUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;


class DBProductEntityTest {

    @Test
    void toDomain() {
        var dbProduct = ClassUtil.generator.nextObject(DBProductEntity.class);

        var product = dbProduct.toDomainValue();

        assertThat(dbProduct.getCreationDate()).isEqualTo(product.creationDate());
    }

    @ParameterizedTest
    @ValueSource(classes = {DBProductEntity.class})
    void setGetPairTest(Class<?> source) throws InvocationTargetException, IllegalAccessException {
        var setValues = new HashMap<String, Object>();
        var obj = ClassUtil.generator.nextObject(source);

        for (var method : obj.getClass().getMethods())
            if (method.getName().startsWith("set")) {
                var value = ClassUtil.generator.nextObject(method.getParameterTypes()[0]);
                method.invoke(obj, value);
                setValues.put(method.getName().replace("set", "get"), value);
                setValues.put(method.getName().replace("set", "is"), value);
            }

        for (var method : obj.getClass().getMethods())
            if (setValues.containsKey(method.getName()))
                assertThat(method.invoke(obj)).isEqualTo(setValues.get(method.getName()));
    }
}