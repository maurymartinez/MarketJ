package com.market.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jeasy.random.EasyRandom;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassUtil {

    public static EasyRandom generator = new EasyRandom();

    public static ClassUtil getInstance() {
        return new ClassUtil();
    }


    public void testSetGetPair(Class<?> source) throws InvocationTargetException, IllegalAccessException {
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
