package com.market.store.infrastructure;

import com.market.store.domain.ProductRepository;
import com.market.store.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Component
@RequiredArgsConstructor
public class MongoProductRepositoryImp implements ProductRepository {

    private final MongoProductRepository mongoProductRepository;

    @Override
    public Product saveOrUpdate(Product product) {
        return mongoProductRepository.save(product);
    }

    @Repository
    interface MongoProductRepository extends MongoRepository<Product, String> {
    }
}
