package com.market.store.infrastructure;

import com.market.core.domain.BaseInformation;
import com.market.store.domain.Product;
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

    public static DBProductEntity from(Product product) {
        return DBProductEntity.builder()
                .id(product.getId())
                .serial(product.getSerial())
                .name(product.getName())
                .type(product.getType())
                .tags(List.copyOf(product.getTags()))
                .price(product.getPrice())
                .sold(product.isSold())
                .creationDate(product.getCreationDate())
                .lastChange(product.getLastChange())
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

    public Product toDomain() {
        var product = new Product(id, serial, name, type, List.copyOf(tags), price, sold);
        setCreationDateToDomain(product);
        product.setLastChange(lastChange);
        return product;
    }

    private void setCreationDateToDomain(BaseInformation product) {
        try {
            var field = product.getClass().getSuperclass().getSuperclass()
                    .getDeclaredField("creationDate");
            field.setAccessible(Boolean.TRUE);
            field.set(product, creationDate);
            field.setAccessible(Boolean.FALSE);
        } catch (Exception ignored) {
        }
    }

}
