package com.market.store.domain;

import com.market.core.domain.search.PageSearch;

import java.util.Collection;


public interface ProductRepository {
    Product saveOrUpdate(Product product);

    Collection<Product> findAll(PageSearch search);
}
