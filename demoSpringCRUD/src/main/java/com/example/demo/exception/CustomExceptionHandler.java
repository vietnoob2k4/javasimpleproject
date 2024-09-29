package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {

        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(1001);
//        apiResponse.setMessage(ex.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//
//    }


    //    @ExceptionHandler(AppException.class)
//    public ResponseEntity<ApiResponse> handleAppException(AppException ex) {
    //    ErrorCode errorCode = ex.getErrorCode
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(1001);
//        apiResponse.setMessage(ex.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "An error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}