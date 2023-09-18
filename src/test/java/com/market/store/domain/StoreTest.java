package com.market.store.domain;

import com.market.core.domain.EntityNotFoundException;
import com.market.core.domain.search.PageSearch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void findProducts() {
        store.findProducts(new PageSearch());

        verify(productRepository, times(1)).findAll(any());
    }

    @Test
    void SellProduct() {
        var product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setSerial("123");
        product.setName("Product");

        when(productRepository.getProductById(eq(product.getId()))).thenReturn(Optional.of(product));

        var productSold = store.sellProduct(product.getId());

        verify(productRepository, times(1)).saveOrUpdate(argThat(Product::isSold));

        assertThat(productSold.isSold()).isTrue();
    }

    @Test
    void SellSoldProduct() {
        var product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setSerial("123");
        product.setName("Product");
        product.setSold(true);

        when(productRepository.getProductById(eq(product.getId()))).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> store.sellProduct(product.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This product has already been sold.");
    }

    @Test
    void SellProductWithNullId() {
        assertThatThrownBy(() -> store.sellProduct(null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("ProductId cant be null or blank.");
    }

    @Test
    void SellProductWithBlankId() {
        assertThatThrownBy(() -> store.sellProduct(""))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("ProductId cant be null or blank.");
    }

    @Test
    void SellNotFoundProduct() {
        var id = UUID.randomUUID().toString();
        assertThatThrownBy(() -> store.sellProduct(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Product %s not exist.", id));
    }
}
