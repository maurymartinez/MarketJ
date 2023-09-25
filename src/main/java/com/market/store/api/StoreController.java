package com.market.store.api;

import com.market.core.domain.search.PageSearch;
import com.market.store.api.dto.CountDTO;
import com.market.store.api.dto.ProductDTO;
import com.market.store.domain.Store;
import com.market.store.domain.value.ProductValue;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/store")
@RequiredArgsConstructor
public class StoreController {

    private final Store store;

    @ApiOperation(value = "Save Product", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/products")
    public ResponseEntity<ProductValue> addProduct(@Valid @RequestBody ProductDTO product) {

        var storedProduct = store.addProduct(product.toDomainValue());

        return ResponseEntity.ok(storedProduct);
    }

    @ApiOperation(value = "Search Product", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/products/search")
    public ResponseEntity<List<ProductValue>> getProduct(@RequestBody(required = false) PageSearch search) {
        if (Objects.isNull(search))
            search = new PageSearch();

        var products = store.findProducts(search).stream().toList();

        return ResponseEntity.ok(products);
    }

    @ApiOperation(value = "Sell Product", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/products/{id}/sell")
    public ResponseEntity<ProductValue> sellProduct(@PathVariable("id") String productId) {
        var productSold = store.sellProduct(productId);

        return ResponseEntity.ok(productSold);
    }

    @ApiOperation(value = "Count of Products on store", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/products/count")
    public ResponseEntity<CountDTO> countProduct() {
        var count = store.getTotalProducts();

        return ResponseEntity.ok(new CountDTO(count));
    }
}
