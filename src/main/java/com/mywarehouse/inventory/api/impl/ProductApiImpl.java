package com.mywarehouse.inventory.api.impl;

import com.mywarehouse.inventory.api.ProductApi;
import com.mywarehouse.inventory.model.Product;
import com.mywarehouse.inventory.model.ProductRequest;
import com.mywarehouse.inventory.model.ProductResponse;
import com.mywarehouse.inventory.model.SuccessResponse;
import com.mywarehouse.inventory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @description: Implementation class for {@link ProductApi}
 * @author: Suresh Vannale
 */
@RestController
public class ProductApiImpl implements ProductApi {

    @Autowired
    private ProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductApiImpl.class);

    /**
     * Buy products and update inventory
     *
     * @param productRequest of type {@link ProductRequest}
     * @return of type {@link ResponseEntity<SuccessResponse>}
     */
    @Override
    public ResponseEntity<SuccessResponse> buyProducts(@Valid ProductRequest productRequest) {
        LOGGER.info("Start buyProducts()");
        productService.updateInventory(productRequest.getProducts());
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage("Successfully you bought all requested products");
        successResponse.setStatus("SUCCESS");
        LOGGER.info("End buyProducts()");
        return ResponseEntity.ok(successResponse);
    }

    /**
     * Get current stock of inventory
     *
     * @return of type {@link ResponseEntity<SuccessResponse>}
     */
    @Override
    public ResponseEntity<ProductResponse> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(products);
        return ResponseEntity.ok(productResponse);
    }
}