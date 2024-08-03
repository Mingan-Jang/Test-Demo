package com.jjk.excel_import.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;


/**
 * Custom exception class to handle REST API errors.
 * 
 * <p>This exception class is designed to be used in a Spring Boot application
 * to provide detailed error information for REST API endpoints. It includes
 * HTTP status codes and custom error codes to help identify specific issues.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * if (someConditionFails) {
 *     throw new RestException(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "The provided input is invalid");
 * }
 * }
 * </pre>
 * 
 * <p>The exception can also be thrown with a default HTTP status of {@code HttpStatus.OK}:</p>
 * <pre>
 * {@code
 * if (someConditionFails) {
 *     throw new RestException("ERROR_CODE", "Detailed error message");
 * }
 * }
 * </pre>
 * 
 * <p>This exception can be caught and handled by a global exception handler to provide
 * consistent error responses to clients.</p>
 * 
 * @see org.springframework.web.bind.annotation.ControllerAdvice
 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
 */
@AllArgsConstructor
@Builder
public class RestException extends RuntimeException {

    /**
     * The HTTP status to be returned with the error response.
     */
    private HttpStatus httpStatus;

    /**
     * A custom error code to identify the specific error.
     */
    private String errorCode;

    /**
     * Constructs a new {@code RestException} with the specified error code and detail message.
     * The HTTP status defaults to {@code HttpStatus.OK}.
     * 
     * @param errorCode the custom error code
     * @param msg the detail message
     */
    public RestException(String errorCode, String msg) {
        super(msg);
        this.httpStatus = HttpStatus.OK;
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new {@code RestException} with the specified HTTP status, error code, detail message, and cause.
     * 
     * @param httpStatus the HTTP status to be returned with the error response
     * @param errorCode the custom error code
     * @param msg the detail message
     * @param e the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public RestException(HttpStatus httpStatus, String errorCode, String msg, Throwable e) {
        super(msg, e);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    /**
     * Returns the HTTP status associated with this exception.
     * 
     * @return the HTTP status
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * Returns the custom error code associated with this exception.
     * 
     * @return the custom error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}