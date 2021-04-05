package com.mywarehouse.inventory.api.impl;

import com.mywarehouse.inventory.model.FileType;
import com.mywarehouse.inventory.model.SuccessResponse;
import com.mywarehouse.inventory.service.ProductService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
@DisplayName("Upload API from file")
class UploadFileControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private UploadFileController uploadFileController;

    @Nested
    class WhenUploadingFile {
        @Test
        @DisplayName("Upload API test")
        void testUploadTest() throws IOException {
            File file = new File("src/test/resources/ProductUpload.json");
            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("file",
                    file.getName(), "text/plain", IOUtils.toByteArray(input));
            doNothing().when(productService).saveProductsOrInventory(multipartFile, FileType.PRODUCTS);
            ResponseEntity<SuccessResponse> responseEntity = uploadFileController.uploadFile(multipartFile, FileType.PRODUCTS);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "HttpStatus is not matching");
            assertNotNull(responseEntity, "ResponseEntity is null");
        }
    }
}