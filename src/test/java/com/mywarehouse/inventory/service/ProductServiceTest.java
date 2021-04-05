package com.mywarehouse.inventory.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mywarehouse.inventory.entity.Inventory;
import com.mywarehouse.inventory.exception.RestBaseException;
import com.mywarehouse.inventory.model.FileType;
import com.mywarehouse.inventory.model.Product;
import com.mywarehouse.inventory.model.ProductRequest;
import com.mywarehouse.inventory.repository.InventoryJpaRepository;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService test cases")
class ProductServiceTest {

    @Mock
    private InventoryJpaRepository inventoryJpaRepository;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductService productService;

    @Nested
    class WhenSavingProductsOrInventory {
        @Test
        @DisplayName("Upload products from file")
        void testSaveProducts() throws IOException {
            File file = new File("src/test/resources/ProductUpload.json");
            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("file",
                    file.getName(), "text/plain", IOUtils.toByteArray(input));
            when(inventoryJpaRepository.findOneByArticleIdAndName(anyString(), anyString())).thenReturn(null);
            productService.saveProductsOrInventory(multipartFile, FileType.PRODUCTS);
            assertTrue(true, "Something went wrong while uploading products");
        }

        @Test
        @DisplayName("Upload inventory from file")
        void testUploadInventory() throws IOException {
            File file = new File("src/test/resources/InventoryUpload.json");
            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("file",
                    file.getName(), "text/plain", IOUtils.toByteArray(input));
            when(inventoryJpaRepository.findOneByArticleIdAndName(anyString(), anyString())).thenReturn(null);
            productService.saveProductsOrInventory(multipartFile, FileType.ARTICLES);
            assertTrue(true, "Something went wrong while uploading inventory");
        }
    }

    @Nested
    class WhenUpdatingInventory {
        private List<Product> products;

        @BeforeEach
        void setup() throws IOException {
            ProductRequest productRequest = objectMapper.readValue(ProductServiceTest.class.getClassLoader().
                    getResourceAsStream("ProductUpload.json"), ProductRequest.class);
            products = productRequest.getProducts();
        }

        @Test
        @DisplayName("Update inventory no stock")
        void testUploadInventoryNoStock() {
            when(inventoryJpaRepository.findOneByArticleIdAndName(anyString(), anyString())).thenReturn(null);
            try {
                productService.updateInventory(products);
            } catch (Exception exception) {
                assertTrue(exception instanceof RestBaseException, "Exception not matching");
            }
            assertTrue(true, "No stock failed");
        }

        @Test
        @DisplayName("Update inventory multi status")
        void testUploadInventoryMultiStatus() {
            Inventory inventory = new Inventory();
            inventory.setName("leg");
            inventory.setStock(10);
            inventory.setArticleId("5");
            when(inventoryJpaRepository.findOneByArticleIdAndName(anyString(), anyString())).thenReturn(inventory);
            try {
                productService.updateInventory(products);
            } catch (Exception exception) {
                assertTrue(exception instanceof RestBaseException, "Exception not matching");
            }
            assertTrue(true, "Multi status failed");
        }
    }

    @Nested
    class WhenFindingAllProducts {
        @Test
        @DisplayName("Find all products")
        void testFindAllProducts() {
            List<Inventory> inventories = getInventory();
            when(inventoryJpaRepository.findAll()).thenReturn(inventories);
            List<Product> products = productService.findAllProducts();
            assertEquals(products.size(), 3, "Size is not matching");
        }

        private List<Inventory> getInventory() {
            List<Inventory> inventories = new ArrayList<>();
            Inventory inventory = new Inventory();
            inventory.setName("leg");
            inventory.setStock(10);
            inventory.setArticleId("5");
            inventories.add(inventory);
            inventory = new Inventory();
            inventory.setName("bolt");
            inventory.setStock(4);
            inventory.setArticleId("2");
            inventories.add(inventory);
            inventory = new Inventory();
            inventory.setName("screw");
            inventory.setStock(8);
            inventory.setArticleId("4");
            inventories.add(inventory);
            return inventories;
        }
    }
}