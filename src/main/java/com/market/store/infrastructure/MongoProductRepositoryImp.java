package com.market.store.infrastructure;

import com.market.core.domain.search.FieldSearch;
import com.market.core.domain.search.PageSearch;
import com.market.store.domain.Product;
import com.market.store.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@Component
@RequiredArgsConstructor
public class MongoProductRepositoryImp implements ProductRepository {
    private final MongoOperations mongoOperations;
    private final MongoProductRepository mongoProductRepository;

    @Override
    public Product saveOrUpdate(Product product) {
        return mongoProductRepository.save(product);
    }

    @Override
    public Collection<Product> findAll(PageSearch search) {
        var operations = new ArrayList<AggregationOperation>();

        addSearchValues(operations, search);
        addOrder(operations, search);
        addPage(operations, search);

        return mongoOperations.aggregate(newAggregation(operations), Product.class, Product.class).getMappedResults();
    }

    private void addSearchValues(List<AggregationOperation> operations, PageSearch search) {
        var query = new Criteria();

        for (FieldSearch field : search.getFields())
            if (field.getConvertedValue().isPresent())
                switch (field.getOperation()) {
                    case NOT_EQUAL -> query.and(field.getName()).ne(field.getConvertedValue().get());
                    case CONTAINS -> query.and(field.getName()).all((Collection<?>) field.getConvertedValue().get());
                    case DOES_NOT_CONTAIN ->
                            query.and(field.getName()).not().in((Collection<?>) field.getConvertedValue().get());
                    case GREATER_THAN -> query.and(field.getName()).gt(field.getConvertedValue().get());
                    case GREATER_THAN_EQUAL -> query.and(field.getName()).gte(field.getConvertedValue().get());
                    case LESS_THAN -> query.and(field.getName()).lt(field.getConvertedValue().get());
                    case LESS_THAN_EQUAL -> query.and(field.getName()).lte(field.getConvertedValue().get());
                    default -> query.and(field.getName()).is(field.getConvertedValue().get());
                }

        operations.add(match(query));
    }

    private void addOrder(List<AggregationOperation> operations, PageSearch search) {
        var orders = search.getFields().stream()
                .filter(fieldSearch -> Strings.isNotBlank(fieldSearch.getName()))
                .map(field -> new Sort.Order(field.getSort(), field.getName()))
                .collect(Collectors.toList());

        if (!orders.isEmpty())
            operations.add(sort(Sort.by(orders)));
    }

    private void addPage(List<AggregationOperation> operations, PageSearch search) {
        long offset = (long) search.getSize() * search.getPage();
        operations.add(skip(offset));
        operations.add(limit(search.getSize()));
    }

    @Repository
    interface MongoProductRepository extends MongoRepository<Product, String> {
    }
}
