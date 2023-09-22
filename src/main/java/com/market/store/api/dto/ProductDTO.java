package com.market.store.api.dto;

import com.market.store.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    public static ProductDTO from(Product product) {
        var productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setSerial(product.getSerial());
        productDTO.setType(product.getType());
        productDTO.setTags(product.getTags());
        productDTO.setPrice(product.getPrice());
        productDTO.setSold(product.isSold());

        return productDTO;
    }

    private String id;
    @NotBlank(message = "Product Serial cant be empty.")
    private String serial;
    @NotBlank(message = "Product Name cant be empty.")
    private String name;

    private String type;
    private Collection<String> tags;
    private double price;

    private boolean sold;

    public Product toEntity() {
        return new Product(id, serial, name, type, tags, price, sold);

    }
}
