package com.market.store.domain;

import com.market.core.domain.EntityBaseInformation;
import com.market.core.util.Asserts;
import lombok.*;

import java.util.Collection;
import java.util.List;


@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Product extends EntityBaseInformation implements Cloneable {

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

    Product sell() {
        Asserts.assertIfNot(sold, String.format("Product %s has already been sold.", id));

        var product = this.clone();
        product.setSold(Boolean.TRUE);

        return product;
    }

    @Override
    public Product clone() {
        try {
            var clone = (Product) super.clone();
            clone.setTags(List.copyOf(tags));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
