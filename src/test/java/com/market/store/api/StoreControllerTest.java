package com.market.store.api;

import com.market.core.api.ApiExceptionHandler;
import com.market.store.api.dto.ProductDTO;
import com.market.store.domain.Product;
import com.market.store.domain.Store;
import com.market.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

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

        mvc.perform(post("/store/product")
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

        var responseContent = mvc.perform(post("/store/product")
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

        var responseContent = mvc.perform(post("/store/product")
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

        var responseContent = mvc.perform(post("/store/product")
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
}