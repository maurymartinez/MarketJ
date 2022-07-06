package com.market.store.api;

import com.market.store.api.dto.ProductDTO;
import com.market.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final Store store;

    @PostMapping("/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO) {

        var storedProduct = store.addProduct(productDTO.toEntity());

        return ResponseEntity.ok(ProductDTO.from(storedProduct));
    }
}
