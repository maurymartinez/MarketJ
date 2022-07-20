package com.market.store.api;

import com.market.core.domain.search.PageSearch;
import com.market.store.api.dto.ProductDTO;
import com.market.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

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

    @PostMapping("/product/search")
    public ResponseEntity<List<ProductDTO>> getProduct(@RequestBody(required = false) PageSearch search) {
        if (Objects.isNull(search))
            search = new PageSearch();

        var products = store.findProducts(search).stream().map(ProductDTO::from).toList();

        return ResponseEntity.ok(products);
    }
}
