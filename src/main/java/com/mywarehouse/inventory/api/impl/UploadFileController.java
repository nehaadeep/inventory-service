package com.mywarehouse.inventory.api.impl;

import com.mywarehouse.inventory.model.FileType;
import com.mywarehouse.inventory.model.SuccessResponse;
import com.mywarehouse.inventory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @description: Controller class for file upload
 * @author: Suresh Vannale
 */
@RestController
public class UploadFileController {

    @Autowired
    private ProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileController.class);

    /**
     * API operation to upload file which contains articles
     *
     * @param file     of type {@link MultipartFile}
     * @param fileType of type {@link FileType}
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException from {@link ProductService}
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> uploadFile(@RequestParam("file") MultipartFile file, @Valid @RequestParam("fileType") FileType fileType) throws IOException {
        LOGGER.info("Start uploadFile()");
        productService.saveProductsOrInventory(file, fileType);
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage(String.format("Successfully uploaded [%s]", fileType));
        successResponse.setStatus("SUCCESS");
        LOGGER.info("End uploadFile()");
        return ResponseEntity.ok(successResponse);
    }
}
