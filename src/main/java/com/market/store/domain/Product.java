package com.market.store.domain;

import com.market.core.domain.EntityBaseInformation;
import com.market.core.util.Asserts;
import com.market.store.domain.value.ProductValue;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@NoArgsConstructor
public class Product extends EntityBaseInformation implements Cloneable {

    Product(ProductValue value) {
        id = value.id();
        serial = value.serial();
        name = value.name();
        type = value.type();
        tags = value.tags();
        price = value.price();
        sold = value.sold();
        creationDate = value.creationDate();
        lastChange = value.lastModification();
    }

    private String serial;
    private String name;

    private String type;
    private Collection<String> tags = new ArrayList<>();
    private double price;
    private boolean sold;

    Product sell() {
        Asserts.assertIfNot(sold, String.format("Product %s has already been sold.", id));

        var product = this.clone();
        product.sold = Boolean.TRUE;

        return product;
    }

    @Override
    public Product clone() {
        try {
            var clone = (Product) super.clone();
            clone.tags = List.copyOf(tags);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
