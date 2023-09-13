package com.micro.product.controller;import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.product.dto.ProductRequest;
import com.micro.product.model.Product;
import com.micro.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        List<Product> products = getMockProducts();
        when(productService.getAllProducts()).thenReturn(products);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
                .andExpect(status().isOk());
    }

    private ProductRequest getProductRequest() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setDescription("iPhone 13");
        productRequest.setName("iPhone 13");
        productRequest.setPrice(BigDecimal.valueOf(1200));
        return productRequest;
    }

    private List<Product> getMockProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("1", "Product 1", "Description 1", BigDecimal.valueOf(100)));
        products.add(new Product("", "Product 2", "Description 2", BigDecimal.valueOf(200)));
        return products;
    }
}
