package com.micro.product;import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.product.dto.ProductRequest;
import com.micro.product.repository.ProductRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@SpringBootTest
class ProductServiceApplicationTests {

	 @Container
	    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	    @Autowired
	    private MockMvc mockMvc;
	    @Autowired
	    private ObjectMapper objectMapper;
	    @Autowired
	    private ProductRepository productRepository;

	    static {
	        mongoDBContainer.start();
	    }

	    @DynamicPropertySource
	    static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
	        dymDynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	    }

	    @Test
	    void shouldCreateProduct() throws Exception {
	        ProductRequest productRequest = getProductRequest();
	        String productRequestString = objectMapper.writeValueAsString(productRequest);
	        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(productRequestString))
	                .andExpect(status().isCreated());
	        Assertions.assertEquals(1, productRepository.findAll().size());
	    }

    private ProductRequest getProductRequest() {
    	ProductRequest productRequest= new ProductRequest();
    	productRequest.setDescription("iPhone 13");
    	productRequest.setName("iPhone 13");
    	productRequest.setPrice(BigDecimal.valueOf(1200));
        return productRequest;
    }


}
