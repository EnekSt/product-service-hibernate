package com.example.psh.errorhandling;

import com.example.psh.errorhandling.apierror.ApiError;
import com.example.psh.errorhandling.exceptions.InvalidIdRepresentationException;
import com.example.psh.errorhandling.exceptions.ProductNotFoundException;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.error("Exception " + ex.getClass().getName() + " occurred.", ex);

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.error("Exception " + ex.getClass().getName() + " occurred.", ex);

        List<String> errors = new ArrayList<>();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.error("Exception " + ex.getClass().getName() + " occurred.", ex);

        List<String> errors = new ArrayList<>();
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.error("Exception " + ex.getClass().getName() + " occurred.", ex);

        List<String> errors = new ArrayList<>();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler({ProductNotFoundException.class})
    protected ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {

        logger.error("Exception " + ex.getClass().getName() + " occurred.", ex);

        List<String> errors = new ArrayList<>();
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({InvalidIdRepresentationException.class})
    protected ResponseEntity<Object> handleInvalidIdRepresentationException(InvalidIdRepresentationException ex, WebRequest request) {

        logger.error("Exception " + ex.getClass().getName() + " occurred.", ex);

        List<String> errors = new ArrayList<>();
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({MongoTimeoutException.class})
    protected ResponseEntity<Object> handleMongoTimeoutException(MongoTimeoutException ex, WebRequest request) {

        logger.error("Exception " + ex.getClass().getName() + " occurred.", ex);

        List<String> errors = new ArrayList<>();
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Timed out. No response from MongoDB", errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({MongoException.class})
    protected ResponseEntity<Object> handleMongoException(MongoException ex, WebRequest request) {

        logger.error("Exception " + ex.getClass().getName() + " occurred.", ex);

        List<String> errors = new ArrayList<>();
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Something happened to MongoDB", errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    // Default handler of unexpected errors
    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {

        logger.error("Unexpected Exception " + ex.getClass().getName() + " occurred.", ex);

        List<String> errors = new ArrayList<>();
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
