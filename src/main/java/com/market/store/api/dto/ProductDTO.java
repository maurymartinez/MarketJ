package com.market.store.api.dto;

import com.market.store.domain.value.ProductValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private String id;
    @NotBlank(message = "Product Serial cant be empty.")
    private String serial;
    @NotBlank(message = "Product Name cant be empty.")
    private String name;

    private String type;
    private Collection<String> tags;
    private double price;

    private boolean sold;

    public ProductValue toDomainValue() {
        return new ProductValue(id, serial, name, type, tags, price, sold, null, null);

    }
}
