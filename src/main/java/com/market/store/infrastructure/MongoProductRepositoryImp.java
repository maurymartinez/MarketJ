package com.market.store.infrastructure;

import com.market.core.domain.search.PageSearch;
import com.market.store.domain.Product;
import com.market.store.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;


@Component
@RequiredArgsConstructor
public class MongoProductRepositoryImp extends AdvanceSearch implements ProductRepository {
    private final MongoOperations mongoOperations;
    private final MongoProductRepository mongoProductRepository;

    @Override
    public Product saveOrUpdate(Product product) {
        return mongoProductRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(String productId) {
        return this.mongoProductRepository.findById(productId);
    }

    @Override
    public Collection<Product> findAll(PageSearch search) {
        var operations = buildQuery(search);

        return mongoOperations.aggregate(newAggregation(operations), Product.class, Product.class).getMappedResults();
    }

    @Override
    public long getNumberOfProducts(boolean sold) {
        return mongoProductRepository.countBySold(sold);
    }

    @Repository
    interface MongoProductRepository extends MongoRepository<Product, String> {
        long countBySold(boolean sold);
    }
}
