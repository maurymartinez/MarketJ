package com.market.store.domain;

import com.market.core.util.Asserts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Store {

    private final ProductRepository productRepository;

    public Product addProduct(@NonNull Product product) {

        Asserts.assertNonNullOrEmpty(product.getSerial(), "Product Serial cant be empty.");
        Asserts.assertNonNullOrEmpty(product.getName(), "Product Name cant be empty.");

        return productRepository.saveOrUpdate(product);
    }

}
