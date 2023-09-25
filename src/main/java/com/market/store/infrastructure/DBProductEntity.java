package com.market.store.infrastructure;

import com.market.core.domain.BaseInformation;
import com.market.store.domain.Product;
import com.market.store.domain.value.ProductValue;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@Document
@Getter
@Setter
@Builder
class DBProductEntity {

    public static DBProductEntity from(ProductValue product) {
        return DBProductEntity.builder()
                .id(product.id())
                .serial(product.serial())
                .name(product.name())
                .type(product.type())
                .tags(List.copyOf(product.tags()))
                .price(product.price())
                .sold(product.sold())
                .creationDate(product.creationDate())
                .lastChange(product.lastModification())
                .build();
    }

    @Id
    private String id;

    private String serial;
    private String name;

    private String type;
    private Collection<String> tags;
    private double price;
    private boolean sold;
    private Date creationDate;
    private Date lastChange;

    public ProductValue toDomainValue() {
        return new ProductValue(id, serial, name, type, List.copyOf(tags), price, sold, creationDate, lastChange);
    }

}
