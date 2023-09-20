package com.market;

import com.market.store.domain.Product;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;


class GetSetTests {

    EasyRandom generator = new EasyRandom();

    @ParameterizedTest
    @ValueSource(classes = {Product.class})
    void setGetPairTest(Class<?> source) throws InvocationTargetException, IllegalAccessException {
        var setValues = new HashMap<String, Object>();
        var obj = generator.nextObject(source);

        for (var method : obj.getClass().getMethods())
            if (method.getName().startsWith("set")) {
                var value = generator.nextObject(method.getParameterTypes()[0]);
                method.invoke(obj, value);
                setValues.put(method.getName().replace("set", "get"), value);
                setValues.put(method.getName().replace("set", "is"), value);
            }

        for (var method : obj.getClass().getMethods())
            if (setValues.containsKey(method.getName()))
                assertThat(method.invoke(obj)).isEqualTo(setValues.get(method.getName()));

    }

}
