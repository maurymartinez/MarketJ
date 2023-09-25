package com.market.store.domain;

import com.market.core.domain.search.PageSearch;
import com.market.store.domain.value.ProductValue;

import java.util.Collection;
import java.util.Optional;


public interface ProductRepository {
    ProductValue saveOrUpdate(ProductValue product);

    Collection<ProductValue> findAll(PageSearch search);

    Optional<ProductValue> getProductById(String productId);

    long getNumberOfProducts(boolean sold);
}
