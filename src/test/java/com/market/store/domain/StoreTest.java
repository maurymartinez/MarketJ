package com.market.store.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class StoreTest {

    @MockBean
    ProductRepository productRepository;

    @Autowired
    Store store;

    @Test
    void whenAddProductWithNullThenNullPointerException() {
        assertThatThrownBy(() -> store.addProduct(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("product is marked non-null but is null");
    }

    @Test
    void whenAddProductWithProductSerialNullThenThrowIllegalStateException() {
        var product = new Product();

        assertThatThrownBy(() -> store.addProduct(product))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Product Serial cant be empty.");
    }

    @Test
    void whenAddProductWithProductSerialEmptyThenThrowIllegalStateException() {
        var product = new Product();
        product.setSerial("");

        assertThatThrownBy(() -> store.addProduct(product))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Product Serial cant be empty.");
    }

    @Test
    void whenAddProductWithProductNameNullThenThrowIllegalStateException() {
        var product = new Product();
        product.setSerial("123");

        assertThatThrownBy(() -> store.addProduct(product))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Product Name cant be empty.");
    }

    @Test
    void whenAddProductWithProductNameEmptyThenThrowIllegalStateException() {
        var product = new Product();
        product.setSerial("123");
        product.setName("");

        assertThatThrownBy(() -> store.addProduct(product))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Product Name cant be empty.");
    }

    @Test
    void whenAddProductWithProductThenProductRepository_saveOrUpdate() {
        var product = new Product();
        product.setSerial("123");
        product.setName("Product");

        store.addProduct(product);

        verify(productRepository, times(1)).saveOrUpdate(any());
    }
}
