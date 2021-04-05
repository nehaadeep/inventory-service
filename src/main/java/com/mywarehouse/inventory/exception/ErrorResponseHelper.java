package com.mywarehouse.inventory.exception;

import com.mywarehouse.inventory.model.ErrorResponse;
import com.mywarehouse.inventory.model.MultiStatusResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: Helper class for handling error codes
 * @author: Suresh_Vannale
 */
@Component
public class ErrorResponseHelper {
    /**
     * Map error code, statusCode and message to {@link com.mywarehouse.inventory.model.ErrorResponse}
     *
     * @param errorCode    of type String
     * @param errorMessage of type String
     * @return ErrorResponse
     */
    public ErrorResponse mapErrorResponse(String errorCode, String errorMessage) {
        return new ErrorResponse().code(errorCode).message(errorMessage);
    }

    /**
     * Map error code, statusCode and message to {@link com.mywarehouse.inventory.model.ErrorResponse}
     *
     * @param errorCode    of type String
     * @param errorMessage of type String
     * @param errorDetails of type List
     * @return ErrorResponse
     */
    public ErrorResponse mapErrorResponse(String errorCode, String errorMessage, List<String> errorDetails) {
        return new ErrorResponse().code(errorCode).message(errorMessage).errorDetails(errorDetails);
    }

    /**
     * Map error code, statusCode and message to {@link com.mywarehouse.inventory.model.ErrorResponse}
     *
     * @param code    of type String
     * @param responseDetails of type List
     * @return MultiStatusResponse
     */
    public MultiStatusResponse mapMultiStatusResponse(String code, List<String> responseDetails) {
        return new MultiStatusResponse().code(code).responseDetails(responseDetails);
    }
}
