package com.mywarehouse.inventory.exception;

/**
 * @description: List the error messages here
 * @author: Suresh_Vannale
 */
public enum ErrorMessage {
    INTERNAL_SERVER_ERROR("Something went wrong while processing application"),

    NO_HANDLER_FOUND_ERROR("No handler found for given request"),

    UN_HANDLED_ERROR("Error while processing request"),

    BAD_REQUEST("Invalid input, please verify");

    private final String errorMsg;

    ErrorMessage(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
