package com.market.store.api;

import com.market.infrastructure.web.ApiExceptionHandler;
import com.market.core.domain.EntityNotFoundException;
import com.market.core.domain.search.FieldSearch;
import com.market.core.domain.search.PageSearch;
import com.market.store.api.dto.CountDTO;
import com.market.store.domain.Product;
import com.market.store.domain.Store;
import com.market.store.domain.value.ProductValue;
import com.market.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.market.util.JsonUtil.getJsonValueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class StoreControllerTest {

    @MockBean
    Store store;

    @Autowired
    private MockMvc mvc;

    @Test
    void whenPOSTaddProductThen200() throws Exception {
        var productVl = new ProductValue("id123", "serial123", "name123", "type1", Collections.singleton("red"), 16.54, Boolean.FALSE, new Date(), new Date());

        when(store.addProduct(any(ProductValue.class))).thenReturn(productVl);

        mvc.perform(post("/v1/store/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonValueOf(productVl)))
                .andExpect(status().isOk());

        verify(store, times(1))
                .addProduct(argThat(product ->
                        product.id().equals("id123")
                                && product.serial().equals("serial123")
                                && product.name().equals("name123")
                                && product.type().equals("type1")
                                && product.tags().size() == 1 && product.tags().contains("red")
                                && product.price() == 16.54
                ));
    }

    @Test
    void whenPOSTaddProductWithProductNoSerialThen400AndSerialMsg() throws Exception {
        var productVl = new ProductValue("id123", "", "name123", "type1", Collections.singleton("red"), 16.54, Boolean.FALSE, new Date(), new Date());

        when(store.addProduct(any(ProductValue.class))).thenReturn(productVl);

        var responseContent = mvc.perform(post("/v1/store/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonValueOf(productVl)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        var response = JsonUtil.getValueOfJson(responseContent, ApiExceptionHandler.ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getErrors()).hasSize(1);
        assertThat(response.getErrors()).contains("serial:Product Serial cant be empty.");
    }

    @Test
    void whenPOSTaddProductWithProductNoSerialThen400AndNameMsg() throws Exception {
        var productVl = new ProductValue("id123", "serial123", "", "type1", Collections.singleton("red"), 16.54, Boolean.FALSE, new Date(), new Date());

        when(store.addProduct(any(ProductValue.class))).thenReturn(productVl);

        var responseContent = mvc.perform(post("/v1/store/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonValueOf(productVl)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        var response = JsonUtil.getValueOfJson(responseContent, ApiExceptionHandler.ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getErrors()).hasSize(1);
        assertThat(response.getErrors()).contains("name:Product Name cant be empty.");
    }

    @Test
    void whenPOSTaddProductWithProductNoSerialThen400AndSerialNameMsg() throws Exception {
        var productVl = new ProductValue("id123", "", "", "type1", Collections.singleton("red"), 16.54, Boolean.FALSE, new Date(), new Date());

        when(store.addProduct(any(ProductValue.class))).thenReturn(productVl);

        var responseContent = mvc.perform(post("/v1/store/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonValueOf(productVl)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        var response = JsonUtil.getValueOfJson(responseContent, ApiExceptionHandler.ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getErrors()).hasSize(2);
        assertThat(response.getErrors()).contains("serial:Product Serial cant be empty.");
        assertThat(response.getErrors()).contains("name:Product Name cant be empty.");
    }

    @Test
    void whenGETFindProductsWithParametersThen200() throws Exception {
        when(store.findProducts(any(PageSearch.class))).thenReturn(new ArrayList<>());

        var nameField = new FieldSearch();
        nameField.setName("name");
        nameField.setValue("Yolo");
        var typeField = new FieldSearch("type", "ice", Sort.Direction.DESC, FieldSearch.SearchOperation.CONTAINS, FieldSearch.FieldType.NUMBER);

        var page = new PageSearch(3, 30, List.of(nameField, typeField));

        mvc.perform(post("/v1/store/products/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.getJsonValueOf(page)))
                .andExpect(status().isOk());

        verify(store, times(1)).findProducts(argThat(search ->
                search.getPage() == 3
                        && search.getSize() == 30
                        && search.getFields().size() == 2
                        && search.getFields().get(0).getName().equals("name")
                        && search.getFields().get(0).getValue().equals("Yolo")
                        && search.getFields().get(0).getSort().equals(Sort.Direction.ASC)
                        && search.getFields().get(0).getOperation().equals(FieldSearch.SearchOperation.EQUAL)
                        && search.getFields().get(0).getType().equals(FieldSearch.FieldType.TEXT)
                        && search.getFields().get(1).getName().equals("type")
                        && search.getFields().get(1).getValue().equals("ice")
                        && search.getFields().get(1).getSort().equals(Sort.Direction.DESC)
                        && search.getFields().get(1).getOperation().equals(FieldSearch.SearchOperation.CONTAINS)
                        && search.getFields().get(1).getType().equals(FieldSearch.FieldType.NUMBER)
        ));
    }

    @Test
    void whenGETFindProductsWithNoParametersThen200() throws Exception {
        when(store.findProducts(any(PageSearch.class))).thenReturn(new ArrayList<>());

        mvc.perform(post("/v1/store/products/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(store, times(1)).findProducts(argThat(search ->
                search.getPage() == 0
                        && search.getSize() == 20
                        && search.getFields().isEmpty()
        ));
    }

    @Test
    void whenPOSTSellProductThen200() throws Exception {
        var productVl = new ProductValue("id123", "", "", "type1", Collections.singleton("red"), 16.54, Boolean.TRUE, new Date(), new Date());

        when(store.sellProduct(anyString())).thenReturn(productVl);

        mvc.perform(post("/v1/store/products/id123/sell")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(store, times(1)).sellProduct(anyString());
    }

    @Test
    void whenPOSTSellProductWithBlankIdThen400AndBlankProductIdMsg() throws Exception {
        when(store.sellProduct(anyString())).thenThrow(new IllegalStateException("ProductId cant be null or blank."));

        var responseContent = mvc.perform(post("/v1/store/products/   /sell")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        var response = JsonUtil.getValueOfJson(responseContent, ApiExceptionHandler.ErrorResponse.class);

        verify(store, times(1)).sellProduct(anyString());

        assertThat(response).isNotNull();
        assertThat(response.getErrors()).hasSize(1);
        assertThat(response.getErrors()).contains("msg:ProductId cant be null or blank.");
    }

    @Test
    void whenPOSTSellSoldProductThen400AndSoldProductIdMsg() throws Exception {
        when(store.sellProduct(anyString())).thenThrow(new IllegalStateException("Product id123 has already been sold."));

        var responseContent = mvc.perform(post("/v1/store/products/id123/sell")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        var response = JsonUtil.getValueOfJson(responseContent, ApiExceptionHandler.ErrorResponse.class);

        verify(store, times(1)).sellProduct(anyString());

        assertThat(response).isNotNull();
        assertThat(response.getErrors()).hasSize(1);
        assertThat(response.getErrors()).contains("msg:Product id123 has already been sold.");
    }

    @Test
    void whenPOSTSellNotFoundProductThen404AndNotFoundProductIdMsg() throws Exception {
        when(store.sellProduct(anyString())).thenThrow(new EntityNotFoundException("Product id123 not found."));

        var responseContent = mvc.perform(post("/v1/store/products/id123/sell")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        var response = JsonUtil.getValueOfJson(responseContent, ApiExceptionHandler.ErrorResponse.class);

        verify(store, times(1)).sellProduct(anyString());

        assertThat(response).isNotNull();
        assertThat(response.getErrors()).hasSize(1);
        assertThat(response.getErrors()).contains("msg:Product id123 not found.");
    }

    @Test
    void WhenGETProductsCountThen200() throws Exception {
        when(store.getTotalProducts()).thenReturn(15l);

        var responseContent = mvc.perform(get("/v1/store/products/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var response = JsonUtil.getValueOfJson(responseContent, CountDTO.class);

        assertThat(response.getCount()).isEqualTo(15);

    }
}