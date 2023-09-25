package com.market.store.infrastructure;

import com.market.core.domain.search.PageSearch;
import com.market.store.domain.ProductRepository;
import com.market.store.domain.value.ProductValue;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;


@Component
@RequiredArgsConstructor
public class MongoProductRepositoryImp extends AdvanceSearch implements ProductRepository {
    private final MongoOperations mongoOperations;
    private final MongoProductRepository mongoProductRepository;

    @Override
    public ProductValue saveOrUpdate(ProductValue product) {
        var dbEntity = DBProductEntity.from(product);

        if (Objects.isNull(dbEntity.getCreationDate()))
            dbEntity.setCreationDate(new Date());
        dbEntity.setLastChange(new Date());

        var savedProduct = mongoProductRepository.save(dbEntity);

        return savedProduct.toDomainValue();
    }

    @Override
    public Optional<ProductValue> getProductById(String productId) {
        return this.mongoProductRepository.findById(productId).map(DBProductEntity::toDomainValue);
    }

    @Override
    public Collection<ProductValue> findAll(PageSearch search) {
        var operations = buildQuery(search);

        return mongoOperations.aggregate(newAggregation(operations), DBProductEntity.class, DBProductEntity.class).getMappedResults()
                .stream().map(DBProductEntity::toDomainValue)
                .collect(Collectors.toList());
    }

    @Override
    public long getNumberOfProducts(boolean sold) {
        return mongoProductRepository.countBySold(sold);
    }

    @Repository
    interface MongoProductRepository extends MongoRepository<DBProductEntity, String> {
        long countBySold(boolean sold);
    }
}
