package com.market.store.domain;

import com.market.core.domain.search.PageSearch;

import java.util.Collection;
import java.util.Optional;


public interface ProductRepository {
    Product saveOrUpdate(Product product);

    Collection<Product> findAll(PageSearch search);

    Optional<Product> getProductById(String productId);
}
