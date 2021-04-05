package com.mywarehouse.inventory.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mywarehouse.inventory.model.ProductRequest;
import com.mywarehouse.inventory.model.ProductResponse;
import com.mywarehouse.inventory.model.SuccessResponse;
import com.mywarehouse.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductApi test cases")
class ProductApiTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductApiImpl productApi;

    @Spy
    private ObjectMapper objectMapper;

    private ProductRequest productRequest;

    @BeforeEach
    void setup() throws IOException {
        productRequest = objectMapper.readValue(ProductApiTest.class.getClassLoader().
                getResourceAsStream("ProductUpload.json"), ProductRequest.class);
    }


    @Nested
    class WhenBuyingProducts {
        @Test
        @DisplayName("Buy products success")
        void testBuyProducts() {
            doNothing().when(productService).updateInventory(anyList());
            ResponseEntity<SuccessResponse> responseEntity = productApi.buyProducts(productRequest);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "HttpStatus is not matching");
            assertNotNull(responseEntity, "ResponseEntity is null");
        }
    }

    @Nested
    class WhenGettingAllProducts {
        @Test
        @DisplayName("Find all products success")
        void testBuyProducts() {
            when(productService.findAllProducts()).thenReturn(productRequest.getProducts());
            ResponseEntity<ProductResponse> responseEntity = productApi.getAllProducts();
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "HttpStatus is not matching");
            assertNotNull(responseEntity, "ResponseEntity is null");
            assertTrue(Objects.requireNonNull(responseEntity.getBody()).getProducts().size() > 0, "Size is not matching");
        }
    }
}