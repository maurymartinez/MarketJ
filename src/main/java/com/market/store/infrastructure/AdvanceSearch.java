package com.market.store.infrastructure;

import com.market.core.domain.search.FieldSearch;
import com.market.core.domain.search.PageSearch;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;

public abstract class AdvanceSearch {

    public List<AggregationOperation> buildQuery(PageSearch search) {
        var operations = new ArrayList<AggregationOperation>();

        addSearchValues(operations, search);
        addOrder(operations, search);
        addPage(operations, search);

        return operations;
    }

    protected void addSearchValues(List<AggregationOperation> operations, PageSearch search) {
        var query = new Criteria();

        for (FieldSearch field : search.getFields())
            field.convertedValue().ifPresent(fieldValue -> {
                switch (field.getOperation()) {
                    case NOT_EQUAL -> query.and(field.getName()).ne(fieldValue);
                    case CONTAINS -> query.and(field.getName()).all(castOrCreateCollection(fieldValue));
                    case DOES_NOT_CONTAIN -> query.and(field.getName()).not().in(castOrCreateCollection(fieldValue));
                    case GREATER_THAN -> query.and(field.getName()).gt(fieldValue);
                    case GREATER_THAN_EQUAL -> query.and(field.getName()).gte(fieldValue);
                    case LESS_THAN -> query.and(field.getName()).lt(fieldValue);
                    case LESS_THAN_EQUAL -> query.and(field.getName()).lte(fieldValue);
                    default -> query.and(field.getName()).is(fieldValue);
                }
            });

        operations.add(match(query));
    }

    private Collection<?> castOrCreateCollection(Object possibleCollection) {

        if (possibleCollection instanceof Collection<?>)
            return (Collection<?>) possibleCollection;

        var collection = new ArrayList<>();
        collection.add(possibleCollection);

        return collection;

    }

    protected void addOrder(List<AggregationOperation> operations, PageSearch search) {
        var orders = search.getFields().stream()
                .filter(fieldSearch -> Strings.isNotBlank(fieldSearch.getName()))
                .map(field -> new Sort.Order(field.getSort(), field.getName()))
                .collect(Collectors.toList());

        if (!orders.isEmpty())
            operations.add(sort(Sort.by(orders)));
    }

    protected void addPage(List<AggregationOperation> operations, PageSearch search) {
        long offset = (long) search.getSize() * search.getPage();
        operations.add(skip(offset));
        operations.add(limit(search.getSize()));
    }
}
