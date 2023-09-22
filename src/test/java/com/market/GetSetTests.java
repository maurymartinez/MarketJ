package com.market;

import com.market.store.domain.Product;
import com.market.util.ClassUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.InvocationTargetException;


class GetSetTests {

    @ParameterizedTest
    @ValueSource(classes = {Product.class})
    void setGetPairTest(Class<?> source) throws InvocationTargetException, IllegalAccessException {
        ClassUtil.getInstance().testSetGetPair(source);
    }

}
