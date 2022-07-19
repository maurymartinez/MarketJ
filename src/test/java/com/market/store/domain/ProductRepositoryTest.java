package com.market.store.domain;

import com.market.core.domain.search.FieldSearch;
import com.market.core.domain.search.PageSearch;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRepositoryTest {

    @Autowired
    ProductRepository repository;

    @BeforeAll
    void beforeAll() {
        fillDB(45);
    }

    @Test
    void findAllWithoutSearchFieldMustReturnFirst20Products() {

        var pageSearch = new PageSearch();

        pageSearch.setFields(new ArrayList<>());

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(20);
    }

    @Test
    void findAllWithoutSearchFieldMustReturnFirst40Products() {

        var pageSearch = new PageSearch();
        pageSearch.setSize(40);

        pageSearch.setFields(new ArrayList<>());

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(40);
    }

    @Test
    void findAllWithSearchFieldTypeIsType4MustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldSearch = new ArrayList<FieldSearch>();
        fieldSearch.add(new FieldSearch("type", "type4"));
        pageSearch.setFields(fieldSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(9);
    }

    @Test
    void findAllWithSearchFieldTypeIsNotType4MustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldsSearch = new ArrayList<FieldSearch>();
        var fieldSearch = new FieldSearch("type", "type4");
        fieldSearch.setOperation(FieldSearch.SearchOperation.NOT_EQUAL);
        fieldsSearch.add(fieldSearch);
        pageSearch.setFields(fieldsSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(36);
    }

    @Test
    void findAllWithSearchFieldNameIsName3MustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldSearch = new ArrayList<FieldSearch>();
        fieldSearch.add(new FieldSearch("name", "name3"));
        pageSearch.setFields(fieldSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(1);
    }

    @Test
    void findAllWithSearchFieldNameIsNotName3MustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldsSearch = new ArrayList<FieldSearch>();
        var fieldSearch = new FieldSearch("name", "name3");
        fieldSearch.setOperation(FieldSearch.SearchOperation.NOT_EQUAL);
        fieldsSearch.add(fieldSearch);
        pageSearch.setFields(fieldsSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(44);
    }

    @Test
    void findAllWithSearchFieldPriceGTEMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldsSearch = new ArrayList<FieldSearch>();
        var fieldSearch = new FieldSearch("price", "24");
        fieldSearch.setOperation(FieldSearch.SearchOperation.GREATER_THAN_EQUAL);
        fieldSearch.setType(FieldSearch.FieldType.NUMBER);
        fieldsSearch.add(fieldSearch);
        pageSearch.setFields(fieldsSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(36);
    }

    @Test
    void findAllWithSearchFieldPriceLTEMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldsSearch = new ArrayList<FieldSearch>();
        var fieldSearch = new FieldSearch("price", "25");
        fieldSearch.setOperation(FieldSearch.SearchOperation.LESS_THAN_EQUAL);
        fieldSearch.setType(FieldSearch.FieldType.NUMBER);
        fieldsSearch.add(fieldSearch);
        pageSearch.setFields(fieldsSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(27);
    }

    @Test
    void findAllWithSearchFieldIsSoldMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldsSearch = new ArrayList<FieldSearch>();
        var fieldSearch = new FieldSearch("sold", "true");
        fieldSearch.setType(FieldSearch.FieldType.BOOLEAN);
        fieldsSearch.add(fieldSearch);
        pageSearch.setFields(fieldsSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(22);
    }

    @Test
    void findAllWithSearchFieldIsNoSoldMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldsSearch = new ArrayList<FieldSearch>();
        var fieldSearch = new FieldSearch("sold", "true");
        fieldSearch.setType(FieldSearch.FieldType.BOOLEAN);
        fieldSearch.setOperation(FieldSearch.SearchOperation.NOT_EQUAL);
        fieldsSearch.add(fieldSearch);
        pageSearch.setFields(fieldsSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(23);
    }

    @Test
    void findAllWithSearchFieldTagContainRedMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldsSearch = new ArrayList<FieldSearch>();
        var fieldSearch = new FieldSearch("tags", "red");
        fieldSearch.setType(FieldSearch.FieldType.LIST);
        fieldSearch.setOperation(FieldSearch.SearchOperation.CONTAINS);
        fieldsSearch.add(fieldSearch);
        pageSearch.setFields(fieldsSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(15);
    }

    @Test
    void findAllWithSearchFieldTagNoContainRedMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldsSearch = new ArrayList<FieldSearch>();
        var fieldSearch = new FieldSearch("tags", "red");
        fieldSearch.setType(FieldSearch.FieldType.LIST);
        fieldSearch.setOperation(FieldSearch.SearchOperation.DOES_NOT_CONTAIN);
        fieldsSearch.add(fieldSearch);
        pageSearch.setFields(fieldsSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(30);
    }

    @Test
    void findAllWithSearchFieldSortedByNameAscMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldSearch = new ArrayList<FieldSearch>();
        fieldSearch.add(new FieldSearch("name"));
        pageSearch.setFields(fieldSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(45);
        assertThat(fields.stream().map(Product::getName)).isSortedAccordingTo(Comparator.naturalOrder());
    }

    @Test
    void findAllWithSearchFieldSortedByNameDescMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldSearch = new ArrayList<FieldSearch>();
        var field = new FieldSearch("name");
        field.setSort(Sort.Direction.DESC);
        fieldSearch.add(field);
        pageSearch.setFields(fieldSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(45);
        assertThat(fields.stream().map(Product::getName)).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    void findAllWithSearchFieldSortedByTypeAscMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldSearch = new ArrayList<FieldSearch>();
        fieldSearch.add(new FieldSearch("type"));
        pageSearch.setFields(fieldSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(45);
        assertThat(fields.stream().map(Product::getType)).isSortedAccordingTo(Comparator.naturalOrder());
    }

    @Test
    void findAllWithSearchFieldSortedByTypeDescMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldSearch = new ArrayList<FieldSearch>();
        var field = new FieldSearch("type");
        field.setSort(Sort.Direction.DESC);
        fieldSearch.add(field);
        pageSearch.setFields(fieldSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(45);
        assertThat(fields.stream().map(Product::getType)).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    void findAllWithSearchFieldSortedByPriceAscMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldSearch = new ArrayList<FieldSearch>();
        fieldSearch.add(new FieldSearch("price"));
        pageSearch.setFields(fieldSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(45);
        assertThat(fields.stream().map(Product::getPrice)).isSortedAccordingTo(Comparator.naturalOrder());
    }

    @Test
    void findAllWithSearchFieldSortedByPriceDescMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldSearch = new ArrayList<FieldSearch>();
        var field = new FieldSearch("price");
        field.setSort(Sort.Direction.DESC);
        fieldSearch.add(field);
        pageSearch.setFields(fieldSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(45);
        assertThat(fields.stream().map(Product::getPrice)).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    void findAllWithSearchFieldSortedBySoldAscMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldSearch = new ArrayList<FieldSearch>();
        fieldSearch.add(new FieldSearch("sold"));
        pageSearch.setFields(fieldSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(45);
        assertThat(fields.stream().map(Product::isSold)).isSortedAccordingTo(Comparator.naturalOrder());
    }

    @Test
    void findAllWithSearchFieldSSortedBySoldDescMustReturnWantedProducts() {
        var pageSearch = new PageSearch();
        var fieldSearch = new ArrayList<FieldSearch>();
        var field = new FieldSearch("sold");
        field.setSort(Sort.Direction.DESC);
        fieldSearch.add(field);
        pageSearch.setFields(fieldSearch);
        pageSearch.setSize(45);

        var fields = repository.findAll(pageSearch);

        assertThat(fields).hasSize(45);
        assertThat(fields.stream().map(Product::isSold)).isSortedAccordingTo(Comparator.reverseOrder());
    }

    private void fillDB(int amount) {
        Function<Integer, List<String>> tagsGenerator = integer -> {
            List<String> tags = Lists.newArrayList();
            if (integer % 3 == 0)
                tags.add("red");
            if (integer % 5 == 0)
                tags.add("blue");
            if (tags.isEmpty())
                tags.add("yellow");
            return tags;
        };

        IntStream.range(1, amount + 1)
                .mapToObj(i -> new Product(UUID.randomUUID().toString(), "name" + i, "type" + i % 5, tagsGenerator.apply(i), 23.0 + i % 5, i % 2 == 0))
                .forEach(repository::saveOrUpdate);
    }

}
