package com.market.store.domain;

public interface ProductRepository {
    Product saveOrUpdate(Product product);
}
