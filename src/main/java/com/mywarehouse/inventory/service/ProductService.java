package com.mywarehouse.inventory.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mywarehouse.inventory.entity.Inventory;
import com.mywarehouse.inventory.exception.ErrorCode;
import com.mywarehouse.inventory.exception.RestBaseException;
import com.mywarehouse.inventory.model.*;
import com.mywarehouse.inventory.repository.InventoryJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: Service class for product
 * @author: Suresh Vannale
 */
@Service
public class ProductService {

    @Autowired
    private InventoryJpaRepository inventoryJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    /**
     * Save or update products inventory from file
     *
     * @param multipartFile of type {@link MultipartFile}
     * @param fileType      of type {@link FileType}
     * @throws IOException throws {@link IOException} if readValue fails
     */

    @Transactional
    public void saveProductsOrInventory(MultipartFile multipartFile, FileType fileType) throws IOException {
        LOGGER.info("Start saveProductsOrInventory()");
        switch (fileType) {
            case PRODUCTS:
                ProductRequest productRequest = objectMapper.readValue(multipartFile.getBytes(), ProductRequest.class);
                List<Inventory> inventories = new ArrayList<>();
                productRequest.getProducts().forEach(product -> product.getArticles().forEach(article -> {
                    Inventory entityInventory = inventoryJpaRepository.findOneByArticleIdAndName(article.getId(), product.getName());
                    if (entityInventory == null) {
                        entityInventory = new Inventory();
                    }
                    entityInventory.setName(product.getName());
                    entityInventory.setStock(entityInventory.getStock() == null ? article.getQuantity() : entityInventory.getStock() + article.getQuantity());
                    entityInventory.setArticleId(article.getId());
                    inventories.add(entityInventory);
                }));
                inventoryJpaRepository.saveAll(inventories);
                break;
            case ARTICLES:
                InventoryRequest inventoryRequest = objectMapper.readValue(multipartFile.getBytes(), InventoryRequest.class);
                inventories = new ArrayList<>();
                for (com.mywarehouse.inventory.model.Inventory apiInventory : inventoryRequest.getInventory()) {
                    Inventory entityInventory = inventoryJpaRepository.findOneByArticleIdAndName(apiInventory.getArticleId(), apiInventory.getName());
                    if (entityInventory == null) {
                        entityInventory = new Inventory();
                    }
                    entityInventory.setName(apiInventory.getName());
                    entityInventory.setStock(entityInventory.getStock() == null ? apiInventory.getStock() : apiInventory.getStock() + entityInventory.getStock());
                    entityInventory.setArticleId(apiInventory.getArticleId());
                    inventories.add(entityInventory);
                }
                inventoryJpaRepository.saveAll(inventories);
                break;
            default:
                break;
        }
        LOGGER.info("End saveProductsOrInventory()");
    }

    /**
     * Update inventory after successful purchase
     *
     * @param products of type {@link List<Product>}
     */
    @Transactional
    public void updateInventory(List<Product> products) {
        LOGGER.info("Start updateInventory()");
        Set<Integer> integerSet = new HashSet<>();
        List<String> responseDetails = new ArrayList<>();
        for (Product product : products) {
            for (Article article : product.getArticles()) {
                Inventory inventory = inventoryJpaRepository.findOneByArticleIdAndName(article.getId(), product.getName());
                if (inventory == null) {
                    responseDetails.add(String.format("No stock/article available for article [%s]", article.getId()));
                    integerSet.add(1);
                    continue;
                }
                if (inventory.getStock().equals(article.getQuantity())) {
                    inventoryJpaRepository.deleteByArticleIdAndName(article.getId(), product.getName());
                    responseDetails.add(String.format("Successfully you bought article [%s] ", article.getId()));
                    integerSet.add(2);
                } else if (inventory.getStock() > article.getQuantity()) {
                    inventory.setStock(inventory.getStock() - article.getQuantity());
                    inventoryJpaRepository.save(inventory);
                    responseDetails.add(String.format("Successfully you bought article [%s] ", article.getId()));
                    integerSet.add(2);
                } else {
                    responseDetails.add(String.format("Limited stock available for article [%s]", article.getId()));
                    integerSet.add(1);
                }
            }
        }

        if (integerSet.size() > 1 && !CollectionUtils.isEmpty(responseDetails)) {
            LOGGER.warn("Multi status request processed");
            throw new RestBaseException(ErrorCode.MULTI_STATUS.getErrorCode(), responseDetails, HttpStatus.MULTI_STATUS);
        }
        if (integerSet.contains(1) && !CollectionUtils.isEmpty(responseDetails)) {
            LOGGER.warn("Error request processed");
            throw new RestBaseException(ErrorCode.BAD_REQUEST.getErrorCode(), responseDetails, HttpStatus.BAD_REQUEST);
        }
        LOGGER.info("End updateInventory()");
    }

    /**
     * Find current stock of inventory
     *
     * @return of type {@link List<Product>}
     */
    public List<Product> findAllProducts() {
        LOGGER.info("Start findAllProducts()");
        List<Inventory> inventories = inventoryJpaRepository.findAll();
        List<Product> products = mapProduct(inventories);
        LOGGER.info("End findAllProducts()");
        return products;
    }

    /**
     * Map {@link Inventory} to {@link Product}
     *
     * @param inventories of type {@link List<Inventory>}
     * @return of type {@link List<Product>}
     */
    private List<Product> mapProduct(List<Inventory> inventories) {
        LOGGER.info("Start mapProduct()");
        List<Product> products = new ArrayList<>();
        if (!CollectionUtils.isEmpty(inventories)) {
            Map<String, List<Inventory>> inventoryMap = inventories.stream().collect(Collectors.groupingBy(Inventory::getName));
            inventoryMap.forEach((key, tempInventories) -> {
                Product product = new Product();
                product.setName(key);
                List<Article> articles = new ArrayList<>();
                tempInventories.forEach(inventory -> {
                    Article article = new Article();
                    article.setId(inventory.getArticleId());
                    article.setQuantity(inventory.getStock());
                    articles.add(article);
                });
                product.setArticles(articles);
                products.add(product);
            });
        }
        LOGGER.info("End mapProduct()");
        return products;
    }
}