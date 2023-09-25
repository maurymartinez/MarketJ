package com.market.store.domain;

import com.market.core.domain.EntityNotFoundException;
import com.market.core.domain.search.PageSearch;
import com.market.store.domain.value.ProductValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Date;
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
        var product = new ProductValue("id123", null, "", "type1", Collections.singleton("red"), 16.54, Boolean.TRUE, new Date(), new Date());

        assertThatThrownBy(() -> store.addProduct(product))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Product Serial cant be empty.");
    }

    @Test
    void whenAddProductWithProductSerialEmptyThenThrowIllegalStateException() {
        var product = new ProductValue("id123", "", "", "type1", Collections.singleton("red"), 16.54, Boolean.TRUE, new Date(), new Date());

        assertThatThrownBy(() -> store.addProduct(product))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Product Serial cant be empty.");
    }

    @Test
    void whenAddProductWithProductNameNullThenThrowIllegalStateException() {
        var product = new ProductValue("id123", "123", "", "type1", Collections.singleton("red"), 16.54, Boolean.TRUE, new Date(), new Date());

        assertThatThrownBy(() -> store.addProduct(product))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Product Name cant be empty.");
    }

    @Test
    void whenAddProductWithProductNameEmptyThenThrowIllegalStateException() {
        var product = new ProductValue("id123", "123", "", "type1", Collections.singleton("red"), 16.54, Boolean.TRUE, new Date(), new Date());

        assertThatThrownBy(() -> store.addProduct(product))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Product Name cant be empty.");
    }

    @Test
    void whenAddProductWithExistentProductThenThrowIllegalStateException() {
        var product = new ProductValue("id123", "123", "Product", "type1", Collections.singleton("red"), 16.54, Boolean.TRUE, new Date(), new Date());

        when(productRepository.getProductById(anyString())).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> store.addProduct(product))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Product with id %s already exist", product.id());
    }

    @Test
    void whenAddProductWithProductThenProductRepository_saveOrUpdate() {
        var product = new ProductValue("id123", "123", "Product", "type1", Collections.singleton("red"), 16.54, Boolean.TRUE, new Date(), new Date());

        when(productRepository.saveOrUpdate(any())).thenReturn(product);

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
        var product = new ProductValue(UUID.randomUUID().toString(), "123", "Product", "type1", Collections.singleton("red"), 16.54, Boolean.FALSE, new Date(), new Date());
        var expectedProduct = new ProductValue(UUID.randomUUID().toString(), "123", "Product", "type1", Collections.singleton("red"), 16.54, Boolean.TRUE, new Date(), new Date());

        when(productRepository.getProductById(eq(product.id()))).thenReturn(Optional.of(product));
        when(productRepository.saveOrUpdate(any(ProductValue.class))).thenReturn(expectedProduct);

        var productSold = store.sellProduct(product.id());

        verify(productRepository, times(1)).saveOrUpdate(argThat(ProductValue::sold));

        assertThat(productSold.sold()).isTrue();
    }

    @Test
    void SellSoldProduct() {
        var product = new ProductValue(UUID.randomUUID().toString(), "123", "Product", "type1", Collections.singleton("red"), 16.54, Boolean.TRUE, new Date(), new Date());

        when(productRepository.getProductById(eq(product.id()))).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> store.sellProduct(product.id()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("Product %s has already been sold.", product.id()));
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
                .hasMessage(String.format("Product %s not found.", id));
    }

    @Test
    void getTotalProducts() {
        when(productRepository.getNumberOfProducts(anyBoolean())).thenReturn(15l);

        assertThat(store.getTotalProducts()).isEqualTo(15l);
    }
}
