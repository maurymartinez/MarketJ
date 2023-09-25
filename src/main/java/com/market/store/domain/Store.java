package com.market.store.domain;

import com.market.core.domain.EntityNotFoundException;
import com.market.core.domain.search.PageSearch;
import com.market.core.util.Asserts;
import com.market.store.domain.value.ProductValue;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Store {

    private final ProductRepository productRepository;

    public ProductValue addProduct(@NonNull ProductValue product) {

        Asserts.assertNonNullOrEmpty(product.serial(), "Product Serial cant be empty.");
        Asserts.assertNonNullOrEmpty(product.name(), "Product Name cant be empty.");

        if (Strings.isNotBlank(product.id()))
            productRepository.getProductById(product.id()).ifPresent(productStored -> {
                throw new IllegalStateException(String.format("Product with id %s already exist", product.id()));
            });

        return productRepository.saveOrUpdate(product);
    }

    public List<ProductValue> findProducts(PageSearch search) {
        return productRepository.findAll(search).stream()
                .toList();
    }

    public ProductValue sellProduct(String productId) {
        Asserts.assertNonNullOrEmpty(productId, "ProductId cant be null or blank.");

        var productVl = productRepository.getProductById(productId);

        if (productVl.isPresent()) {
            var product = new Product(productVl.get());
            return productRepository.saveOrUpdate(ProductValue.of(product.sell()));
        }

        throw new EntityNotFoundException(String.format("Product %s not found.", productId));
    }

    public long getTotalProducts() {
        return productRepository.getNumberOfProducts(Boolean.FALSE);
    }

}
