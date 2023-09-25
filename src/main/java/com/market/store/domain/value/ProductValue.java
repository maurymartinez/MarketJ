package com.market.store.domain.value;

import com.market.store.domain.Product;

import java.util.Collection;
import java.util.Date;


public record ProductValue(String id, String serial, String name, String type, Collection<String> tags, double price,
                           boolean sold, Date creationDate, Date lastModification) {
    public static ProductValue of(Product product) {
        return new ProductValue(product.getId(), product.getSerial(), product.getName(), product.getType(), product.getTags(),
                product.getPrice(), product.isSold(), product.getCreationDate(), product.getLastChange());
    }
}
