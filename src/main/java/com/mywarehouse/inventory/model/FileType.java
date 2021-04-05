package com.mywarehouse.inventory.model;

/**
 * @description: Enum for file type
 * @author: Suresh Vannale
 */
public enum FileType {
    PRODUCTS("PRODUCTS"),
    ARTICLES("ARTICLES");

    private String fileType;

    FileType(String fileType) {
        this.fileType = fileType;
    }
}
