package com.market.store.domain;

import com.market.core.domain.EntityBaseInformation;
import com.market.core.util.Asserts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends EntityBaseInformation {

    public Product(String id, String serial, String name, String type, Collection<String> tags, double price, boolean sold) {
        this.id = id;
        this.serial = serial;
        this.name = name;
        this.type = type;
        this.tags = tags;
        this.price = price;
        this.sold = sold;
    }

    private String serial;
    private String name;

    private String type;
    private Collection<String> tags;
    private double price;
    private boolean sold;

    public Product sell() {
        Asserts.assertIfNot(sold, String.format("Product %s has already been sold.", id));

        sold = Boolean.TRUE;

        return new Product(id, serial, name, type, List.copyOf(tags), price, sold);
    }
}
