package com.mywarehouse.inventory.exception;

/**
 * @description: List the error  codes here
 * @author: Suresh_Vannale
 */
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),

    MULTI_STATUS("MULTI_STATUS"),

    BAD_REQUEST("BAD_REQUEST");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getErrorCode() {
        return code;
    }
}
