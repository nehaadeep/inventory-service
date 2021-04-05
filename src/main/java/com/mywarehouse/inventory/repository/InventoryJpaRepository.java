package com.mywarehouse.inventory.repository;

import com.mywarehouse.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryJpaRepository extends JpaRepository<Inventory, Integer> {
    void deleteByArticleIdAndName(String articleId, String name);

    Inventory findOneByArticleIdAndName(String articleId, String name);
}