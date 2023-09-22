package com.market.store.domain;

import com.market.core.domain.EntityBaseInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;


@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends EntityBaseInformation {

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
