package com.market.store.api;

import com.market.core.api.ApiExceptionHandler;
import com.market.core.domain.search.FieldSearch;
import com.market.core.domain.search.PageSearch;
import com.market.store.api.dto.ProductDTO;
import com.market.store.domain.Product;
import com.market.store.domain.Store;
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
import java.util.List;

import static com.market.util.JsonUtil.getJsonValueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
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
        var productDTO = new ProductDTO("id123", "serial123", "name123", "type1", Collections.singleton("red"), 16.54);

        when(store.addProduct(any(Product.class))).thenReturn(productDTO.toEntity());

        mvc.perform(post("/v1/store/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonValueOf(productDTO)))
                .andExpect(status().isOk());

        verify(store, times(1))
                .addProduct(argThat(product ->
                        product.getId().equals("id123")
                                && product.getSerial().equals("serial123")
                                && product.getName().equals("name123")
                                && product.getType().equals("type1")
                                && product.getTags().size() == 1 && product.getTags().contains("red")
                                && product.getPrice() == 16.54
                ));
    }

    @Test
    void whenPOSTaddProductWithProductNoSerialThen400AndSerialMsg() throws Exception {
        var productDTO = new ProductDTO("id123", "", "name123", "type1", Collections.singleton("red"), 16.54);

        when(store.addProduct(any(Product.class))).thenReturn(productDTO.toEntity());

        var responseContent = mvc.perform(post("/v1/store/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonValueOf(productDTO)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        var response = JsonUtil.getValueOfJson(responseContent, ApiExceptionHandler.ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getErrors()).hasSize(1);
        assertThat(response.getErrors()).contains("serial:Product Serial cant be empty.");
    }

    @Test
    void whenPOSTaddProductWithProductNoSerialThen400AndNameMsg() throws Exception {
        var productDTO = new ProductDTO("id123", "serial123", "", "type1", Collections.singleton("red"), 16.54);

        when(store.addProduct(any(Product.class))).thenReturn(productDTO.toEntity());

        var responseContent = mvc.perform(post("/v1/store/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonValueOf(productDTO)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        var response = JsonUtil.getValueOfJson(responseContent, ApiExceptionHandler.ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getErrors()).hasSize(1);
        assertThat(response.getErrors()).contains("name:Product Name cant be empty.");
    }

    @Test
    void whenPOSTaddProductWithProductNoSerialThen400AndSerialNameMsg() throws Exception {
        var productDTO = new ProductDTO("id123", "", "", "type1", Collections.singleton("red"), 16.54);

        when(store.addProduct(any(Product.class))).thenReturn(productDTO.toEntity());

        var responseContent = mvc.perform(post("/v1/store/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonValueOf(productDTO)))
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
}