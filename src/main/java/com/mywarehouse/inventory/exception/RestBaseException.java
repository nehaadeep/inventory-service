package com.mywarehouse.inventory.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * @description: List of exceptions to be thrown
 * @author: Suresh_Vannale
 */
@Getter
@AllArgsConstructor
public class RestBaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String code;
    private final List<String> responseDetails;
    private final HttpStatus httpStatus;
}